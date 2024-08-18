package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.ViewModel.NewsAdapter;
import fascon.vovinam.vn.Model.NewsModel;
import fascon.vovinam.vn.Model.ClubModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.NewsApiService;

import java.nio.charset.StandardCharsets;
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
    private TextView noNewsText;
    private boolean isFirstLoad = true;
    private String languageS;

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
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language",null);
        if(languageS!= null){
            if(languageS.contains("en")){
                myContentE.putString("title", "News and Notification");
                myContentE.apply();
            }
        }
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNews(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
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
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        club_label = findViewById(R.id.club_label);
        // Tải tất cả tin tức lần đầu tiên
        fetchAllNews();
        if(languageS!= null){
            if(languageS.contains("en")){
                searchEditText.setHint("Enter find news");
                club_label.setText("Filter by club");
            }
        }
    }
    private TextView club_label;

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
                    allClubs.setId(0);
                    allClubs.setTen("Tất cả");
                    if(languageS!= null){
                        if(languageS.contains("en")){
                            allClubs.setTen("All");
                        }
                    }
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
    public static String decodeRoleFromToken(String jwtToken) {
        try {
            // Tách token thành các phần: header, payload, và signature
            String[] parts = jwtToken.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid JWT token format");
            }

            // Phần payload là phần thứ 2
            String payload = parts[1];

            // Giải mã payload từ Base64Url
            byte[] decodedBytes = Base64.decode(payload, Base64.URL_SAFE);
            String decodedPayload = new String(decodedBytes, StandardCharsets.UTF_8);

            // Chuyển đổi payload thành JSONObject
            JSONObject jsonObject = new JSONObject(decodedPayload);

            // Trích xuất role từ payload
            return jsonObject.getString("role");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private void fetchAllNews() {
        showLoading();
        NewsApiService apiService = ApiServiceProvider.getNewsApiService();
        Call<List<NewsModel>> call = apiService.getAnouncements(); // Gọi API lấy các bài báo mới nhất
        call.enqueue(new Callback<List<NewsModel>>() {
            @Override
            public void onResponse(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    newsList.clear(); // Xóa dữ liệu cũ

                    // Lấy câu lạc bộ hiện tại được chọn
                    ClubModel selectedClub = (ClubModel) clubSpinner.getSelectedItem();
                    int selectedClubId = 0; // Giá trị mặc định nếu không có câu lạc bộ nào được chọn

                    if (selectedClub != null) {
                        selectedClubId = selectedClub.getId(); // Lấy ID câu lạc bộ nếu không null
                    } else {
//                        Toast.makeText(ActivityNews.this, "Không tìm thấy câu lạc bộ", Toast.LENGTH_SHORT).show();
                    }

                    // Kiểm tra vai trò người dùng để lọc các bài báo
                    if (isCoach()) {
                        // Nếu là HLV, chỉ hiển thị các bài báo có type là "giang-vien" và thuộc câu lạc bộ được chọn
                        for (NewsModel news : response.body()) {
                            if ("giang-vien".equals(news.getType()) && (selectedClubId == 0 || news.getId_club() == selectedClubId)) {
                                newsList.add(news);
                            }
                        }
                    } else {
                        // Nếu không phải HLV, hiển thị tất cả bài báo trừ "giang-vien"
                        for (NewsModel news : response.body()) {
                            if (!"giang-vien".equals(news.getType()) && (selectedClubId == 0 || news.getId_club() == selectedClubId)) {
                                newsList.add(news);
                            }
                        }
                    }

                    // Sắp xếp tin tức theo thứ tự mới nhất lên đầu
                    Collections.sort(newsList, (o1, o2) -> Long.compare(o2.getNgaytao(), o1.getNgaytao()));

                    // Lọc theo tìm kiếm nếu có
                    filterNews(searchEditText.getText().toString());

                    // Cập nhật danh sách tin tức
                    adapter.notifyDataSetChanged();
                    checkIfNoNews(); // Kiểm tra nếu không có tin tức
                } else {
                    showNoNewsMessage(); // Hiển thị thông báo nếu không có tin tức
                }
            }

            @Override
            public void onFailure(Call<List<NewsModel>> call, Throwable t) {
                hideLoading();
                Toast.makeText(ActivityNews.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean isCoach() {
        // Đảm bảo sharedPreferences được khởi tạo
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        }

        String token = sharedPreferences.getString("access_token", null);
        if (token == null) {
            // Trường hợp không tìm thấy token
            return false;
        }

        String role = decodeRoleFromToken(token);
        return role != null && role.contains("1");
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

                    // Kiểm tra vai trò người dùng để lọc các bài báo
                    if (isCoach()) {
                        // Nếu là HLV, chỉ hiển thị các bài báo có type là "giang-vien" và id_club khớp với clubId
                        for (NewsModel news : response.body()) {
                            if ("giang-vien".equals(news.getType()) && news.getId_club() == clubId) {
                                filteredNewsList.add(news);
                            }
                        }
                    } else {
                        // Nếu không phải HLV, hiển thị tất cả bài báo trừ "giang-vien"
                        for (NewsModel news : response.body()) {
                            if (!"giang-vien".equals(news.getType()) && news.getId_club() == clubId) {
                                filteredNewsList.add(news);
                            }
                        }
                    }

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
        if (loadingFragment != null && loadingFragment.isAdded()) { // Kiểm tra fragment đã được thêm vào FragmentManager chưa
            loadingFragment.dismissAllowingStateLoss(); // Dùng dismissAllowingStateLoss thay vì dismiss để tránh IllegalStateException
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
    private TextView text;
    public void onMenuItemClick(View view) {
        text = findViewById(R.id.languageText);
        String language = text.getText()+"";
        if(view.getId() == R.id.btn_change){
            SharedPreferences sga = getSharedPreferences("login_prefs", MODE_PRIVATE);
            SharedPreferences.Editor edit =  sga.edit();

            if(language.contains("VN")){
                edit.putString("language","en");
                text.setText("ENG");
            }else {
                edit.putString("language","vn");
                text.setText("VN");
            }
            edit.apply();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}
