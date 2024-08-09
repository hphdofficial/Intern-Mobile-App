package com.android.mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SearchView;
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

public class ClassActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private ClassAdapter adapter;
    private List<Class> classList = new ArrayList<>();
    private SearchView searchView;
    private BlankFragment loadingFragment;

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

        searchView = findViewById(R.id.search_class);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchClass(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                searchClass(newText);
                return false;
            }
        });
        showLoading();

        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", null);

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<List<Class>> call = service.getClassofClub("Bearer" + token);

        call.enqueue(new Callback<List<Class>>() {
            @Override
            public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                hideLoading();

                if (response.isSuccessful()) {
                    List<Class> classes = response.body();
                    adapter.setData(classes);
                    if (!classes.isEmpty()) {
                        Toast.makeText(ClassActivity.this, "Tải dữ liệu thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ClassActivity.this, "Không có lớp học nào thuộc câu lạc bộ bạn tham gia", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ClassActivity.this, "Không có lớp học nào thuộc câu lạc bộ bạn tham gia", Toast.LENGTH_SHORT).show();
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Class>> call, Throwable t) {
                hideLoading();

                t.printStackTrace();
            }
        });
    }

    public void searchClass(String text) {
        showLoading();

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<List<Class>> call = service.searchClass(text);

        call.enqueue(new Callback<List<Class>>() {
            @Override
            public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                hideLoading();

                if (response.isSuccessful()) {
                    List<Class> classes = response.body();
                    adapter.setData(classes);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(ClassActivity.this, "Tìm kiếm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Class>> call, Throwable t) {
                hideLoading();

                t.printStackTrace();
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