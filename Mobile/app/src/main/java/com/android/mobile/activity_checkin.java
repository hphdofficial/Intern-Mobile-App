package com.android.mobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.adapter.Checkin_adapter;
import com.android.mobile.adapter.MyClassAdapter;
import com.android.mobile.models.AttendanceRequest;
import com.android.mobile.models.CheckinMemberModel;
import com.android.mobile.models.ClassModel;
import com.android.mobile.models.ErrorCheckinModel;
import com.android.mobile.models.Member;
import com.android.mobile.models.OptionCategory;
import com.android.mobile.models.ProductModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.CheckinApiService;
import com.android.mobile.services.ProductApiService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_checkin extends BaseActivity {

    private Checkin_adapter checkinAdapter;
    private BlankFragment loadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkin);



        Intent intent = getIntent();
        int idClass = intent.getIntExtra("id", -1);

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        TextView txtDate = findViewById(R.id.txtDate);
        txtDate.setText("Ngày: "+formattedDate);


        loadLanguagePreference();

        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Giao diện điểm danh");
        myContentE.apply();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

// Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();

        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", null);

        fetchMemberForCheckin(idClass,"Bearer "+token);

        Button btnHistory = findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(activity_checkin.this, activity_teacher_checkin.class);
                intent1.putExtra("id", idClass);
                startActivity(intent1);
            }
        });

        Button btnDiemDanh = findViewById(R.id.btnDiemDanh);
        String finalFormattedDate = formattedDate;
        btnDiemDanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading();
                List<CheckinMemberModel> checkinOptions = checkinAdapter.getCheckinList().stream()
                        .filter(CheckinMemberModel::isChecked)
                        .collect(Collectors.toList());

                // Lấy thời gian hiện tại
                LocalTime currentTime = null;

                currentTime = LocalTime.now();
                // Định dạng thời gian theo kiểu 24 giờ
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

                // Chuyển đổi thời gian hiện tại sang chuỗi theo định dạng đã cho
                String formattedTime = currentTime.format(formatter);

                System.out.println("Time: " + formattedTime);


                AttendanceRequest attendanceRequest = new AttendanceRequest();
                attendanceRequest.setId_class(idClass);

                LocalDate currentTodayDate = null;
                String todayDate = "";
                currentTodayDate = LocalDate.now();
                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                todayDate = currentTodayDate.format(formatter1);

                System.out.println("Date: " +todayDate);

                attendanceRequest.setDate(todayDate);

//                attendanceRequest.setDate("2024-07-29");

                List<AttendanceRequest.Attendee> attendees = new ArrayList<>();




                for (CheckinMemberModel checkin : checkinOptions) {
                    AttendanceRequest.Attendee attendee = new AttendanceRequest.Attendee();
                    attendee.setId_atg_member(checkin.getId());
                    System.out.println(checkin.getId());
                    attendee.setIn(formattedTime);
//                    attendee.setIn("18:08");
                    attendee.setOut("20:30");
                    attendees.add(attendee);
                }



                attendanceRequest.setAttendees(attendees);

                CheckinApiService apiService = ApiServiceProvider.getCheckinApiService();
                apiService.teacherCheckin("Bearer "+token, attendanceRequest).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){

                            hideLoading();

                            //Chuyển qua lịch sử điểm danh
                            Intent intent1 = new Intent(activity_checkin.this, activity_teacher_checkin.class);
                            intent1.putExtra("id", idClass);
                            startActivity(intent1);

                            Toast.makeText(activity_checkin.this, "Điểm danh thành công", Toast.LENGTH_SHORT).show();
                        }else {
                            try {
                                // Xử lý phản hồi lỗi từ server
                                JSONObject errorObject = new JSONObject(response.errorBody().string());
                                String errorMessage = errorObject.getString("error");

                                Toast.makeText(activity_checkin.this, errorMessage, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(activity_checkin.this, "Điểm danh thất bại.", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                            hideLoading();

//                            System.out.println(response.errorBody());
//                            Toast.makeText(activity_checkin.this, "Điểm danh thất bại do chưa đúng thời gian điểm danh lớp học", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {
                        hideLoading();
                        System.out.println("Active: Call Onfail");
                        Log.e("PostData", "Failure: " + throwable.getMessage());
                        Toast.makeText(activity_checkin.this, "Điểm danh thất bại do lỗi mạng", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void fetchMemberForCheckin(int idClass, String token){
        showLoading();
        CheckinApiService apiService = ApiServiceProvider.getCheckinApiService();
        apiService.getClassMembers(token, idClass).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject jsonObject = response.body();
                    Gson gson = new Gson();
                    Type checkinMemberListType = new TypeToken<List<CheckinMemberModel>>() {}.getType();
                    List<CheckinMemberModel> checkinMembers = gson.fromJson(jsonObject.get("data"), checkinMemberListType);
                    if(checkinMembers == null){
                        Toast.makeText(activity_checkin.this, "Lớp chưa có thành viên", Toast.LENGTH_SHORT).show();
                        hideLoading();
                    }else{
                        for (CheckinMemberModel memberSample : checkinMembers){
                            Log.e("PostData", "Success: " + memberSample.getTen());
                        }

                        checkinAdapter = new Checkin_adapter(getApplicationContext(), checkinMembers);
                        RecyclerView recyclerView = findViewById(R.id.recycler_checkin);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(checkinAdapter);

                        hideLoading();
                    }

                }else {
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());
                    Toast.makeText(activity_checkin.this, "Lỗi lấy dữ liệu vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                hideLoading();
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + throwable.getMessage());
            }
        });
    }

    private void showLoading() {
        loadingFragment = new BlankFragment();
        loadingFragment.show(getSupportFragmentManager(), "loading");
    }
    private void hideLoading() {
        if (loadingFragment != null) {
            loadingFragment.dismiss();
            loadingFragment = null;
        }
    }

    private void loadLanguagePreference() {
        SharedPreferences preferences = getSharedPreferences("AppSettings", Activity.MODE_PRIVATE);
        String language = preferences.getString("App_Language", "vi"); // mặc định là tiếng Viet
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}