package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.ViewModel.TheoryAdapter;
import fascon.vovinam.vn.Model.Club;
import fascon.vovinam.vn.Model.TheoryModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.ClubApiService;
import fascon.vovinam.vn.Model.services.TheoryApiService;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_lessons extends BaseActivity {
    private List<TheoryModel> theoryList = new ArrayList<>();
    private List<TheoryModel> filteredTheoryList = new ArrayList<>();
    private TheoryAdapter theoryAdapter;
    private EditText search_edit_text;
    private BlankFragment loadingFragment;
    private String token;
    private String languageS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);

        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Danh sách lý thuyết theo đai");
        myContentE.apply();
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);


        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("access_token", null);
//        token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vdm92aW5hbW1vaS00YmVkYjZkZDFjMDUuaGVyb2t1YXBwLmNvbS9hcGkvYXV0aC9sb2dpbiIsImlhdCI6MTcyMjg2OTcwOCwiZXhwIjoxNzIyOTU2MTA4LCJuYmYiOjE3MjI4Njk3MDgsImp0aSI6IlcyaTRucGpCWldBMGtYMEoiLCJzdWIiOiIyNDkiLCJwcnYiOiIxMDY2NmI2ZDAzNThiMTA4YmY2MzIyYTg1OWJkZjk0MmFmYjg4ZjAyIiwibWVtYmVyX2lkIjoyNDksInJvbGUiOjB9.SKZRS6X1ON0Eh2Dk9H6MMpHeO59Sgg1sGmVu7qt0xaM";

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

// Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
//        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();

        search_edit_text = findViewById(R.id.search_edit_text);
        ImageButton search_button = findViewById(R.id.search_button);
        languageS = shared.getString("language",null);
        if(languageS!= null){
            if(languageS.contains("en")){
                search_edit_text.setHint("Find Theories");
                myContentE.putString("title", "List Theories By Belt");
                myContentE.apply();
            }
        }
        search_edit_text.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                SearchTheoryByName(search_edit_text.getText().toString());
                return true;
            }
            return false;
        });

        theoryAdapter = new TheoryAdapter(getApplicationContext(), filteredTheoryList);
        RecyclerView recyclerView = findViewById(R.id.lesson_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(theoryAdapter);

        FetchTheory();

        search_button.setOnClickListener(v -> SearchTheoryByName(search_edit_text.getText().toString()));

        // Listen for text changes to filter the list
        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTheory(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    private void FetchTheory(){
        showLoading();

        ClubApiService apiServiceClub = ApiServiceProvider.getClubApiService();
        apiServiceClub.getDetailClubMember("Bearer "+token).enqueue(new Callback<Club>() {
            @Override
            public void onResponse(Call<Club> call, Response<Club> response) {
                if(response.isSuccessful()){
                    Club club = response.body();
                    if(club.getId_club().equals("0")){
                        if(languageS!= null){
                            if(languageS.contains("en")){
                                Toast.makeText(activity_lessons.this, "You have not resgíter any club, please register and come again", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(activity_lessons.this, "Bạn chưa đăng ký club, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();

                            }
                        }else{
                            Toast.makeText(activity_lessons.this, "Bạn chưa đăng ký club, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();

                        }

                    }else{
                        int idClub = Integer.parseInt(club.getId_club());

                        Intent intent = getIntent();
                        int idBelt = intent.getIntExtra("idBelt", -1);

                        // Lấy danh sách lý thuyết theo id đai vs id Club
                        TheoryApiService apiService = ApiServiceProvider.getTheoryApiService();
                        apiService.getMartialArtsTheoryByClubByBelt(idClub, idBelt).enqueue(new Callback<List<TheoryModel>>() {
                            @Override
                            public void onResponse(Call<List<TheoryModel>> call, Response<List<TheoryModel>> response) {
                                if(response.isSuccessful()){
                                    theoryList.clear(); // Clear existing data
                                    theoryList.addAll(response.body());
                                    if(theoryList.isEmpty()){
                                        if(languageS!= null){
                                            if(languageS.contains("en")){
                                                Toast.makeText(activity_lessons.this, "Club have no Theory", Toast.LENGTH_SHORT).show();

                                            }else{
                                                Toast.makeText(activity_lessons.this, "Club chưa có lý thuyết", Toast.LENGTH_SHORT).show();

                                            }
                                        }else{
                                            Toast.makeText(activity_lessons.this, "Club chưa có lý thuyết", Toast.LENGTH_SHORT).show();

                                        }

                                    }else{
                                        filterTheory(search_edit_text.getText().toString());
                                    }

                                    hideLoading();
                                }else {
                                    hideLoading();
                                    if(languageS!= null){
                                        if(languageS.contains("en")){
                                            Toast.makeText(activity_lessons.this, "Cant find any Theory", Toast.LENGTH_SHORT).show();

                                        }else{
                                            Toast.makeText(activity_lessons.this, "Không tìm thấy lý thuyết", Toast.LENGTH_SHORT).show();

                                        }
                                    }else{
                                        Toast.makeText(activity_lessons.this, "Không tìm thấy lý thuyết", Toast.LENGTH_SHORT).show();

                                    }
                                    System.out.println("Active: Call onResponse");
                                    Log.e("PostData", "Error: " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<List<TheoryModel>> call, Throwable throwable) {
                                hideLoading();
                                if(languageS!= null){
                                    if(languageS.contains("en")){
                                        Toast.makeText(activity_lessons.this, "Internet Lost, please try again", Toast.LENGTH_SHORT).show();

                                    }else{
                                        Toast.makeText(activity_lessons.this, "Lỗi kết nối mạng, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();

                                    }
                                }else{
                                    Toast.makeText(activity_lessons.this, "Lỗi kết nối mạng, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();

                                }
                                System.out.println("Active: Call Onfail");
                                Log.e("PostData", "Failure: " + throwable.getMessage());
                            }
                        });
                    }

                }else {
                    hideLoading();
                    if(languageS!= null){
                        if(languageS.contains("en")){
                            Toast.makeText(activity_lessons.this, "You have not resgíter any club, please register and come again", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(activity_lessons.this, "Bạn chưa đăng ký club, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();

                        }
                    }else{
                        Toast.makeText(activity_lessons.this, "Bạn chưa đăng ký club, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();

                    }
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Club> call, Throwable throwable) {
                hideLoading();
                if(languageS!= null){
                    if(languageS.contains("en")){
                        Toast.makeText(activity_lessons.this, "Internet Lost, please try again", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(activity_lessons.this, "Lỗi kết nối mạng, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(activity_lessons.this, "Lỗi kết nối mạng, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();

                }
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + throwable.getMessage());
            }
        });

    }

    private void SearchTheoryByName(String query){
        if (query.isEmpty()) {
            FetchTheory(); // Fetch all theory if query is empty
        }else{
            TheoryApiService apiService = ApiServiceProvider.getTheoryApiService();
            apiService.searchMartialArtsTheory(query).enqueue(new Callback<List<TheoryModel>>() {
                @Override
                public void onResponse(Call<List<TheoryModel>> call, Response<List<TheoryModel>> response) {
                    if(response.isSuccessful()){
                        List<TheoryModel> theoryList = response.body();
                        theoryList.clear(); // Clear existing data
                        theoryList.addAll(response.body());
                        filterTheory(query);
                    }else {
                        System.out.println("Active: Call onResponse");
                        Log.e("PostData", "Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<TheoryModel>> call, Throwable throwable) {
                    System.out.println("Active: Call Onfail");
                    Log.e("PostData", "Failure: " + throwable.getMessage());
                }
            });
        }

    }

    private void filterTheory(String query) {
        filteredTheoryList.clear();
        String normalizedQuery = removeDiacritics(query.toLowerCase());
        if (normalizedQuery.isEmpty()) {
            filteredTheoryList.addAll(theoryList);
        } else {
            for (TheoryModel theory : theoryList) {
                if (removeDiacritics(theory.getTenvi().toLowerCase()).contains(normalizedQuery) ||
                        removeDiacritics(theory.getNoidungvi().toLowerCase()).contains(normalizedQuery)) {
                    filteredTheoryList.add(theory);
                }
            }
        }
        theoryAdapter.notifyDataSetChanged();
    }

    private String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
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