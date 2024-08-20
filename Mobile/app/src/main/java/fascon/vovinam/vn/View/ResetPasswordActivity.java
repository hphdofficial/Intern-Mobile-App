package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.Model.ResetPasswordModel;
import fascon.vovinam.vn.Model.ReponseModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.UserApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends BaseActivity {
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




    private EditText editTextNewPassword;
    private EditText editTextConfirmPassword;
    private Button buttonResetPassword;
    private String email;
    private String otp;
    private boolean isPasswordVisibleNew = false;
    private boolean isPasswordVisibleConfirm = false;
    private ImageView iconPasswordVisibilityNew;
    private ImageView iconPasswordVisibilityConfirm;

    private BlankFragment loadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        email = getIntent().getStringExtra("email");
        otp = getIntent().getStringExtra("otp");

        editTextNewPassword = findViewById(R.id.edit_text_new_password);
        editTextConfirmPassword = findViewById(R.id.edit_text_confirm_password);
        buttonResetPassword = findViewById(R.id.button_reset_password);
        iconPasswordVisibilityNew = findViewById(R.id.iconPasswordVisibilityNew);
        iconPasswordVisibilityConfirm = findViewById(R.id.iconPasswordVisibilityConfirm);

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
        iconPasswordVisibilityNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(editTextNewPassword, iconPasswordVisibilityNew);
            }
        });

        iconPasswordVisibilityConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(editTextConfirmPassword, iconPasswordVisibilityConfirm);
            }
        });

        // Gọi hàm updateLanguageForTextViews sau khi ánh xạ các View
        updateLanguageForTextViews();

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

    private void togglePasswordVisibility(EditText editText, ImageView icon) {
        if (editText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            icon.setImageResource(R.drawable.hide); // Use your hide icon
        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            icon.setImageResource(R.drawable.view); // Use your show icon
        }
        editText.setSelection(editText.getText().length());
    }

    private void showToastBasedOnLanguage(String messageVN, String messageEN) {
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        String languageS = shared.getString("language", null);

        if (languageS != null && languageS.contains("en")) {
            Toast.makeText(ResetPasswordActivity.this, messageEN, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ResetPasswordActivity.this, messageVN, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLanguageForTextViews() {
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        String languageS = shared.getString("language", null);

        if (languageS != null && languageS.contains("en")) {
            // English translations
            buttonResetPassword.setText("Reset Password");
            editTextNewPassword.setHint("New Password");
            editTextConfirmPassword.setHint("Confirm Password");
        } else {
            // Vietnamese translations
            buttonResetPassword.setText("Đặt lại mật khẩu");
            editTextNewPassword.setHint("Mật khẩu mới");
            editTextConfirmPassword.setHint("Xác nhận mật khẩu");
        }
    }



    private void resetPassword() {
        String newPassword = editTextNewPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        if (newPassword.isEmpty() || newPassword.length() < 6) {
            showToastBasedOnLanguage("Mật khẩu phải có ít nhất 6 ký tự", "Password must be at least 6 characters");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showToastBasedOnLanguage("Mật khẩu xác nhận không khớp", "Confirm password does not match");
            return;
        }

        ResetPasswordModel request = new ResetPasswordModel(email, otp, newPassword, confirmPassword);
        UserApiService apiService = ApiServiceProvider.getUserApiService();
        showLoading();
        Call<ReponseModel> call = apiService.resetPassword(request);
        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    showToastBasedOnLanguage("Đặt lại mật khẩu thành công", "Password reset successful");
                    Intent intent = new Intent(ResetPasswordActivity.this, StartActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showToastBasedOnLanguage("Đặt lại mật khẩu thất bại: " + response.message(), "Password reset failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                hideLoading();
                showToastBasedOnLanguage("Lỗi kết nối: " + t.getMessage(), "Connection error: " + t.getMessage());
            }
        });
    }

}
