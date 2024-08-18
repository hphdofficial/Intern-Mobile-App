package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.Model.ReponseModel;
import fascon.vovinam.vn.Model.UpdatePasswordModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.UserApiService;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePassword extends BaseActivity {
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
    private EditText editTextEmail, editTextCurrentPassword, editTextPassword, editTextPasswordConfirmation;
    private Button buttonUpdatePassword;

    private ImageButton buttonToggleCurrentPasswordVisibility, buttonToggleNewPasswordVisibility, buttonTogglePasswordConfirmationVisibility;
    private SharedPreferences sharedPreferences;
    private static final String NAME_SHARED = "login_prefs";

    private BlankFragment loadingFragment;
        private String languageS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language",null);
        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Thay đổi mât khẩu");
        myContentE.apply();
        if(languageS!= null){
            if(languageS.contains("en")){
                myContentE.putString("title", "Change Password");
                myContentE.apply();
            }
        }

        // chèn fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
//        fragmentTransaction.addToBackStack(null);
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


        if(languageS != null){
            if(languageS.contains("en")){


                editTextEmail.setHint("Enter Email");
                editTextCurrentPassword.setHint("Current Passoword");
                editTextPassword.setHint("New Password");
                editTextPasswordConfirmation.setHint("Confirm Password");

                buttonUpdatePassword.setText("Update Password");
            }
        }
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
            showLoading();
            Call<ReponseModel> call = apiService.updatePassword("Bearer " + token, updatePasswordModel);
            call.enqueue(new Callback<ReponseModel>() {
                @Override
                public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                    hideLoading();
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(UpdatePassword.this, "Cập nhật mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdatePassword.this, MenuActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            // Xử lý phản hồi lỗi từ server
                            String errorMessage = response.errorBody().string();
                            JSONObject errorObject = new JSONObject(errorMessage);
                            String error = errorObject.optString("error");

                            if (response.code() == 400 && error.equals("Mật khẩu hiện tại không đúng")) {
                                Toast.makeText(UpdatePassword.this, "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UpdatePassword.this, "Cập nhật mật khẩu thất bại: " + error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(UpdatePassword.this, "Cập nhật mật khẩu thất bại.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ReponseModel> call, Throwable t) {
                    hideLoading();
                    Toast.makeText(UpdatePassword.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
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

}
