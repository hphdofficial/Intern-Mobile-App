package com.android.mobile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.models.ReponseModel;
import com.android.mobile.models.UpdatePasswordModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.UserApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePassword extends BaseActivity {

    private EditText editTextEmail, editTextCurrentPassword, editTextPassword, editTextPasswordConfirmation;
    private Button buttonUpdatePassword;

    private ImageButton buttonToggleCurrentPasswordVisibility, buttonToggleNewPasswordVisibility, buttonTogglePasswordConfirmationVisibility;
    private SharedPreferences sharedPreferences;
    private static final String NAME_SHARED = "login_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        // chèn fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        // Khởi tạo các view
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextCurrentPassword = findViewById(R.id.edit_text_current_password);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextPasswordConfirmation = findViewById(R.id.edit_text_password_confirmation);
        buttonUpdatePassword = findViewById(R.id.button_update_info);

        buttonToggleCurrentPasswordVisibility = findViewById(R.id.button_toggle_current_password_visibility);
        buttonToggleNewPasswordVisibility = findViewById(R.id.button_toggle_new_password_visibility);
        buttonTogglePasswordConfirmationVisibility = findViewById(R.id.button_toggle_password_confirmation_visibility);

        // Nhận dữ liệu từ ActivityDetailMember
        Intent intent = getIntent();
        editTextEmail.setText(intent.getStringExtra("email"));

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(NAME_SHARED, MODE_PRIVATE);

        buttonUpdatePassword.setOnClickListener(v -> updatePassword());

        buttonToggleCurrentPasswordVisibility.setOnClickListener(v -> togglePasswordVisibility(editTextCurrentPassword, buttonToggleCurrentPasswordVisibility));
        buttonToggleNewPasswordVisibility.setOnClickListener(v -> togglePasswordVisibility(editTextPassword, buttonToggleNewPasswordVisibility));
        buttonTogglePasswordConfirmationVisibility.setOnClickListener(v -> togglePasswordVisibility(editTextPasswordConfirmation, buttonTogglePasswordConfirmationVisibility));
    }

    private void togglePasswordVisibility(EditText editText, ImageButton imageButton) {
        if (editText.getInputType() == (android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageButton.setImageResource(R.drawable.view);
        } else {
            editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageButton.setImageResource(R.drawable.hide);
        }
        editText.setSelection(editText.getText().length());
    }

    private void updatePassword() {
        String currentPassword = editTextCurrentPassword.getText().toString();
        String newPassword = editTextPassword.getText().toString();
        String confirmPassword = editTextPasswordConfirmation.getText().toString();

        if (newPassword.length() < 6) {
            Toast.makeText(this, "Mật khẩu mới phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu mới và xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = sharedPreferences.getString("access_token", null);
        if (token != null) {
            UpdatePasswordModel updatePasswordModel = new UpdatePasswordModel();
            updatePasswordModel.setEmail(editTextEmail.getText().toString());
            updatePasswordModel.setCurrent_pass(currentPassword);
            updatePasswordModel.setNew_pass(newPassword);
            updatePasswordModel.setNew_pass_confirmation(confirmPassword);

            UserApiService apiService = ApiServiceProvider.getUserApiService();
            Call<ReponseModel> call = apiService.updatePassword("Bearer " + token, updatePasswordModel);
            call.enqueue(new Callback<ReponseModel>() {
                @Override
                public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(UpdatePassword.this, "Cập nhật mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdatePassword.this, MenuActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(UpdatePassword.this, "Cập nhật mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ReponseModel> call, Throwable t) {
                    Toast.makeText(UpdatePassword.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }
}
