package com.android.mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.adapter.Item_adapter;
import com.android.mobile.adapter.TheoryAdapter;
import com.android.mobile.adapter.lesson_adapter;
import com.android.mobile.models.Class;
import com.android.mobile.models.Lesson;
import com.android.mobile.models.NewsModel;
import com.android.mobile.models.ProductModel;
import com.android.mobile.models.TheoryModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.CheckinApiService;
import com.android.mobile.services.ProductApiService;
import com.android.mobile.services.TheoryApiService;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);

        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Lý thuyết võ thuật");
        myContentE.apply();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

// Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();

        search_edit_text = findViewById(R.id.search_edit_text);
        ImageButton search_button = findViewById(R.id.search_button);

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
        TheoryApiService apiService = ApiServiceProvider.getTheoryApiService();
        apiService.getMartialArtsTheory().enqueue(new Callback<List<TheoryModel>>() {
            @Override
            public void onResponse(Call<List<TheoryModel>> call, Response<List<TheoryModel>> response) {
                if(response.isSuccessful()){
                    theoryList.clear(); // Clear existing data
                    theoryList.addAll(response.body());
                    filterTheory(search_edit_text.getText().toString());
                    hideLoading();
                }else {
                    hideLoading();
                    Toast.makeText(activity_lessons.this, "Lỗi lấy danh sách lý thuyết, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TheoryModel>> call, Throwable throwable) {
                hideLoading();
                Toast.makeText(activity_lessons.this, "Lỗi kết nối, vui lòng thử lại", Toast.LENGTH_SHORT).show();
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
}