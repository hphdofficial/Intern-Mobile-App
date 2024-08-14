package com.android.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;

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
import com.android.mobile.models.ClubModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.NewsApiService;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private Spinner clubSpinner;
    private BlankFragment loadingFragment;
    private ArrayAdapter<ClubModel> spinnerAdapter;
    private List<ClubModel> clubList = new ArrayList<>();
    private TextView noNewsText; // TextView hiển thị khi không có thông báo nào
    private boolean isFirstLoad = true; // Cờ để kiểm tra lần tải đầu tiên

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

        // Chèn fragment tiêu đề
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.commit();

        // Thiết lập RecyclerView và Adapter
        RecyclerView recyclerView = findViewById(R.id.itemNews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(filteredNewsList, this);
        recyclerView.setAdapter(adapter);

        noNewsText = findViewById(R.id.no_news_text); // Ánh xạ TextView

        // Thiết lập EditText và ImageButton cho tìm kiếm
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

        // Thiết lập Spinner và tải danh sách câu lạc bộ
        clubSpinner = findViewById(R.id.club_spinner);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clubList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clubSpinner.setAdapter(spinnerAdapter);

        fetchClubs();

        // Xử lý sự kiện chọn mục trong Spinner
        clubSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isFirstLoad) { // Chỉ thực hiện khi không phải lần tải đầu tiên
                    ClubModel selectedClub = (ClubModel) parent.getItemAtPosition(position);

                    if (selectedClub.getId() == 0) {
                        fetchAllNews(); // Nếu chọn "Tất cả"
                    } else {
                        fetchNewsByClub(selectedClub.getId()); // Lọc theo câu lạc bộ
                    }
                } else {
                    isFirstLoad = false; // Đặt cờ là đã tải lần đầu
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì cả
            }
        });

        // Tải tất cả tin tức lần đầu tiên
        fetchAllNews();
    }

    private void fetchClubs() {
        NewsApiService apiService = ApiServiceProvider.getNewsApiService();
        Call<List<ClubModel>> call = apiService.getAllClubs();
        call.enqueue(new Callback<List<ClubModel>>() {
            @Override
            public void onResponse(Call<List<ClubModel>> call, Response<List<ClubModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    clubList.clear();

                    // Thêm mục "Tất cả" vào đầu danh sách
                    ClubModel allClubs = new ClubModel();
                    allClubs.setId(0); // ID giả định cho "Tất cả"
                    allClubs.setTen("Tất cả");
                    clubList.add(allClubs);

                    clubList.addAll(response.body());
                    spinnerAdapter.notifyDataSetChanged(); // Cập nhật dữ liệu cho Spinner
                } else {
                    Toast.makeText(ActivityNews.this, "Không thể lấy danh sách câu lạc bộ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ClubModel>> call, Throwable t) {
                Toast.makeText(ActivityNews.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAllNews() {
        showLoading();
        NewsApiService apiService = ApiServiceProvider.getNewsApiService();
        Call<List<NewsModel>> call = apiService.getAllNews(); // Sử dụng API mới để lấy tất cả tin tức
        call.enqueue(new Callback<List<NewsModel>>() {
            @Override
            public void onResponse(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    newsList.clear(); // Xóa dữ liệu cũ

                    // Thêm tất cả tin tức vào danh sách
                    newsList.addAll(response.body());


                    // Sắp xếp tin tức theo thứ tự mới nhất lên đầu
                    Collections.sort(newsList, new Comparator<NewsModel>() {
                        @Override
                        public int compare(NewsModel o1, NewsModel o2) {
                            // Giả sử ngaytao là trường thời gian lưu trữ theo UNIX timestamp
                            return Long.compare(o2.getNgaytao(), o1.getNgaytao()); // Sắp xếp giảm dần
                        }
                    });

                    filterNews(searchEditText.getText().toString()); // Lọc theo tìm kiếm
                } else {
                    showNoNewsMessage();
                }
            }

            @Override
            public void onFailure(Call<List<NewsModel>> call, Throwable t) {
                hideLoading();
                Toast.makeText(ActivityNews.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchNewsByClub(int clubId) {
        showLoading();
        NewsApiService apiService = ApiServiceProvider.getNewsApiService();
        Call<List<NewsModel>> call = apiService.filterAnnouncementsByClub(clubId);
        call.enqueue(new Callback<List<NewsModel>>() {
            @Override
            public void onResponse(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    filteredNewsList.clear();
                    filteredNewsList.addAll(response.body());

                    // Sắp xếp tin tức theo thứ tự mới nhất lên đầu
                    Collections.sort(filteredNewsList, new Comparator<NewsModel>() {
                        @Override
                        public int compare(NewsModel o1, NewsModel o2) {
                            return Long.compare(o2.getNgaytao(), o1.getNgaytao()); // Sắp xếp giảm dần
                        }
                    });

                    adapter.notifyDataSetChanged();
                    checkIfNoNews();
                } else {
                    showNoNewsMessage();
                }
            }

            @Override
            public void onFailure(Call<List<NewsModel>> call, Throwable t) {
                hideLoading();
                Toast.makeText(ActivityNews.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void searchNews(String query) {
        if (query.isEmpty()) {
            int selectedClubId = ((ClubModel) clubSpinner.getSelectedItem()).getId();
            if (selectedClubId == 0) {
                fetchAllNews();
            } else {
                fetchNewsByClub(selectedClubId);
            }
        } else {
            NewsApiService apiService = ApiServiceProvider.getNewsApiService();
            Call<List<NewsModel>> call = apiService.searchAnouncements(query);
            call.enqueue(new Callback<List<NewsModel>>() {
                @Override
                public void onResponse(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        filteredNewsList.clear();
                        filteredNewsList.addAll(response.body());
                        adapter.notifyDataSetChanged();
                        checkIfNoNews();
                    } else {
                        showNoNewsMessage();
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
        checkIfNoNews();
    }

    private void checkIfNoNews() {
        if (filteredNewsList.isEmpty()) {
            noNewsText.setVisibility(View.VISIBLE);
        } else {
            noNewsText.setVisibility(View.GONE);
        }
    }

    private void showNoNewsMessage() {
        filteredNewsList.clear();
        adapter.notifyDataSetChanged();
        noNewsText.setVisibility(View.VISIBLE);
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
