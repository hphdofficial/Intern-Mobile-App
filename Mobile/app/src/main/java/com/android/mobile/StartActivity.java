package com.android.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.mobile.models.LoginModel;
import com.android.mobile.models.TokenModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.UserApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartActivity extends AppCompatActivity {

    private Button btn_login;
    private Button btn_register;
    private EditText editEmail;
    private EditText editPassword;
    private TextView forgotPassword;
    private ImageView iconPasswordVisibility;
    private CheckBox checkboxSavePassword;
    private boolean isPasswordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Kiểm tra trạng thái đăng nhập
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);
        long expiryTime = sharedPreferences.getLong("expiry_time", 0);
        long currentTime = System.currentTimeMillis();
        boolean isPasswordSaved = sharedPreferences.getBoolean("checkbox_save_password", false);

        if (accessToken != null && currentTime < expiryTime) {
            // Người dùng đã đăng nhập và token còn hiệu lực, chuyển hướng đến MenuActivity
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            finish();
            return;
        } else if (accessToken != null && currentTime >= expiryTime) {
            // Token hết hạn, thông báo cho người dùng và xóa thông tin đăng nhập
            Toast.makeText(this, "Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // Nếu không lưu mật khẩu, xóa tất cả thông tin đăng nhập
            if (!isPasswordSaved) {
                editor.clear();
            } else {
                // Chỉ xóa token và thông tin liên quan
                editor.remove("access_token");
                editor.remove("token_type");
                editor.remove("expires_in");
                editor.remove("expiry_time");
            }

            editor.apply();
        }

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        forgotPassword = findViewById(R.id.forgotPassword);
        iconPasswordVisibility = findViewById(R.id.iconPasswordVisibility);
        checkboxSavePassword = findViewById(R.id.checkbox_save_password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
            }
        });

        iconPasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        // Load saved credentials if available
        loadSavedCredentials();
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            iconPasswordVisibility.setImageResource(R.drawable.hide);
        } else {
            editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            iconPasswordVisibility.setImageResource(R.drawable.view);
        }
        isPasswordVisible = !isPasswordVisible;
        editPassword.setSelection(editPassword.getText().length());
    }

    private void loginUser() {
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(StartActivity.this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginModel request = new LoginModel(email, password);
        UserApiService apiService = ApiServiceProvider.getUserApiService();
        Call<TokenModel> call = apiService.loginUser(request);
        call.enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                if (response.isSuccessful()) {
                    TokenModel tokenResponse = response.body();
                    if (tokenResponse != null) {
                        saveLoginDetails(tokenResponse);
                        if (checkboxSavePassword.isChecked()) {
                            saveCredentials(email, password);
                            saveCheckboxState(true); // Lưu trạng thái của checkbox
                        } else {
                            clearCredentials();
                            saveCheckboxState(false); // Lưu trạng thái của checkbox
                        }
                        Toast.makeText(StartActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        finish(); // Đóng StartActivity để người dùng không quay lại trang đăng nhập
                    }


                } else {
                    Toast.makeText(StartActivity.this, "Đăng nhập thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                Toast.makeText(StartActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveLoginDetails(TokenModel tokenResponse) {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("access_token", tokenResponse.getAccess_token());
        editor.putString("token_type", tokenResponse.getToken_type());
        editor.putInt("expires_in", tokenResponse.getExpires_in());
        editor.putInt("member_id", tokenResponse.getMember_id());

        long expiryTime = System.currentTimeMillis() + (tokenResponse.getExpires_in() * 1000); // thời gian hết hạn tính bằng milliseconds
        editor.putLong("expiry_time", expiryTime);
        editor.apply();
    }


    private void saveCheckboxState(boolean isChecked) {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("checkbox_save_password", isChecked);
        editor.apply();
    }


    private void saveCredentials(String email, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("saved_email", email);
        editor.putString("saved_password", password);
        editor.apply();
    }

    private void clearCredentials() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("saved_email");
        editor.remove("saved_password");
        editor.apply();
    }

    private void loadSavedCredentials() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("saved_email", null);
        String savedPassword = sharedPreferences.getString("saved_password", null);

        if (savedEmail != null && savedPassword != null) {
            editEmail.setText(savedEmail);
            editPassword.setText(savedPassword);
            checkboxSavePassword.setChecked(true);
        }
    }
}
