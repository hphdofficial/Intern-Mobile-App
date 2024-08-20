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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
                        // Hiển thị ngày theo định dạng Ngày/Tháng/Năm
                        String displayDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                        editTextNgaysinh.setText(displayDate);

                        // Lưu ngày theo định dạng Năm-Tháng-Ngày để gửi lên server
                        String serverDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                        editTextNgaysinh.setTag(serverDate); // Lưu vào tag của EditText
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
                editTextUsername.setHint("Enter Username");
                editTextTen.setHint("Enter Full Name");
                editTextChieucao.setHint("Enter Height (m)");
                editTextCannang.setHint("Enter Weight (kg)");
                editTextDienthoai.setHint("Enter Phone Number");
                editTextDiachi.setHint("Enter Address");
                editTextNgaysinh.setHint("Birthday (DD-MM-YYYY)");
                editTextPassword.setHint("Enter Password");
                edit_text_confirm_password.setHint("Confirm Password");
                editTextHotenGiamho.setHint("Enter Guardian Name");
                editTextDienthoaiGiamho.setHint("Enter Guardian Phone Number");
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

        // Lấy ngày sinh từ Tag (định dạng yyyy-MM-dd)
        String ngaysinh = editTextNgaysinh.getTag() != null ? editTextNgaysinh.getTag().toString() : "";

        String hoten_giamho = editTextHotenGiamho.getText().toString().trim();
        String dienthoai_giamho = editTextDienthoaiGiamho.getText().toString().trim();
        String confirmPassword = edit_text_confirm_password.getText().toString().trim();

        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        RadioButton selectedGenderButton = findViewById(selectedGenderId);
        String gioitinh = selectedGenderButton.getText().toString();

        // Chuyển đổi giá trị giới tính trước khi gửi lên server
        if (gioitinh.equalsIgnoreCase("Male")) {
            gioitinh = "Nam";
        } else if (gioitinh.equalsIgnoreCase("Female")) {
            gioitinh = "Nữ";
        }

        // Kiểm tra các trường nhập liệu
        if (username.isEmpty()) {
            showLocalizedToast("Vui lòng nhập tên tài khoản.", "Please enter a username.");
            return;
        }

        if (email.isEmpty()) {
            showLocalizedToast("Vui lòng nhập email.", "Please enter an email.");
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showLocalizedToast("Email không hợp lệ.", "Invalid email address.");
            return;
        }

        if (password.length() < 6) {
            showLocalizedToast("Mật khẩu phải có ít nhất 6 ký tự.", "Password must be at least 6 characters long.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showLocalizedToast("Mật khẩu xác nhận không khớp.", "Confirm password does not match.");
            return;
        }

        if (ten.isEmpty()) {
            showLocalizedToast("Vui lòng nhập họ và tên.", "Please enter your full name.");
            return;
        }

        if (dienthoai.isEmpty()) {
            showLocalizedToast("Vui lòng nhập số điện thoại.", "Please enter your phone number.");
            return;
        }

        if (!Patterns.PHONE.matcher(dienthoai).matches()) {
            showLocalizedToast("Số điện thoại không hợp lệ.", "Invalid phone number.");
            return;
        }

        if (dienthoai.length() < 10 || dienthoai.length() > 11) {
            showLocalizedToast("Số điện thoại phải có từ 10 đến 11 chữ số.", "Phone number must be between 10 and 11 digits.");
            return;
        }

        if (diachi.isEmpty()) {
            showLocalizedToast("Vui lòng nhập địa chỉ.", "Please enter your address.");
            return;
        }

        if (ngaysinh.isEmpty()) {
            showLocalizedToast("Vui lòng nhập ngày sinh.", "Please enter your date of birth.");
            return;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date birthDate = dateFormat.parse(ngaysinh);
            if (birthDate.after(new Date())) {
                showLocalizedToast("Ngày sinh phải trước ngày hiện tại.", "Date of birth must be before the current date.");
                return;
            }
        } catch (ParseException e) {
            showLocalizedToast("Ngày sinh không hợp lệ.", "Invalid date of birth.");
            return;
        }

        float chieucao;
        try {
            chieucao = Float.parseFloat(editTextChieucao.getText().toString().trim());
            if (chieucao < 0.5 || chieucao > 4) {
                showLocalizedToast("Vui lòng nhập chiều cao hợp lệ trong khoảng 0.5 - 4.0 mét.", "Please enter a valid height between 0.5 and 4.0 meters.");
                return;
            }
        } catch (NumberFormatException e) {
            showLocalizedToast("Vui lòng nhập chiều cao hợp lệ (ví dụ: 1.58).", "Please enter a valid height (e.g., 1.58).");
            return;
        }

        int cannang;
        try {
            cannang = Integer.parseInt(editTextCannang.getText().toString().trim());
            if (cannang < 10 || cannang > 400) {
                showLocalizedToast("Vui lòng nhập cân nặng hợp lệ (từ 10kg đến 400kg).", "Please enter a valid weight (between 10kg and 400kg).");
                return;
            }
        } catch (NumberFormatException e) {
            showLocalizedToast("Cân nặng phải là số hợp lệ.", "Weight must be a valid number.");
            return;
        }

        if (gioitinh.isEmpty()) {
            showLocalizedToast("Vui lòng chọn giới tính.", "Please select your gender.");
            return;
        }

        Calendar birthDate = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        String[] dateParts = ngaysinh.split("-");
        birthDate.set(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]) - 1, Integer.parseInt(dateParts[2]));
        int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        if (age < 18) {
            if (hoten_giamho.isEmpty() || dienthoai_giamho.isEmpty()) {
                showLocalizedToast("Yêu cầu họ tên và số điện thoại phụ huynh cho trẻ dưới 18.", "Guardian name and phone are required for children under 18.");
                return;
            }

            if (dienthoai_giamho.length() < 10 || dienthoai_giamho.length() > 11) {
                showLocalizedToast("Số điện thoại giám hộ phải có từ 10 đến 11 chữ số.", "Guardian phone number must be between 10 and 11 digits.");
                return;
            }
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
        request.setGioitinh(gioitinh);  // Sử dụng giá trị đã chuyển đổi
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
                    showLocalizedToast("Đăng ký thành công", "Registration successful");
                    Intent intent = new Intent(SignupActivity.this, StartActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        JSONObject errors = errorObject.getJSONObject("error");

                        if (errors.has("username")) {
                            showLocalizedToast("Tên tài khoản đã tồn tại.", "Username already exists.");
                        } else if (errors.has("email")) {
                            showLocalizedToast("Email đã tồn tại.", "Email already exists.");
                        } else if (errors.has("dienthoai")) {
                            showLocalizedToast("Số điện thoại đã tồn tại.", "Phone number already exists.");
                        } else if (errors.has("ngaysinh")) {
                            showLocalizedToast("Ngày sinh không hợp lệ: " + errors.getJSONArray("ngaysinh").getString(0), "Invalid date of birth: " + errors.getJSONArray("ngaysinh").getString(0));
                        } else {
                            showLocalizedToast("Đăng ký thất bại: " + response.message(), "Registration failed: " + response.message());
                        }
                    } catch (Exception e) {
                        showLocalizedToast("Đăng ký thất bại.", "Registration failed.");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                hideLoading();
                showLocalizedToast("Lỗi kết nối: " + t.getMessage(), "Connection error: " + t.getMessage());
            }
        });
    }


    private void showLocalizedToast(String vietnameseMessage, String englishMessage) {
        if (languageS != null && languageS.contains("en")) {
            Toast.makeText(this, englishMessage, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, vietnameseMessage, Toast.LENGTH_SHORT).show();
        }
    }


}
