package com.android.mobile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.mobile.models.ReponseModel;
import com.android.mobile.models.UpdateInfoModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.UserApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateInfo extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextPasswordConfirmation;
    private Button buttonUpdateInfo;
    private SharedPreferences sharedPreferences;
    private static final String NAME_SHARED = "login_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
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
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextPasswordConfirmation = findViewById(R.id.edit_text_password_confirmation);
        buttonUpdateInfo = findViewById(R.id.button_update_info);

        // Nhận dữ liệu từ ActivityDetailMember
        Intent intent = getIntent();
        editTextEmail.setText(intent.getStringExtra("email"));

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(NAME_SHARED, MODE_PRIVATE);

        buttonUpdateInfo.setOnClickListener(v -> updateInfo());
    }

    private void updateInfo() {
        String token = sharedPreferences.getString("access_token", null);
        if (token != null) {
            UpdateInfoModel updateInfoModel = new UpdateInfoModel();
            updateInfoModel.setEmail(editTextEmail.getText().toString());
            updateInfoModel.setPassword(editTextPassword.getText().toString());
            updateInfoModel.setPasswordConfirmation(editTextPasswordConfirmation.getText().toString());

            UserApiService apiService = ApiServiceProvider.getUserApiService();
            Call<ReponseModel> call = apiService.updateInfo("Bearer " + token, updateInfoModel);
            call.enqueue(new Callback<ReponseModel>() {
                @Override
                public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(UpdateInfo.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateInfo.this,MenuActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(UpdateInfo.this, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ReponseModel> call, Throwable t) {
                    Toast.makeText(UpdateInfo.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }
}
