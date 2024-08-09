package com.android.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.android.mobile.adapter.NewsAdapter;
import com.android.mobile.models.NewsModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.NewsApiService;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityNews extends BaseActivity implements NewsAdapter.OnNewsClickListener {

    private SharedPreferences sharedPreferences;
    private List<NewsModel> newsList = new ArrayList<>();
    private List<NewsModel> filteredNewsList = new ArrayList<>();
    private NewsAdapter adapter;
    private static final String TAG = "ActivityNews";
    private EditText searchEditText;
    private ImageButton searchButton;

    private BlankFragment loadingFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news);

        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Tin tức và thông báo");
        myContentE.apply();

        RecyclerView recyclerView = findViewById(R.id.itemNews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NewsAdapter(filteredNewsList, this);
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

        // Initialize EditText and ImageButton
        searchEditText = findViewById(R.id.search_edit_text);
        searchButton = findViewById(R.id.search_button);

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchNews(searchEditText.getText().toString());
                return true;
            }
            return false;
        });

        searchButton.setOnClickListener(v -> searchNews(searchEditText.getText().toString()));

        // Listen for text changes to filter the list
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNews(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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


    private void saveToSharedPreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void fetchNews() {
        showLoading();
        NewsApiService apiService = ApiServiceProvider.getNewsApiService();
        Call<List<NewsModel>> call = apiService.getAnouncements();
        call.enqueue(new Callback<List<NewsModel>>() {
            @Override
            public void onResponse(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "API Response: " + response.body().toString());
                    newsList.clear(); // Clear existing data
                    newsList.addAll(response.body());
                    filterNews(searchEditText.getText().toString());
                } else {
                    Log.d(TAG, "API Response is not successful or body is null");
                    Toast.makeText(ActivityNews.this, "Không thể lấy danh sách tin tức", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NewsModel>> call, Throwable t) {
                hideLoading();
                Log.e(TAG, "API Call failed: " + t.getMessage());
                Toast.makeText(ActivityNews.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchNews(String query) {
        if (query.isEmpty()) {
            fetchNews(); // Fetch all news if query is empty
        } else {
            NewsApiService apiService = ApiServiceProvider.getNewsApiService();
            Call<List<NewsModel>> call = apiService.searchAnouncements(query);
            call.enqueue(new Callback<List<NewsModel>>() {
                @Override
                public void onResponse(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        newsList.clear(); // Clear existing data
                        newsList.addAll(response.body());
                        filterNews(query);
                    } else {
                        Toast.makeText(ActivityNews.this, "Không thể tìm kiếm tin tức", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<NewsModel>> call, Throwable t) {
                    Toast.makeText(ActivityNews.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void filterNews(String query) {
        filteredNewsList.clear();
        String normalizedQuery = removeDiacritics(query.toLowerCase());
        if (normalizedQuery.isEmpty()) {
            filteredNewsList.addAll(newsList);
        } else {
            for (NewsModel news : newsList) {
                if (removeDiacritics(news.getTenvi().toLowerCase()).contains(normalizedQuery) ||
                        removeDiacritics(news.getNoidungvi().toLowerCase()).contains(normalizedQuery)) {
                    filteredNewsList.add(news);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
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
