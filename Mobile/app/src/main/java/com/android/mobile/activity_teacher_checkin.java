package com.android.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
import com.android.mobile.models.AttendanceTeacher;
import com.android.mobile.models.CheckinMemberModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.CheckinApiService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_teacher_checkin extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_checkin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        Intent intent = getIntent();
        int idClass = intent.getIntExtra("id", -1);

        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Giao diện lịch sử điểm danh");
        myContentE.apply();

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

        CheckinApiService apiService = ApiServiceProvider.getCheckinApiService();
        apiService.teacherViewCheckin("Bearer "+token, "2023-11-01", formattedDate).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject jsonObject = response.body();
                    Gson gson = new Gson();

                    JsonObject dataObject = jsonObject.getAsJsonObject("data");
                    Type checkinMemberListType = new TypeToken<Map<String, List<AttendanceTeacher>>>() {}.getType();

                    Map<String, List<AttendanceTeacher>> attendanceMap = gson.fromJson(dataObject.get("attendance"), checkinMemberListType);

                    displayData(dataObject, attendanceMap);
                }else {
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + throwable.getMessage());
            }
        });
    }

    private void displayData(JsonObject dataObject, Map<String, List<AttendanceTeacher>> attendanceMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("Class Name: ").append(dataObject.get("class_name").getAsString()).append("\n");
        sb.append("Begin Date: ").append(dataObject.get("begin_date").getAsString()).append("\n\n");

        for (Map.Entry<String, List<AttendanceTeacher>> entry : attendanceMap.entrySet()) {
            String date = entry.getKey();
            List<AttendanceTeacher> attendanceList = entry.getValue();

            sb.append("Date: ").append(date).append("\n");
            for (AttendanceTeacher attendance : attendanceList) {
                sb.append("Member ID: ").append(attendance.getMember_id()).append("\n");
                sb.append("Member Name: ").append(attendance.getMember_name()).append("\n");
                sb.append("Day of Week: ").append(attendance.getDay_of_week()).append("\n");
                sb.append("In: ").append(attendance.getIn()).append("\n");
                sb.append("Out: ").append(attendance.getOut()).append("\n\n");
            }
        }
    }
}