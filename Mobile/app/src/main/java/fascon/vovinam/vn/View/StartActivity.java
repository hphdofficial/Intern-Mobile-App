package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.Model.LoginModel;
import fascon.vovinam.vn.Model.TokenModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.UserApiService;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartActivity extends BaseActivity {
    private TextView text;
    public void onMenuItemClick(View view) {
        text = findViewById(R.id.languageText);
        String language = text.getText()+"";
        if(view.getId() == R.id.btn_change){
            SharedPreferences sga = getSharedPreferences("login_prefs", MODE_PRIVATE);
            SharedPreferences.Editor edit =  sga.edit();

            if(language.contains("VN")){
                edit.putString("language","en");
                text.setText("ENG");
            }else {
                edit.putString("language","vn");
                text.setText("VN");
            }
            edit.apply();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
    private Button btn_login;
    private Button btn_register;
    private EditText editEmail;
    private EditText editPassword;
    private TextView forgotPassword;
    private ImageView iconPasswordVisibility;
    private CheckBox checkboxSavePassword;
    private boolean isPasswordVisible = false;
    private TextView title;

    private TextView subtitle;
    private BlankFragment loadingFragment;
    private String languageS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Reset id
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language", "vn");  // Mặc định là "en"
        SharedPreferences.Editor edit = shared.edit();
        edit.putString("id_club_shared", null);
        edit.putString("id_class_shared", null);
        edit.apply();

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
            showToastBasedOnLanguage("Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại.", "Session expired, please log in again.");
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

        // Ánh xạ các thành phần UI
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        forgotPassword = findViewById(R.id.forgotPassword);
        iconPasswordVisibility = findViewById(R.id.iconPasswordVisibility);
        checkboxSavePassword = findViewById(R.id.checkbox_save_password);
        subtitle = findViewById(R.id.subtitle);
        title = findViewById(R.id.title);

        // Spinner để chọn ngôn ngữ
        Spinner languageSpinner = findViewById(R.id.language_spinner);

        // Thiết lập dữ liệu cho Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.language_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        // Đặt ngôn ngữ hiện tại cho Spinner
        if (languageS.equals("vn")) {
            languageSpinner.setSelection(1); // Chọn Tiếng Việt
        } else {
            languageSpinner.setSelection(0); // Chọn English
        }

        // Cập nhật giao diện theo ngôn ngữ đã lưu
        updateLanguage(languageS);

        // Lắng nghe sự kiện thay đổi ngôn ngữ từ Spinner
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    languageS = "vn"; // Tiếng Việt
                } else {
                    languageS = "en"; // English
                }

                // Lưu ngôn ngữ vào SharedPreferences
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("language", languageS);
                editor.apply();

                // Cập nhật giao diện với ngôn ngữ mới
                updateLanguage(languageS);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì khi không có lựa chọn
            }
        });


        // Gán sự kiện cho các nút và phần tử khác
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

    // Phương thức để hiển thị Toast theo ngôn ngữ người dùng chọn
    private void showToastBasedOnLanguage(String messageVN, String messageEN) {
        if (languageS.equals("vn")) {
            Toast.makeText(this, messageVN, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, messageEN, Toast.LENGTH_SHORT).show();
        }
    }


    // Phương thức để cập nhật giao diện theo ngôn ngữ
    private void updateLanguage(String language) {
        if (language.equals("en")) {
            title.setText("Login");
            editEmail.setHint("Email, Phone, Account");
            editPassword.setHint("Password");
            checkboxSavePassword.setText("Save Password");
            forgotPassword.setText("Forgot Password");
            btn_login.setText("Login");
            btn_register.setText("Register");
            subtitle.setText("Log in to continue using the application");
        } else {
            title.setText("Đăng nhập");
            editEmail.setHint("Email, Số điện thoại, Tài khoản");
            editPassword.setHint("Mật khẩu");
            checkboxSavePassword.setText("Lưu mật khẩu");
            forgotPassword.setText("Quên mật khẩu");
            btn_login.setText("Đăng nhập");
            btn_register.setText("Đăng ký");
            subtitle.setText("Đăng nhập để tiếp tục sử dụng ứng dụng");
        }
    }


    private void showLoading() {
        if (loadingFragment == null) {
            loadingFragment = new BlankFragment();
            loadingFragment.show(getSupportFragmentManager(), "loading");
        }
    }

    private void hideLoading() {
        if (loadingFragment != null) {
            loadingFragment.dismiss();
            loadingFragment = null;
        }
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

    // Phương thức loginUser đã điều chỉnh để hiển thị Toast theo ngôn ngữ người dùng chọn
    private void loginUser() {
        String login = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (login.isEmpty() || password.isEmpty()) {
            showToastBasedOnLanguage("Vui lòng nhập tên tài khoản, email hoặc số điện thoại và mật khẩu",
                    "Please enter your account name, email, or phone number and password.");
            return;
        }

        showLoading();
        LoginModel request = new LoginModel(login, password);
        UserApiService apiService = ApiServiceProvider.getUserApiService();
        Call<TokenModel> call = apiService.loginUser(request);
        call.enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    TokenModel tokenResponse = response.body();
                    if (tokenResponse != null) {
                        saveLoginDetails(tokenResponse);
                        if (checkboxSavePassword.isChecked()) {
                            saveCredentials(login, password);
                            saveCheckboxState(true); // Lưu trạng thái của checkbox
                        } else {
                            clearCredentials();
                            saveCheckboxState(false); // Lưu trạng thái của checkbox
                        }

                        showToastBasedOnLanguage("Đăng nhập thành công", "Login successful");

                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        finish(); // Đóng StartActivity để người dùng không quay lại trang đăng nhập
                    }
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        String errorMessage = errorObject.getString("error");

                        showToastBasedOnLanguage(errorMessage, "Login failed: " + errorMessage);
                    } catch (Exception e) {
                        showToastBasedOnLanguage("Đăng nhập thất bại.", "Login failed.");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                hideLoading();
                showToastBasedOnLanguage("Lỗi kết nối: " + t.getMessage(), "Connection error: " + t.getMessage());
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
