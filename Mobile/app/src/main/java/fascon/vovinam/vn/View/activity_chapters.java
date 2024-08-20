package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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
    private String languageS;
    List<Belt> belts = new ArrayList<>();
    private BlankFragment loadingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language",null);
        showLoading();
        PaymentAPI apiService = APIServicePayment.getPaymentApiService();
        Call<List<Belt>> call;
        if(languageS != null){
            if(languageS.contains("en")){

                call = apiService.getAllBelt("en");
            }else {
                call = apiService.getAllBelt("vi");
            }
        }else call = apiService.getAllBelt("vi");

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

        if(languageS!= null){
            if(languageS.contains("en")){
                myContentE.putString("title", "List Theories");
                myContentE.apply();
            }
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

// Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
//        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();


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