package com.android.mobile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.content.Intent;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.models.ReponseModel;
import com.android.mobile.models.UpdateInfoModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.UserApiService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateInfoMember extends BaseActivity {

    private EditText editTextUsername, editTextEmail, editTextTen, editTextDienthoai, editTextDiachi, editTextNgaysinh, editTextHotengiamho, editTextDienthoaigiamho, editTextChieucao, editTextCannang;
    private RadioGroup radioGroupGioitinh;
    private RadioButton radioNam, radioNu;
    private Button buttonUpdateInfo;
    private SharedPreferences sharedPreferences;
    private static final String NAME_SHARED = "login_prefs";

    private BlankFragment loadingFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info_member);

        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Chỉnh sửa thông tin");
        myContentE.apply();

        // chèn fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
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
    }

    private void updateInfo() {
        String token = sharedPreferences.getString("access_token", null);
        if (token != null) {
            if (isValidInput()) {
                UpdateInfoModel updateInfoModel = new UpdateInfoModel();
                updateInfoModel.setUsername(editTextUsername.getText().toString());
                updateInfoModel.setEmail(editTextEmail.getText().toString());
                updateInfoModel.setTen(editTextTen.getText().toString());
                updateInfoModel.setDienthoai(editTextDienthoai.getText().toString());
                updateInfoModel.setDiachi(editTextDiachi.getText().toString());
                updateInfoModel.setNgaysinh(editTextNgaysinh.getText().toString());
                updateInfoModel.setChieucao(editTextChieucao.getText().toString());
                updateInfoModel.setCannang(editTextCannang.getText().toString());
                int selectedGioitinhId = radioGroupGioitinh.getCheckedRadioButtonId();
                if (selectedGioitinhId == R.id.radio_nam) {
                    updateInfoModel.setGioitinh("Nam");
                } else if (selectedGioitinhId == R.id.radio_nu) {
                    updateInfoModel.setGioitinh("Nữ");
                }

                int age = getAgeFromBirthdate(editTextNgaysinh.getText().toString());
                if (age < 18) {
                    updateInfoModel.setHoten_giamho(editTextHotengiamho.getText().toString());
                    updateInfoModel.setDienthoai_giamho(editTextDienthoaigiamho.getText().toString());
                }

                // In log các giá trị
                Log.d("UpdateInfo", "Username: " + updateInfoModel.getUsername());
                Log.d("UpdateInfo", "Email: " + updateInfoModel.getEmail());
                Log.d("UpdateInfo", "Ten: " + updateInfoModel.getTen());
                Log.d("UpdateInfo", "Dienthoai: " + updateInfoModel.getDienthoai());
                Log.d("UpdateInfo", "Diachi: " + updateInfoModel.getDiachi());
                Log.d("UpdateInfo", "Ngaysinh: " + updateInfoModel.getNgaysinh());
                Log.d("UpdateInfo", "Hoten_giamho: " + updateInfoModel.getHoten_giamho());
                Log.d("UpdateInfo", "Dienthoai_giamho: " + updateInfoModel.getDienthoai_giamho());
                Log.d("UpdateInfo", "Chieucao: " + updateInfoModel.getChieucao());
                Log.d("UpdateInfo", "Cannang: " + updateInfoModel.getCannang());
                Log.d("UpdateInfo", "Gioitinh: " + updateInfoModel.getGioitinh());

                UserApiService apiService = ApiServiceProvider.getUserApiService();
                showLoading();
                Call<ReponseModel> call = apiService.updateInfo("Bearer " + token, updateInfoModel);
                call.enqueue(new Callback<ReponseModel>() {
                    @Override
                    public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                        hideLoading();
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(UpdateInfoMember.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                            // Chuyển hướng về ActivityDetailMember và load lại thông tin
                            Intent intent = new Intent(UpdateInfoMember.this, ActivityDetailMember.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(UpdateInfoMember.this, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
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