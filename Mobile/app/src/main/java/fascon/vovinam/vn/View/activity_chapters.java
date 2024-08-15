package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.ViewModel.Theory_Belt_Adapter;
import fascon.vovinam.vn.Model.Belt;
import fascon.vovinam.vn.Model.network.APIServicePayment;
import fascon.vovinam.vn.Model.services.PaymentAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_chapters extends BaseActivity {

    List<Belt> belts = new ArrayList<>();
    private BlankFragment loadingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");


        showLoading();
        PaymentAPI apiService = APIServicePayment.getPaymentApiService();
        Call<List<Belt>> call = apiService.getAllBelt();
        call.enqueue(new Callback<List<Belt>>() {
            @Override
            public void onResponse(Call<List<Belt>> call, Response<List<Belt>> response) {
                if(response.isSuccessful()){
                    belts = response.body();
                    Theory_Belt_Adapter beltAdapter = new Theory_Belt_Adapter(belts, activity_chapters.this);
                    RecyclerView recyclerView = findViewById(R.id.chapter_recycleview);
                    recyclerView.setLayoutManager(new LinearLayoutManager(activity_chapters.this));
                    recyclerView.setAdapter(beltAdapter);
                    hideLoading();
                }else{
                    Toast.makeText(activity_chapters.this, "Lỗi lấy dữ liệu", Toast.LENGTH_SHORT).show();
                    hideLoading();
                }

            }

            @Override
            public void onFailure(Call<List<Belt>> call, Throwable t) {
                Toast.makeText(activity_chapters.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
                hideLoading();
            }
        });

        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Danh sách lý thuyết");
        myContentE.apply();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

// Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
//        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();


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