package com.android.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.models.ForgotPasswordModel;
import com.android.mobile.models.ReponseModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.UserApiService;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

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


    private void sendOtp() {
        String email = editTextEmail.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(ForgotPasswordActivity.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ForgotPasswordActivity.this, "OTP đã được gửi đến email của bạn", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotPasswordActivity.this, EnterOtpActivity.class).putExtra("email", email));
                } else {
                    try {
                        // Xử lý phản hồi lỗi từ server
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        JSONObject errors = errorObject.getJSONObject("errors");

                        if (errors.has("email")) {
                            Toast.makeText(ForgotPasswordActivity.this, "Email không hợp lệ hoặc không tồn tại.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "Gửi OTP thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(ForgotPasswordActivity.this, "Gửi OTP thất bại.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                hideLoading(); // Ẩn loading khi có lỗi xảy ra
                if (t instanceof IOException) {
                    Toast.makeText(ForgotPasswordActivity.this, "Email không tồn tại trong hệ thống", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
