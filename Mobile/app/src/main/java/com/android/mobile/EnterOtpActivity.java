package com.android.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.mobile.models.VerifyOtpModel;
import com.android.mobile.models.ReponseModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.UserApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterOtpActivity extends AppCompatActivity {

    private EditText inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;
    private String email;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);

        email = getIntent().getStringExtra("email");

        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        inputCode5 = findViewById(R.id.inputCode5);
        inputCode6 = findViewById(R.id.inputCode6);

        img_back = findViewById(R.id.img_back);

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

    private void verifyOtp() {
        String otp = inputCode1.getText().toString() + inputCode2.getText().toString() +
                inputCode3.getText().toString() + inputCode4.getText().toString() +
                inputCode5.getText().toString() + inputCode6.getText().toString();

        if (otp.length() != 6) {
            Toast.makeText(this, "Mã OTP phải là 6 chữ số", Toast.LENGTH_SHORT).show();
            return;
        }

        VerifyOtpModel request = new VerifyOtpModel(email, otp);
        UserApiService apiService = ApiServiceProvider.getUserApiService();
        Call<ReponseModel> call = apiService.verifyOtp(request);
        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(EnterOtpActivity.this, "OTP đã được xác thực", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EnterOtpActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("otp", otp);
                    startActivity(intent);
                } else {
                    Toast.makeText(EnterOtpActivity.this, "Xác thực OTP thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                Toast.makeText(EnterOtpActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
