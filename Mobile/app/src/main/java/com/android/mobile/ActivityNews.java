package com.android.mobile;

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

import com.android.mobile.adapter.NewsAdapter;
import com.android.mobile.models.NewsModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.NewsApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityNews extends AppCompatActivity implements NewsAdapter.OnNewsClickListener {

    private SharedPreferences sharedPreferences;
    private static final String NAME_SHARED = "myContent";
    private static final String KEY_TITLE = "title";
    private static final String VALUE_INFO = "info";
    private List<NewsModel> newsList = new ArrayList<>();
    private NewsAdapter adapter;
    private static final String TAG = "ActivityNews";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(NAME_SHARED, MODE_PRIVATE);
        saveToSharedPreferences(KEY_TITLE, VALUE_INFO);

        RecyclerView recyclerView = findViewById(R.id.itemNews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NewsAdapter(newsList, this);
        recyclerView.setAdapter(adapter);

        fetchNews();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        // chèn fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();
    }

    private void saveToSharedPreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void fetchNews() {
        NewsApiService apiService = ApiServiceProvider.getNewsApiService();
        Call<List<NewsModel>> call = apiService.getAnouncements();
        call.enqueue(new Callback<List<NewsModel>>() {
            @Override
            public void onResponse(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "API Response: " + response.body().toString());
                    newsList.clear(); // Clear existing data
                    newsList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "API Response is not successful or body is null");
                    Toast.makeText(ActivityNews.this, "Không thể lấy danh sách tin tức", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NewsModel>> call, Throwable t) {
                Log.e(TAG, "API Call failed: " + t.getMessage());
                Toast.makeText(ActivityNews.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNewsClick(NewsModel news) {
        Intent intent = new Intent(ActivityNews.this, NewsDetailActivity.class);
        intent.putExtra("NewsTitle", news.getTenvi());
        intent.putExtra("NewsContent", news.getNoidungvi());
        intent.putExtra("NewsImage", news.getPhoto());
        startActivity(intent);
    }
}
