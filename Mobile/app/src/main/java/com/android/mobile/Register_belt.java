package com.android.mobile;

import android.content.Context;
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

import com.android.mobile.adapter.Adapter_Register_belt;
import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.adapter.Chapter_adapter;
import com.android.mobile.models.Belt;
import com.android.mobile.models.BeltModel;
import com.android.mobile.models.CartResponse;
import com.android.mobile.models.Chapter;
import com.android.mobile.network.APIServicePayment;
import com.android.mobile.services.PaymentAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register_belt extends BaseActivity {


    private  List<Belt> chapters = new ArrayList<>();
    private RecyclerView recyclerView;
    private  Adapter_Register_belt chapterAdapter;
    private BlankFragment loadingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);


        chapterAdapter = new Adapter_Register_belt(this, chapters);
        recyclerView = findViewById(R.id.chapter_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", null);


        showLoading();
        PaymentAPI apiService = APIServicePayment.getPaymentApiService();

        Call<BeltModel> callBell = apiService.GetBelt("Bearer" + token);

        callBell.enqueue(new Callback<BeltModel>() {
            @Override
            public void onResponse(Call<BeltModel> call, Response<BeltModel> response) {
               if(response.isSuccessful()){
                   BeltModel b = response.body();


                   chapterAdapter.loadBelt(b);

                   Call<List<Belt>> callb = apiService.getAllBelt();
                   callb.enqueue(new Callback<List<Belt>>() {
                       @Override
                       public void onResponse(Call<List<Belt>> call, Response<List<Belt>> response) {
                           chapters = response.body();
                           chapterAdapter.loadList(chapters);
                           recyclerView.setAdapter(chapterAdapter);
                           hideLoading();
                       }

                       @Override
                       public void onFailure(Call<List<Belt>> call, Throwable t) {

                       }
                   });

               }else {
                   Toast.makeText(getApplicationContext(),"fails nn",Toast.LENGTH_SHORT).show();
               }
            }

            @Override
            public void onFailure(Call<BeltModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"fails",Toast.LENGTH_SHORT).show();
            }
        });



       /* chapters.add(new Belt("Đai trắng","Đã học"));
        chapters.add(new Belt("Đai đen","Đã học"));*/

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");






        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Danh sách đai");
        myContentE.apply();
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