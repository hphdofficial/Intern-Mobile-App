package com.android.mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.android.mobile.adapter.ClassAdapter;
import com.android.mobile.adapter.ClubAdapter;
import com.android.mobile.models.Class;
import com.android.mobile.models.Club;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.ClassApiService;
import com.android.mobile.services.ClubApiService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ClassAdapter adapter;
    private List<Class> classList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Danh sách lớp học");
        myContentE.apply();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new titleFragment());
        fragmentTransaction.commit();

        adapter = new ClassAdapter(this, classList);
        recyclerView = findViewById(R.id.recycler_class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

//        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vdm92aW5hbW1vaS00YmVkYjZkZDFjMDUuaGVyb2t1YXBwLmNvbS9hcGkvYXV0aC9sb2dpbiIsImlhdCI6MTcyMjY0MTE5NiwiZXhwIjoxNzIyNzI3NTk2LCJuYmYiOjE3MjI2NDExOTYsImp0aSI6ImhkV2p3OWZVWFRzYnkzdEciLCJzdWIiOiIyNTciLCJwcnYiOiIxMDY2NmI2ZDAzNThiMTA4YmY2MzIyYTg1OWJkZjk0MmFmYjg4ZjAyIiwibWVtYmVyX2lkIjoyNTcsInJvbGUiOjB9.0KPLL12aL9IM4_K9Dp8CLAibwnzT-4QIbqNP8XYZK4c";

        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
                String token = sharedPreferences.getString("access_token", null);

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<List<Class>> call = service.getClassofClub("Bearer" + token);

        call.enqueue(new Callback<List<Class>>() {
            @Override
            public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                if (response.isSuccessful()) {
                    List<Class> classes = response.body();
                    adapter.setData(classes);
                    Toast.makeText(ClassActivity.this, "Success " + response.message(), Toast.LENGTH_SHORT).show();
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Class>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}