package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.content.Context;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.Model.ReponseModel;
import fascon.vovinam.vn.Model.UpdateInfoModel;
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

public class UpdateInfoMember extends BaseActivity {
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
    private EditText editTextUsername, editTextEmail, editTextTen, editTextDienthoai, editTextDiachi, editTextNgaysinh, editTextHotengiamho, editTextDienthoaigiamho, editTextChieucao, editTextCannang;
    private TextView textViewUsername, textViewEmail, textViewTen, textViewDienthoai, textViewDiachi, text_view_gioitinh, textViewNgaysinh, textViewHotengiamho, textViewDienthoaigiamho, textViewChieucao, textViewCannang;

    private RadioGroup radioGroupGioitinh;
    private RadioButton radioNam, radioNu;
    private Button buttonUpdateInfo;
    private SharedPreferences sharedPreferences;
    private static final String NAME_SHARED = "login_prefs";

    private Calendar calendar;

    private BlankFragment loadingFragment;
    private String languageS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info_member);
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language", "vn");
        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();

        myContentE.putString("title", "Chỉnh sửa thông tin");
        myContentE.apply();
        if(languageS!= null){
            if(languageS.contains("en")){
                myContentE.putString("title", "Edit Information");
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
//        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();

        textViewUsername = findViewById(R.id.text_view_username);
        textViewEmail = findViewById(R.id.text_view_email);
        textViewTen = findViewById(R.id.text_view_ten);
        textViewDienthoai = findViewById(R.id.text_view_dienthoai);
        textViewDiachi = findViewById(R.id.text_view_diachi);
        text_view_gioitinh = findViewById(R.id.text_view_gioitinh);
        textViewNgaysinh = findViewById(R.id.text_view_ngaysinh);
        textViewHotengiamho = findViewById(R.id.text_view_hotengiamho);
        textViewDienthoaigiamho = findViewById(R.id.text_view_dienthoaigiamho);
        textViewChieucao = findViewById(R.id.text_view_chieucao);
        textViewCannang = findViewById(R.id.text_view_cannang);

        // Khởi tạo các view
        editTextUsername = findViewById(R.id.edit_text_username);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextTen = findViewById(R.id.edit_text_ten);
        editTextDienthoai = findViewById(R.id.edit_text_dienthoai);
        editTextDiachi = findViewById(R.id.edit_text_diachi);
        editTextNgaysinh = findViewById(R.id.edit_text_ngaysinh);
        editTextHotengiamho = findViewById(R.id.edit_text_hotengiamho);
        editTextDienthoaigiamho = findViewById(R.id.edit_text_dienthoaigiamho);
        editTextChieucao = findViewById(R.id.edit_text_chieucao);
        editTextCannang = findViewById(R.id.edit_text_cannang);


        radioGroupGioitinh = findViewById(R.id.radio_group_gioitinh);
        radioNam = findViewById(R.id.radio_nam);
        radioNu = findViewById(R.id.radio_nu);
        buttonUpdateInfo = findViewById(R.id.button_update_info);

        // Nhận dữ liệu từ ActivityDetailMember
        Intent intent = getIntent();
        editTextUsername.setText(intent.getStringExtra("username"));
        editTextEmail.setText(intent.getStringExtra("email"));
        editTextTen.setText(intent.getStringExtra("ten"));
        editTextDienthoai.setText(intent.getStringExtra("dienthoai"));

        // Disable fields that should not be edited
        editTextUsername.setEnabled(false);  // Không cho chỉnh sửa username
        editTextEmail.setEnabled(false);     // Không cho chỉnh sửa email
        editTextDienthoai.setEnabled(false); // Không cho chỉnh sửa số điện thoại

        editTextDiachi.setText(intent.getStringExtra("diachi"));

        // Chuyển đổi ngày sinh từ yyyy-MM-dd sang dd/MM/yyyy để hiển thị
        String ngaysinh = intent.getStringExtra("ngaysinh");
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = originalFormat.parse(ngaysinh);
            String formattedDate = displayFormat.format(date);
            editTextNgaysinh.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            editTextNgaysinh.setText(ngaysinh); // Hiển thị như ban đầu nếu có lỗi
        }
        // Khởi tạo lịch
        calendar = Calendar.getInstance();
        editTextNgaysinh.setInputType(InputType.TYPE_NULL);
        editTextNgaysinh.setFocusable(false);
        // Thiết lập sự kiện bấm vào trường ngày sinh để hiển thị DatePickerDialog
        editTextNgaysinh.setOnClickListener(v -> showDatePicker());


        editTextHotengiamho.setText(intent.getStringExtra("hotengiamho"));
        editTextDienthoaigiamho.setText(intent.getStringExtra("dienthoaigiamho"));
        editTextChieucao.setText(intent.getStringExtra("chieucao"));
        editTextCannang.setText(intent.getStringExtra("cannang"));
        // Xử lý giới tính
        String gioitinh = intent.getStringExtra("gioitinh");
        if (gioitinh != null) {
            if (gioitinh.equals("Nam") || gioitinh.equals("Male")) {
                radioNam.setChecked(true);
            } else if (gioitinh.equals("Nữ") || gioitinh.equals("Female")) {
                radioNu.setChecked(true);
            }
        }
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(NAME_SHARED, MODE_PRIVATE);

        // Cập nhật ngôn ngữ cho các TextView và Button
        updateLanguageUI();
        buttonUpdateInfo.setOnClickListener(v -> updateInfo());
    }

    private void updateLanguageUI() {
        if (languageS.equals("en")) {
            textViewUsername.setText("Login name");
            textViewEmail.setText("Email");
            textViewTen.setText("Full Name");
            textViewDienthoai.setText("Phone");
            textViewDiachi.setText("Address");
            text_view_gioitinh.setText("Gender");
            textViewNgaysinh.setText("Birthday");
            textViewHotengiamho.setText("Guardian Name");
            textViewDienthoaigiamho.setText("Guardian Phone");
            textViewChieucao.setText("Height (m)");
            textViewCannang.setText("Weight (kg)");
            radioNam.setText("Male");
            radioNu.setText("Female");
            buttonUpdateInfo.setText("Update Information");
        } else {
            textViewUsername.setText("Tên đăng nhập");
            textViewEmail.setText("Email");
            textViewTen.setText("Họ và Tên");
            textViewDienthoai.setText("Điện thoại");
            textViewDiachi.setText("Địa chỉ");
            text_view_gioitinh.setText("Giới tính");
            textViewNgaysinh.setText("Ngày sinh");
            textViewHotengiamho.setText("Họ tên giám hộ");
            textViewDienthoaigiamho.setText("Điện thoại giám hộ");
            textViewChieucao.setText("Chiều cao (m)");
            textViewCannang.setText("Cân nặng (kg)");
            radioNam.setText("Nam");
            radioNu.setText("Nữ");
            buttonUpdateInfo.setText("Cập nhật thông tin");
        }
    }
    private void showDatePicker() {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Cập nhật Calendar với ngày đã chọn
                    calendar.set(Calendar.YEAR, selectedYear);
                    calendar.set(Calendar.MONTH, selectedMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, selectedDay);

                    // Hiển thị ngày đã chọn trong EditText với định dạng dd/MM/yyyy
                    SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    editTextNgaysinh.setText(displayFormat.format(calendar.getTime()));
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private boolean isValidDateFormat(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); // Đảm bảo không chấp nhận các ngày không hợp lệ như 2024-02-30
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void updateInfo() {
        String token = sharedPreferences.getString("access_token", null);
        if (token != null) {
            if (isValidInput()) {
                String displayedNgaysinh = editTextNgaysinh.getText().toString();

                // Chuyển đổi ngày sinh từ dd/MM/yyyy về yyyy-MM-dd để gửi lên server
                String ngaysinh = displayedNgaysinh;
                try {
                    SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date date = displayFormat.parse(displayedNgaysinh);
                    ngaysinh = serverFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    if (languageS.equals("en")) {
                        Toast.makeText(this, "Invalid birthdate format", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Định dạng ngày sinh không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

                // Tính toán tuổi
                int age = getAgeFromBirthdate(displayedNgaysinh);

                // Kiểm tra thông tin giám hộ cho trẻ dưới 18
                if (age < 18) {
                    if (TextUtils.isEmpty(editTextHotengiamho.getText().toString()) ||
                            TextUtils.isEmpty(editTextDienthoaigiamho.getText().toString())) {
                        if (languageS.equals("en")) {
                            Toast.makeText(this, "Guardian's name and phone are required for children under 18.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Yêu cầu họ tên và số điện thoại phụ huynh cho trẻ dưới 18.", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                }

                // Gửi yêu cầu cập nhật thông tin
                UpdateInfoModel updateInfoModel = new UpdateInfoModel();
                updateInfoModel.setUsername(editTextUsername.getText().toString());
                updateInfoModel.setEmail(editTextEmail.getText().toString());
                updateInfoModel.setTen(editTextTen.getText().toString());
                updateInfoModel.setDienthoai(editTextDienthoai.getText().toString());
                updateInfoModel.setDiachi(editTextDiachi.getText().toString());
                updateInfoModel.setNgaysinh(ngaysinh);
                updateInfoModel.setChieucao(editTextChieucao.getText().toString());
                updateInfoModel.setCannang(editTextCannang.getText().toString());

                // Thêm thông tin giám hộ nếu cần
                if (age < 18) {
                    updateInfoModel.setHoten_giamho(editTextHotengiamho.getText().toString());
                    updateInfoModel.setDienthoai_giamho(editTextDienthoaigiamho.getText().toString());
                }

                int selectedGioitinhId = radioGroupGioitinh.getCheckedRadioButtonId();
                if (selectedGioitinhId == R.id.radio_nam) {
                    updateInfoModel.setGioitinh("Nam");
                } else if (selectedGioitinhId == R.id.radio_nu) {
                    updateInfoModel.setGioitinh("Nữ");
                }

                // Gửi yêu cầu lên server
                UserApiService apiService = ApiServiceProvider.getUserApiService();
                showLoading();
                Call<ReponseModel> call = apiService.updateInfo("Bearer " + token, updateInfoModel);
                call.enqueue(new Callback<ReponseModel>() {
                    @Override
                    public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                        hideLoading();
                        if (response.isSuccessful() && response.body() != null) {
                            if (languageS.equals("en")) {
                                Toast.makeText(UpdateInfoMember.this, "Information updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UpdateInfoMember.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                            }

                            // Tạo Intent để quay lại ActivityDetailMember và gửi thông tin cập nhật
                            Intent intent = new Intent(UpdateInfoMember.this, ActivityDetailMember.class);
                            intent.putExtra("ngaysinh", editTextNgaysinh.getText().toString());
                            intent.putExtra("hotengiamho", editTextHotengiamho.getText().toString());
                            intent.putExtra("dienthoai_giamho", editTextDienthoaigiamho.getText().toString());

                            // Đặt flag để xóa các activity trước đó và bắt đầu activity mới
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            // Kết thúc activity hiện tại để quay lại ActivityDetailMember
                            finish();
                        } else {
                            handleErrorResponse(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ReponseModel> call, Throwable t) {
                        hideLoading();
                        if (languageS.equals("en")) {
                            Toast.makeText(UpdateInfoMember.this, "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UpdateInfoMember.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else {
            if (languageS.equals("en")) {
                Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void showLocalizedToast(String vietnameseMessage, String englishMessage) {
        if (languageS != null && languageS.contains("en")) {
            Toast.makeText(this, englishMessage, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, vietnameseMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void handleErrorResponse(Response<ReponseModel> response) {
        try {
            String errorMessage = response.errorBody().string();
            JSONObject errorObject = new JSONObject(errorMessage);

            // Kiểm tra lỗi trả về từ server
            if (errorObject.has("error")) {
                JSONObject errors = errorObject.getJSONObject("error");

                if (errors.has("ngaysinh")) {
                    showLocalizedToast("Ngày sinh phải là ngày trước hôm nay.", "Birthdate must be before today.");
                } else if (errors.has("username")) {
                    showLocalizedToast("Tên tài khoản đã tồn tại.", "Username already exists.");
                } else if (errors.has("email")) {
                    showLocalizedToast("Email đã tồn tại.", "Email already exists.");
                } else if (errors.has("dienthoai")) {
                    showLocalizedToast("Số điện thoại đã tồn tại.", "Phone number already exists.");
                } else {
                    showLocalizedToast("Cập nhật thông tin thất bại: " + response.message(), "Failed to update information: " + response.message());
                }
            }
        } catch (Exception e) {
            showLocalizedToast("Cập nhật thông tin thất bại.", "Failed to update information.");
            e.printStackTrace();
        }
    }



    private boolean isValidInput() {
        String hoten_giamho = editTextHotengiamho.getText().toString().trim();
        String dienthoai_giamho = editTextDienthoaigiamho.getText().toString().trim();
        String ngaysinh = editTextNgaysinh.getText().toString().trim();

        if (TextUtils.isEmpty(editTextTen.getText().toString().trim())) {
            showLocalizedToast("Họ tên không được để trống", "Full name cannot be empty");
            return false;
        }

        if (TextUtils.isEmpty(editTextUsername.getText().toString().trim())) {
            showLocalizedToast("Tên đăng nhập không được để trống", "Username cannot be empty");
            return false;
        }

        if (TextUtils.isEmpty(editTextEmail.getText().toString().trim())) {
            showLocalizedToast("Email không được để trống", "Email cannot be empty");
            return false;
        }

        if (TextUtils.isEmpty(editTextDiachi.getText().toString().trim())) {
            showLocalizedToast("Địa chỉ không được để trống", "Address cannot be empty");
            return false;
        }

        String dienthoai = editTextDienthoai.getText().toString().trim();
        if (TextUtils.isEmpty(dienthoai)) {
            showLocalizedToast("Điện thoại không được để trống", "Phone number cannot be empty");
            return false;
        } else if (dienthoai.length() < 10 || dienthoai.length() > 11) {
            showLocalizedToast("Số điện thoại phải có từ 10 đến 11 chữ số", "Phone number must be between 10 to 11 digits");
            return false;
        }

        String chieucaoText = editTextChieucao.getText().toString().trim();
        if (TextUtils.isEmpty(chieucaoText)) {
            showLocalizedToast("Chiều cao không được để trống", "Height cannot be empty");
            return false;
        }
        try {
            float chieucao = Float.parseFloat(chieucaoText);
            if (chieucao < 0.5 || chieucao > 4.0) {
                showLocalizedToast("Chiều cao không hợp lệ (nhập giá trị từ 0.5 đến 4.0 mét)", "Invalid height (enter values between 0.5 and 4.0 meters)");
                return false;
            }
        } catch (NumberFormatException e) {
            showLocalizedToast("Chiều cao phải là một số thập phân hợp lệ", "Height must be a valid decimal number");
            return false;
        }

        String cannangText = editTextCannang.getText().toString().trim();
        if (TextUtils.isEmpty(cannangText)) {
            showLocalizedToast("Cân nặng không được để trống", "Weight cannot be empty");
            return false;
        }
        try {
            float cannang = Float.parseFloat(cannangText);
            if (cannang < 10 || cannang > 400) {
                showLocalizedToast("Cân nặng không hợp lệ (nhập giá trị từ 10 kg đến 400 kg)", "Invalid weight (enter values between 10 kg and 400 kg)");
                return false;
            }
        } catch (NumberFormatException e) {
            showLocalizedToast("Cân nặng phải là một số hợp lệ", "Weight must be a valid number");
            return false;
        }

        int age = getAgeFromBirthdate(ngaysinh);
        Log.d("Age", "Calculated age: " + age);
        if (age < 18) {
            if (hoten_giamho.isEmpty() || dienthoai_giamho.isEmpty()) {
                showLocalizedToast("Yêu cầu họ tên và số điện thoại phụ huynh cho trẻ dưới 18.", "Guardian's name and phone are required for children under 18.");
                return false;
            }

            if (dienthoai_giamho.length() < 10 || dienthoai_giamho.length() > 11) {
                showLocalizedToast("Số điện thoại giám hộ phải có từ 10 đến 11 chữ số", "Guardian's phone number must be between 10 to 11 digits");
                return false;
            }
        } else {
            editTextHotengiamho.setText("");
            editTextDienthoaigiamho.setText("");
        }

        return true;
    }



    private int getAgeFromBirthdate(String birthdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = sdf.parse(birthdate);
            Calendar dob = Calendar.getInstance();
            dob.setTime(date);
            Calendar today = Calendar.getInstance();
            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            return age;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
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