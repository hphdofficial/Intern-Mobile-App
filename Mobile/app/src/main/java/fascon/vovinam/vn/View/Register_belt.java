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

import fascon.vovinam.vn.ViewModel.Adapter_Register_belt;
import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.Model.Belt;
import fascon.vovinam.vn.Model.BeltModel;
import fascon.vovinam.vn.Model.network.APIServicePayment;
import fascon.vovinam.vn.Model.services.PaymentAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register_belt extends BaseActivity {

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
    private  List<Belt> chapters = new ArrayList<>();
    private RecyclerView recyclerView;
    private Adapter_Register_belt chapterAdapter;
    private BlankFragment loadingFragment;
    private String languageS;
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
                           Toast.makeText(getApplicationContext(),"fails nn",Toast.LENGTH_SHORT).show();
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
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language",null);
        if(languageS!= null){
            if(languageS.contains("en")){
                myContentE.putString("title", "Belt List");
                myContentE.apply();
            }
        }
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