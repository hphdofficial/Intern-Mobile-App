package com.android.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.mobile.models.Member;
import com.android.mobile.models.OptionCategory;
import com.android.mobile.models.ProductModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.CheckinApiService;
import com.android.mobile.services.ProductApiService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_checkin extends BaseActivity {

    private Checkin_adapter checkinAdapter;
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

        Button btnDiemDanh = findViewById(R.id.btnDiemDanh);
        btnDiemDanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<CheckinMemberModel> checkinOptions = checkinAdapter.getCheckinList().stream()
                        .filter(CheckinMemberModel::isChecked)
                        .collect(Collectors.toList());

                AttendanceRequest attendanceRequest = new AttendanceRequest();
                attendanceRequest.setId_class(idClass);
                attendanceRequest.setDate(formattedDate);
//                attendanceRequest.setDate("2024-08-05");
                List<AttendanceRequest.Attendee> attendees = new ArrayList<>();
                AttendanceRequest.Attendee attendee = new AttendanceRequest.Attendee();

                for (CheckinMemberModel checkin : checkinOptions) {
                    attendee.setId_atg_member(checkin.getId());
                    attendee.setIn("18:00");
                    attendee.setOut("19:30");
                }

                attendees.add(attendee);

                attendanceRequest.setAttendees(attendees);

                CheckinApiService apiService = ApiServiceProvider.getCheckinApiService();
                apiService.teacherCheckin("Bearer "+token,attendanceRequest).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(activity_checkin.this, "Điểm danh thành công", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(activity_checkin.this, "Điểm danh thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {
                        System.out.println("Active: Call Onfail");
                        Log.e("PostData", "Failure: " + throwable.getMessage());
                    }
                });

            }
        });
    }

    private void fetchMemberForCheckin(int idClass, String token){
        CheckinApiService apiService = ApiServiceProvider.getCheckinApiService();
        apiService.getClassMembers(token, idClass).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject jsonObject = response.body();
                    Gson gson = new Gson();
                    Type checkinMemberListType = new TypeToken<List<CheckinMemberModel>>() {}.getType();
                    List<CheckinMemberModel> checkinMembers = gson.fromJson(jsonObject.get("data"), checkinMemberListType);
                    for (CheckinMemberModel memberSample : checkinMembers){
                        Log.e("PostData", "Success: " + memberSample.getTen());
                    }
                    checkinAdapter = new Checkin_adapter(getApplicationContext(), checkinMembers);
                    RecyclerView recyclerView = findViewById(R.id.recycler_checkin);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(checkinAdapter);
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
}