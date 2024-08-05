package com.android.mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.adapter.Adapter_Register_belt;
import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.adapter.HistoryClassAdapter;
import com.android.mobile.models.Belt;
import com.android.mobile.models.HistoryClassModel;
import com.android.mobile.network.APIServicePayment;
import com.android.mobile.services.PaymentAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryRegisterClass extends BaseActivity {


    private List<HistoryClassModel> chapters = new ArrayList<>();
    private RecyclerView recyclerView;
    private HistoryClassAdapter chapterAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history_register_class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        chapterAdapter = new HistoryClassAdapter(this, chapters);
        recyclerView = findViewById(R.id.chapter_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", null);



        PaymentAPI apiService = APIServicePayment.getPaymentApiService();
        Call<List<HistoryClassModel>> call = apiService.GetHistoryClass("Bearer" + token);
        call.enqueue(new Callback<List<HistoryClassModel>>() {
            @Override
            public void onResponse(Call<List<HistoryClassModel>> call, Response<List<HistoryClassModel>> response) {
                if(response.isSuccessful()){
                    List<HistoryClassModel> list = response.body();
                    chapters = response.body();
                    chapterAdapter.loadList(chapters);
                    recyclerView.setAdapter(chapterAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<HistoryClassModel>> call, Throwable t) {

            }
        });


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Thêm hoặc thay thế Fragment mới
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Lịch sử đăng ký");
        myContentE.apply();


        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();
    }
}