package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.Model.RegisterModel;
import fascon.vovinam.vn.Model.ReponseModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.UserApiService;

import org.json.JSONObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends BaseActivity {

    private EditText editTextUsername, editTextPassword, editTextEmail, editTextTen, editTextChieucao, editTextCannang, editTextDienthoai, editTextDiachi, editTextNgaysinh, editTextHotenGiamho, editTextDienthoaiGiamho, edit_text_confirm_password;
    private RadioGroup radioGroupGender;
    private Button buttonSignUp;
    private TextView text_view_signup;
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
        setContentView(R.layout.activity_signup);
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language",null);
        editTextUsername = findViewById(R.id.edit_text_username);
        editTextPassword = findViewById(R.id.edit_text_password);

        edit_text_confirm_password = findViewById(R.id.edit_text_confirm_password);

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
        text_view_signup = findViewById(R.id.text_view_signup);

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
        text_view_subtitle = findViewById(R.id.text_view_subtitle);
        radio_button_male = findViewById(R.id.radio_button_male);
        radio_button_female = findViewById(R.id.radio_button_female);
        if(languageS!= null){
            if(languageS.contains("en")){
                text_view_signup.setText("Register");
                text_view_subtitle.setText("Please Enter Your Personal Information");
                editTextEmail.setHint("Enter Email");
                editTextTen.setHint("Enter Name");
                editTextChieucao.setHint("Enter Height");
                editTextCannang.setHint("Enter Weight");
                editTextDienthoai.setHint("Enter Phone");
                editTextDiachi.setHint("Enter Address");
                editTextNgaysinh.setHint("Birthday ((YYYY-MM-DD))");
                editTextHotenGiamho.setHint("Guardian");
                editTextDienthoaiGiamho.setHint("Phone Guardian");
                radio_button_male.setText("Male");
                radio_button_female.setText("Female");
                buttonSignUp.setText("Sign Up");
            }
        }

    }
    private RadioButton radio_button_male;
    private RadioButton radio_button_female;

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
    private TextView text_view_subtitle;


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
        // Lấy giá trị từ các trường nhập liệu
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String ten = editTextTen.getText().toString().trim();
        String dienthoai = editTextDienthoai.getText().toString().trim();
        String diachi = editTextDiachi.getText().toString().trim();
        String ngaysinh = editTextNgaysinh.getText().toString().trim();
        String hoten_giamho = editTextHotenGiamho.getText().toString().trim();
        String dienthoai_giamho = editTextDienthoaiGiamho.getText().toString().trim();
        String confirmPasswordEditText = edit_text_confirm_password.getText().toString().trim();

        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        RadioButton selectedGenderButton = findViewById(selectedGenderId);
        String gioitinh = selectedGenderButton.getText().toString();

        // Kiểm tra username
        if (username.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Vui lòng nhập tên tài khoản.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra email
        if (email.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Vui lòng nhập email.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(SignupActivity.this, "Email không hợp lệ.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu có ít nhất 6 ký tự
        if (password.length() < 6) {
            Toast.makeText(SignupActivity.this, "Mật khẩu phải có ít nhất 6 ký tự.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu xác nhận khớp với mật khẩu
        if (!password.equals(confirmPasswordEditText)) {
            Toast.makeText(SignupActivity.this, "Mật khẩu xác nhận không khớp.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra tên người dùng
        if (ten.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Vui lòng nhập họ và tên.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra số điện thoại
        if (dienthoai.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Vui lòng nhập số điện thoại.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra địa chỉ
        if (diachi.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Vui lòng nhập địa chỉ.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra ngày sinh
        if (ngaysinh.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Vui lòng nhập ngày sinh.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra chiều cao và cân nặng
        int chieucao, cannang;
        try {
            chieucao = Integer.parseInt(editTextChieucao.getText().toString().trim());
            cannang = Integer.parseInt(editTextCannang.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(SignupActivity.this, "Chiều cao và cân nặng phải là số hợp lệ.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra giới tính
        if (gioitinh.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Vui lòng chọn giới tính.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra độ tuổi
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

        // Tạo đối tượng RegisterModel và gửi yêu cầu đăng ký
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
            request.setHoten_giamho(null);
            request.setDienthoai_giamho(null);
        }

        // Hiển thị loading và gọi API đăng ký
        showLoading();
        UserApiService apiService = ApiServiceProvider.getUserApiService();
        Call<ReponseModel> call = apiService.registerUser(request);
        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, StartActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        // Xử lý phản hồi lỗi từ server
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        JSONObject errors = errorObject.getJSONObject("error");

                        if (errors.has("username")) {
                            Toast.makeText(SignupActivity.this, "Tên tài khoản đã tồn tại.", Toast.LENGTH_SHORT).show();
                        } else if (errors.has("email")) {
                            Toast.makeText(SignupActivity.this, "Email đã tồn tại.", Toast.LENGTH_SHORT).show();
                        } else if (errors.has("dienthoai")) {
                            Toast.makeText(SignupActivity.this, "Số điện thoại đã tồn tại.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignupActivity.this, "Đăng ký thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(SignupActivity.this, "Đăng ký thất bại.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                hideLoading();
                Toast.makeText(SignupActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
