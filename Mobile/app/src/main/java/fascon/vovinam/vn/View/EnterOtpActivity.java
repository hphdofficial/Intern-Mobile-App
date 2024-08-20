package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.Model.ForgotPasswordModel;
import fascon.vovinam.vn.Model.VerifyOtpModel;
import fascon.vovinam.vn.Model.ReponseModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.UserApiService;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterOtpActivity extends BaseActivity {

    private EditText inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;
    private String email;
    private ImageView img_back;

    private TextView tvResendOTP;
    private String languageS;
    private BlankFragment loadingFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language",null);

        email = getIntent().getStringExtra("email");

        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        inputCode5 = findViewById(R.id.inputCode5);
        inputCode6 = findViewById(R.id.inputCode6);

        img_back = findViewById(R.id.img_back);

        tvResendOTP = findViewById(R.id.tvResendOTP);

        setupEditTextListeners();

        findViewById(R.id.btnVerify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp();
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterOtpActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtp();
            }
        });
        // Cập nhật ngôn ngữ cho TextViews
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


    private void setupEditTextListeners() {
        inputCode1.addTextChangedListener(new FocusTextWatcher(inputCode1, inputCode2));
        inputCode2.addTextChangedListener(new FocusTextWatcher(inputCode2, inputCode3));
        inputCode3.addTextChangedListener(new FocusTextWatcher(inputCode3, inputCode4));
        inputCode4.addTextChangedListener(new FocusTextWatcher(inputCode4, inputCode5));
        inputCode5.addTextChangedListener(new FocusTextWatcher(inputCode5, inputCode6));
        inputCode6.addTextChangedListener(new FocusTextWatcher(inputCode6, null));
    }

    private static class FocusTextWatcher implements TextWatcher {
        private final EditText currentEditText;
        private final EditText nextEditText;

        FocusTextWatcher(EditText currentEditText, EditText nextEditText) {
            this.currentEditText = currentEditText;
            this.nextEditText = nextEditText;
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 1 && nextEditText != null) {
                nextEditText.requestFocus();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    private void resetOtpInputs() {
        inputCode1.setText("");
        inputCode2.setText("");
        inputCode3.setText("");
        inputCode4.setText("");
        inputCode5.setText("");
        inputCode6.setText("");
        inputCode1.requestFocus();
    }

    private void showToastBasedOnLanguage(String messageVN, String messageEN) {
        if (languageS != null && languageS.contains("en")) {
            Toast.makeText(EnterOtpActivity.this, messageEN, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(EnterOtpActivity.this, messageVN, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLanguageForTextViews() {
        tv_enter_otp = findViewById(R.id.tv_enter_otp);
        tv_notification = findViewById(R.id.tv_notification);
        tv_phone_number = findViewById(R.id.tv_phone_number);
        text11 = findViewById(R.id.text11);
        btnVerify = findViewById(R.id.btnVerify);

        if (languageS != null && languageS.contains("en")) {
            tv_notification.setText("The 6-digit code has been sent");
            tv_enter_otp.setText("Enter code OTP");
            tv_phone_number.setText("Sent to your email!");
            text11.setText("Didn't receive OTP?");
            tvResendOTP.setText("Resend code");
            btnVerify.setText("Confirm");
        } else {
            tv_notification.setText("Mã 6 chữ số đã được gửi");
            tv_enter_otp.setText("Nhập mã OTP");
            tv_phone_number.setText("Đã gửi đến email của bạn!");
            text11.setText("Chưa nhận được OTP?");
            tvResendOTP.setText("Gửi lại mã");
            btnVerify.setText("Xác nhận");
        }
    }




    private void verifyOtp() {
        // Lấy OTP từ các trường nhập liệu
        String otp = inputCode1.getText().toString() + inputCode2.getText().toString() +
                inputCode3.getText().toString() + inputCode4.getText().toString() +
                inputCode5.getText().toString() + inputCode6.getText().toString();

        // Kiểm tra độ dài của OTP
        if (otp.length() != 6) {
            showToastBasedOnLanguage("Mã OTP phải là 6 chữ số", "OTP must be 6 digits");
            return;
        }

        // Tạo yêu cầu xác thực OTP
        VerifyOtpModel request = new VerifyOtpModel(email, otp);
        UserApiService apiService = ApiServiceProvider.getUserApiService();
        showLoading(); // Hiển thị loading trước khi thực hiện gọi API

        Call<ReponseModel> call = apiService.verifyOtp(request);
        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    // OTP xác thực thành công, chuyển sang ResetPasswordActivity
                    showToastBasedOnLanguage("OTP đã được xác thực", "OTP has been verified");
                    Intent intent = new Intent(EnterOtpActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("otp", otp);
                    startActivity(intent);
                } else {
                    try {
                        // Xử lý lỗi từ server
                        String errorMessage = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorMessage);
                        String error = errorObject.optString("error");

                        if (response.code() == 400 && error.equals("Không đúng hoặc hết thời gian OTP.")) {
                            showToastBasedOnLanguage("Mã OTP không chính xác hoặc đã hết hạn.", "OTP is incorrect or has expired.");
                        } else {
                            showToastBasedOnLanguage("Xác thực OTP thất bại: " + error, "OTP verification failed: " + error);
                        }
                    } catch (Exception e) {
                        showToastBasedOnLanguage("Xác thực OTP thất bại.", "OTP verification failed.");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                hideLoading();
                showToastBasedOnLanguage("Lỗi kết nối: " + t.getMessage(), "Connection error: " + t.getMessage());
            }
        });
    }


    private Button btnVerify;
    private TextView text11;
    private TextView tv_phone_number;
    private TextView tv_notification;
    private TextView tv_enter_otp;


    private void resendOtp() {
        ForgotPasswordModel request = new ForgotPasswordModel(email);
        UserApiService apiService = ApiServiceProvider.getUserApiService();
        showLoading();
        Call<ReponseModel> call = apiService.sendOtp(request);
        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                hideLoading();
                resetOtpInputs();
                if (response.isSuccessful()) {
                    showToastBasedOnLanguage("OTP đã được gửi lại đến email của bạn", "OTP has been resent to your email");
                } else {
                    showToastBasedOnLanguage("Gửi lại OTP thất bại: " + response.message(), "Failed to resend OTP: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                hideLoading();
                resetOtpInputs();
                if (t instanceof IOException) {
                    showToastBasedOnLanguage("Email không tồn tại trong hệ thống", "Email does not exist in the system");
                } else {
                    showToastBasedOnLanguage("Lỗi kết nối: " + t.getMessage(), "Connection error: " + t.getMessage());
                }
            }
        });
    }


}
