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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.adapter.Checked_Teacher_adapter;
import com.android.mobile.adapter.Checkin_adapter;
import com.android.mobile.adapter.OptionCheckBoxAdapter;
import com.android.mobile.models.ApiResponse;
import com.android.mobile.models.AttendanceTeacher;
import com.android.mobile.models.CheckinMemberModel;
import com.android.mobile.models.ClassData;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.CheckinApiService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_teacher_checkin extends BaseActivity {
    private static final String TAG = "MainActivity";
    private List<AttendanceTeacher> listChecked = new ArrayList<>();
    private BlankFragment loadingFragment;

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

        TextView txtClassName = findViewById(R.id.txtClassName);

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

        showLoading();
        CheckinApiService apiService = ApiServiceProvider.getCheckinApiService();
        apiService.teacherViewCheckin("Bearer "+token, "2023-11-01", formattedDate, idClass).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject jsonObject = response.body();
                    if(jsonObject!=null){
                        Gson gson = new Gson();
                        Type apiResponseType = new TypeToken<ApiResponse>(){}.getType();
                        ApiResponse apiResponse = gson.fromJson(jsonObject, apiResponseType);

                        if (apiResponse != null && "Thành công".equals(apiResponse.getSuccess())) {
                            List<String> stringList = new ArrayList<>();
                            List<ClassData> classDataList = apiResponse.getData();
                            for (ClassData classData : classDataList) {
                                //Tên lớp
                                txtClassName.setText(classData.getClassName());

                                for (Map.Entry<String, List<AttendanceTeacher>> entry : classData.getAttendance().entrySet()) {

                                    String date = entry.getKey();
                                    stringList.add(date);
                                    List<AttendanceTeacher> attendanceList = entry.getValue();
                                    for (AttendanceTeacher attendance : attendanceList) {
                                        attendance.setDate(date);
                                        listChecked.add(attendance);
                                    }
                                }
                                for (String s : stringList){
                                    System.out.println("Date: "+ s);
                                }
                            }
                            Checked_Teacher_adapter checkedTeacherAdapter = new Checked_Teacher_adapter(getApplicationContext(), listChecked);
                            RecyclerView recyclerView = findViewById(R.id.recycler_checkin);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(checkedTeacherAdapter);

                            hideLoading();
                        }
                    }


                }else {
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());

                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
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
}