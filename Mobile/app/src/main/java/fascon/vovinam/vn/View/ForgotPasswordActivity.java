package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.Model.ForgotPasswordModel;
import fascon.vovinam.vn.Model.ReponseModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.UserApiService;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivity {

    private EditText editTextEmail;
    private Button buttonSendOtp;
    private ImageView img_back;

    private BlankFragment loadingFragment;
    private String languageS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language",null);
        editTextEmail = findViewById(R.id.edit_text_email);
        buttonSendOtp = findViewById(R.id.button_send_otp);
        img_back = findViewById(R.id.img_back);

        buttonSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtp();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
        if(languageS != null){
            if(languageS.contains("en")){
                buttonSendOtp.setText("Send OTP");
            }
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

    private void showToastBasedOnLanguage(String messageVN, String messageEN) {
        if (languageS != null && languageS.contains("en")) {
            Toast.makeText(ForgotPasswordActivity.this, messageEN, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ForgotPasswordActivity.this, messageVN, Toast.LENGTH_SHORT).show();
        }
    }

    private void sendOtp() {
        String email = editTextEmail.getText().toString().trim();

        if (email.isEmpty()) {
            showToastBasedOnLanguage("Vui lòng nhập email", "Please enter your email");
            return;
        }

        ForgotPasswordModel request = new ForgotPasswordModel(email);
        UserApiService apiService = ApiServiceProvider.getUserApiService();
        showLoading();
        Call<ReponseModel> call = apiService.sendOtp(request);
        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    showToastBasedOnLanguage("OTP đã được gửi đến email của bạn", "OTP has been sent to your email");
                    startActivity(new Intent(ForgotPasswordActivity.this, EnterOtpActivity.class).putExtra("email", email));
                } else {
                    try {
                        // Xử lý phản hồi lỗi từ server
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        JSONObject errors = errorObject.getJSONObject("errors");

                        if (errors.has("email")) {
                            showToastBasedOnLanguage("Email không hợp lệ hoặc không tồn tại.", "Invalid or non-existent email.");
                        } else {
                            showToastBasedOnLanguage("Gửi OTP thất bại: " + response.message(), "Failed to send OTP: " + response.message());
                        }
                    } catch (Exception e) {
                        showToastBasedOnLanguage("Gửi OTP thất bại.", "Failed to send OTP.");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                hideLoading(); // Ẩn loading khi có lỗi xảy ra
                if (t instanceof IOException) {
                    showToastBasedOnLanguage("Email không tồn tại trong hệ thống", "Email does not exist in the system");
                } else {
                    showToastBasedOnLanguage("Lỗi kết nối: " + t.getMessage(), "Connection error: " + t.getMessage());
                }
            }
        });
    }


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

}
