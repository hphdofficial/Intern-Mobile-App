package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.content.Context;

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
    private RadioGroup radioGroupGioitinh;
    private RadioButton radioNam, radioNu;
    private Button buttonUpdateInfo;
    private SharedPreferences sharedPreferences;
    private static final String NAME_SHARED = "login_prefs";

    private BlankFragment loadingFragment;
    private String languageS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info_member);
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language",null);
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
        editTextDiachi.setText(intent.getStringExtra("diachi"));
        editTextNgaysinh.setText(intent.getStringExtra("ngaysinh"));
        editTextHotengiamho.setText(intent.getStringExtra("hotengiamho"));
        editTextDienthoaigiamho.setText(intent.getStringExtra("dienthoaigiamho"));
        editTextChieucao.setText(intent.getStringExtra("chieucao"));
        editTextCannang.setText(intent.getStringExtra("cannang"));
        String gioitinh = intent.getStringExtra("gioitinh");
        if (gioitinh != null) {
            if (gioitinh.equals("Nam")) {
                radioNam.setChecked(true);
            } else if (gioitinh.equals("Nữ")) {
                radioNu.setChecked(true);
            }
        }

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(NAME_SHARED, MODE_PRIVATE);

        buttonUpdateInfo.setOnClickListener(v -> updateInfo());

        if(languageS != null){
            if(languageS.contains("en")){

                editTextUsername.setText("Login name");
                editTextEmail.setHint("Enter Email");
                editTextTen.setHint("Enter Name");
                editTextChieucao.setHint("Enter Height");
                editTextCannang.setHint("Enter Weight");
                editTextDienthoai.setHint("Enter Phone");
                editTextDiachi.setHint("Enter Address");
                editTextNgaysinh.setHint("Birthday ((YYYY-MM-DD))");
                editTextHotengiamho.setHint("Guardian");
                editTextDienthoaigiamho.setHint("Phone Guardian");
                radioNam.setText("Male");
                radioNu.setText("Female");
                buttonUpdateInfo.setText("Update Information");
            }
        }
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
                String ngaysinh = editTextNgaysinh.getText().toString();

                // Kiểm tra định dạng ngày sinh
                if (!isValidDateFormat(ngaysinh)) {
                    Toast.makeText(this, "Ngày sinh phải có định dạng Y-m-d, ví dụ: 2024-08-11", Toast.LENGTH_SHORT).show();
                    return;
                }

                UpdateInfoModel updateInfoModel = new UpdateInfoModel();
                updateInfoModel.setUsername(editTextUsername.getText().toString());
                updateInfoModel.setEmail(editTextEmail.getText().toString());
                updateInfoModel.setTen(editTextTen.getText().toString());
                updateInfoModel.setDienthoai(editTextDienthoai.getText().toString());
                updateInfoModel.setDiachi(editTextDiachi.getText().toString());
                updateInfoModel.setNgaysinh(ngaysinh);
                updateInfoModel.setChieucao(editTextChieucao.getText().toString());
                updateInfoModel.setCannang(editTextCannang.getText().toString());
                int selectedGioitinhId = radioGroupGioitinh.getCheckedRadioButtonId();
                if (selectedGioitinhId == R.id.radio_nam) {
                    updateInfoModel.setGioitinh("Nam");
                } else if (selectedGioitinhId == R.id.radio_nu) {
                    updateInfoModel.setGioitinh("Nữ");
                }

                int age = getAgeFromBirthdate(ngaysinh);
                if (age < 18) {
                    updateInfoModel.setHoten_giamho(editTextHotengiamho.getText().toString());
                    updateInfoModel.setDienthoai_giamho(editTextDienthoaigiamho.getText().toString());
                }

                UserApiService apiService = ApiServiceProvider.getUserApiService();
                showLoading();
                Call<ReponseModel> call = apiService.updateInfo("Bearer " + token, updateInfoModel);
                call.enqueue(new Callback<ReponseModel>() {
                    @Override
                    public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                        hideLoading();
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(UpdateInfoMember.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UpdateInfoMember.this, ActivityDetailMember.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            handleErrorResponse(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ReponseModel> call, Throwable t) {
                        hideLoading();
                        Toast.makeText(UpdateInfoMember.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleErrorResponse(Response<ReponseModel> response) {
        try {
            String errorMessage = response.errorBody().string();
            JSONObject errorObject = new JSONObject(errorMessage);
            JSONObject errors = errorObject.getJSONObject("error");

            if (errors.has("username")) {
                Toast.makeText(UpdateInfoMember.this, "Tên tài khoản đã tồn tại.", Toast.LENGTH_SHORT).show();
            } else if (errors.has("email")) {
                Toast.makeText(UpdateInfoMember.this, "Email đã tồn tại.", Toast.LENGTH_SHORT).show();
            } else if (errors.has("dienthoai")) {
                Toast.makeText(UpdateInfoMember.this, "Số điện thoại đã tồn tại.", Toast.LENGTH_SHORT).show();
            } else if (errors.has("ngaysinh")) {
                Toast.makeText(UpdateInfoMember.this, "Ngày sinh phải có định dạng Y-m-d, ví dụ: 2024-08-03", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UpdateInfoMember.this, "Cập nhật thông tin thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(UpdateInfoMember.this, "Cập nhật thông tin thất bại.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }




    private boolean isValidInput() {
        String ngaysinh = editTextNgaysinh.getText().toString();
        if (!TextUtils.isEmpty(ngaysinh)) {
            int age = getAgeFromBirthdate(ngaysinh);
            if (age < 18) {
                if (TextUtils.isEmpty(editTextHotengiamho.getText().toString()) || TextUtils.isEmpty(editTextDienthoaigiamho.getText().toString())) {
                    Toast.makeText(this, "Họ tên giám hộ và điện thoại giám hộ là bắt buộc đối với người dưới 18 tuổi", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    private int getAgeFromBirthdate(String birthdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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