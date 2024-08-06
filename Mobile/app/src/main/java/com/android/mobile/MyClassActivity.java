package com.android.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.android.mobile.adapter.MyClassAdapter;
import com.android.mobile.models.Class;
import com.android.mobile.models.ClassModel;
import com.android.mobile.models.Club;
import com.android.mobile.models.ProductModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.CheckinApiService;
import com.android.mobile.services.ProductApiService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyClassActivity extends BaseActivity {

    private BlankFragment loadingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_class);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Những lớp đang dạy");
        myContentE.apply();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

// Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();

        FetchClassesForTeacher();
    }

    private void FetchClassesForTeacher(){
        showLoading();
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", null);
        CheckinApiService apiService = ApiServiceProvider.getCheckinApiService();
        apiService.getTeacherClasses("Bearer "+token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject jsonObject = response.body();
                    Gson gson = new Gson();
                    Type classListType = new TypeToken<List<ClassModel>>() {}.getType();
                    List<ClassModel> classes = gson.fromJson(jsonObject.get("data"), classListType);
                    for (ClassModel classSample : classes){
                        Log.e("PostData", "Success: " + classSample.getTen());
                    }
                    MyClassAdapter classAdapter = new MyClassAdapter(getApplicationContext(), classes);
                    RecyclerView recyclerView = findViewById(R.id.recycler_class);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(classAdapter);

                    hideLoading();
                }else {
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());
                    hideLoading();
                    Toast.makeText(MyClassActivity.this, "Đang chuyển qua lớp học cho học viên", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), activity_member_checkin.class));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                hideLoading();
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + throwable.getMessage());
                Toast.makeText(MyClassActivity.this, "Lỗi lấy dữ liệu lớp học", Toast.LENGTH_SHORT).show();
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