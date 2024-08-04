package com.android.mobile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.models.RegisterModel;
import com.android.mobile.models.ReponseModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.UserApiService;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends BaseActivity {

    private EditText editTextUsername, editTextPassword, editTextEmail, editTextTen, editTextChieucao, editTextCannang, editTextDienthoai, editTextDiachi, editTextNgaysinh, editTextHotenGiamho, editTextDienthoaiGiamho;
    private RadioGroup radioGroupGender;
    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextUsername = findViewById(R.id.edit_text_username);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextTen = findViewById(R.id.edit_text_ten);
        editTextChieucao = findViewById(R.id.edit_text_chieucao);
        editTextCannang = findViewById(R.id.edit_text_cannang);
        editTextDienthoai = findViewById(R.id.edit_text_dienthoai);
        editTextDiachi = findViewById(R.id.edit_text_diachi);
        editTextNgaysinh = findViewById(R.id.edit_text_ngaysinh);
        editTextHotenGiamho = findViewById(R.id.edit_text_hoten_giamho);
        editTextDienthoaiGiamho = findViewById(R.id.edit_text_dienthoai_giamho);
        radioGroupGender = findViewById(R.id.radio_group_gender);
        buttonSignUp = findViewById(R.id.button_sign_up);

        editTextNgaysinh.setInputType(InputType.TYPE_NULL); // Disable keyboard input
        editTextNgaysinh.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    SignupActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                        editTextNgaysinh.setText(formattedDate);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        buttonSignUp.setOnClickListener(v -> registerUser());

        // Handle password visibility toggle
        ImageButton togglePasswordVisibility = findViewById(R.id.image_button_toggle_password_visibility);
        ImageButton toggleConfirmPasswordVisibility = findViewById(R.id.image_button_toggle_confirm_password_visibility);
        EditText passwordEditText = findViewById(R.id.edit_text_password);
        EditText confirmPasswordEditText = findViewById(R.id.edit_text_confirm_password);

        togglePasswordVisibility.setOnClickListener(v -> {
            togglePasswordVisibility(passwordEditText, togglePasswordVisibility);
        });

        toggleConfirmPasswordVisibility.setOnClickListener(v -> {
            togglePasswordVisibility(confirmPasswordEditText, toggleConfirmPasswordVisibility);
        });
    }

    private void togglePasswordVisibility(EditText editText, ImageButton toggleButton) {
        if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            toggleButton.setImageResource(R.drawable.view); // Change to your view icon
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            toggleButton.setImageResource(R.drawable.hide); // Change back to your hide icon
        }
        editText.setSelection(editText.getText().length());
    }

    private void registerUser() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String email = editTextEmail.getText().toString();
        String ten = editTextTen.getText().toString();
        int chieucao = Integer.parseInt(editTextChieucao.getText().toString());
        int cannang = Integer.parseInt(editTextCannang.getText().toString());
        String dienthoai = editTextDienthoai.getText().toString();
        String diachi = editTextDiachi.getText().toString();
        String ngaysinh = editTextNgaysinh.getText().toString();
        String hoten_giamho = editTextHotenGiamho.getText().toString();
        String dienthoai_giamho = editTextDienthoaiGiamho.getText().toString();

        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        RadioButton selectedGenderButton = findViewById(selectedGenderId);
        String gioitinh = selectedGenderButton.getText().toString();

        // Check if user is under 18
        Calendar birthDate = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        String[] dateParts = ngaysinh.split("-");
        birthDate.set(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]) - 1, Integer.parseInt(dateParts[2]));
        int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        if (age < 18 && (hoten_giamho.isEmpty() || dienthoai_giamho.isEmpty())) {
            Toast.makeText(SignupActivity.this, "Yêu cầu họ tên và số điện thoại phụ huynh cho trẻ dưới 18.", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterModel request = new RegisterModel();
        request.setUsername(username);
        request.setPassword(password);
        request.setEmail(email);
        request.setTen(ten);
        request.setChieucao(chieucao);
        request.setCannang(cannang);
        request.setDienthoai(dienthoai);
        request.setDiachi(diachi);
        request.setGioitinh(gioitinh);
        request.setNgaysinh(ngaysinh);
        if (age < 18) {
            request.setHoten_giamho(hoten_giamho);
            request.setDienthoai_giamho(dienthoai_giamho);
        } else {
            request.setHoten_giamho(null); // explicitly setting these to null to avoid sending empty strings
            request.setDienthoai_giamho(null);
        }

        UserApiService apiService = ApiServiceProvider.getUserApiService();
        Call<ReponseModel> call = apiService.registerUser(request);
        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this,StartActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignupActivity.this, "Đăng ký thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                Toast.makeText(SignupActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
