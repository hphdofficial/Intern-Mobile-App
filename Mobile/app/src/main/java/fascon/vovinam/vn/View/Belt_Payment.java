package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.Model.DetailsBelt;
import fascon.vovinam.vn.Model.network.APIServicePayment;
import fascon.vovinam.vn.Model.services.PaymentAPI;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Belt_Payment extends BaseActivity {

    private int idBelt;
    private int idCurrent;
    private String languageS;
    private TextView change_class;
    private TextView status_class;
    private TextView money;
    private TextView date;
    private TextView date1;
    private ImageView image;
    private TextView color;
    private TextView money1;
    private TextView danhxung;
    private Button buttonRegister;
    private LinearLayout linear;
    private  TextView date_learn;
    private ImageView qrcode;
    private LinearLayout linear1;
    private TextView satus;
    private BlankFragment loadingFragment;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_belt_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        date1 = findViewById(R.id.date);
        money1 = findViewById(R.id.money);
        change_class = findViewById(R.id.change_class);
        status_class = findViewById(R.id.status);
        money = findViewById(R.id.total);
        satus = findViewById(R.id.status_class);
        date = findViewById(R.id.date1);
        image = findViewById(R.id.image);
        color = findViewById(R.id.color);
        danhxung = findViewById(R.id.danhxung);
        buttonRegister = findViewById(R.id.buttonRegister);
        linear = findViewById(R.id.linear);
        linear1 = findViewById(R.id.linear1);
        date_learn = findViewById(R.id.date_learn);
        qrcode = findViewById(R.id.qrcode);
        showLoading();
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Thông tin đai");
        myContentE.apply();
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language",null);
        if(languageS!= null){
            if(languageS.contains("en")){
                myContentE.putString("title", "Belt Information");
                myContentE.apply();
                buttonRegister.setText("Scan qr to pay registration fee");
                satus.setText("Description");
                money1.setText("Register up belt");
                date1.setText("Time");
            }
        }

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
//        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();

        PaymentAPI apiService = APIServicePayment.getPaymentApiService();


        if (extras != null) {
            // Lấy dữ liệu từ Bundle
             idBelt = extras.getInt("id");
             idCurrent = extras.getInt("current");



         //   Toast.makeText(getApplicationContext(),idBelt+"",Toast.LENGTH_SHORT).show();
        }
        Call<List<DetailsBelt>> callBell;
        if(languageS != null){
            if(languageS.contains("en")){
                callBell =  apiService.getBeltInfo(idBelt,"en");
            }else {
                callBell =  apiService.getBeltInfo(idBelt,"vi");
            }
        }else  callBell =  apiService.getBeltInfo(idBelt,"vi");
        callBell.enqueue(new Callback<List<DetailsBelt>>() {
            @Override
            public void onResponse(Call<List<DetailsBelt>> call, Response<List<DetailsBelt>> response) {
                   if(response.isSuccessful()){
                    List<DetailsBelt> list = response.body();
                       DetailsBelt  de = list.get(0);
                    if(idBelt  > idCurrent+1 || idBelt <= idCurrent){
                        buttonRegister.setVisibility(View.GONE);
                        linear.setVisibility(View.GONE);
                        linear1.setVisibility(View.GONE);
                        if(idBelt >idCurrent+1 ){
                            date_learn.setText("Tình trạng: chưa thi");
                            if(languageS!= null){
                                if(languageS.contains("en")){
                                    date_learn.setText("Status: not yet tested");
                                }
                            }
                        }else {
                            date_learn.setText("Tình trạng: đã thi");
                            if(languageS!= null){
                                if(languageS.contains("en")){
                                    date_learn.setText("Status: passed exam");
                                }
                            }
                        }
                    }else {

                        buttonRegister.setVisibility(View.VISIBLE);
                        linear.setVisibility(View.VISIBLE);
                        linear1.setVisibility(View.VISIBLE);

                        money.setText(idBelt*200000+ "VND");
                        String total = idBelt*200000 + "";
                        displayQRCode("https://api.vietqr.io/image/970425-0937759311-cG4PADy.jpg&amount=" + total+

                                "&addInfo=" + "Dang ky dai");
                        date.setText(" chờ thông báo về email");
                        date_learn.setText("Tình trạng: chưa thi");
                        if(languageS!= null){
                            if(languageS.contains("en")){
                                date.setText(" Wait for email notification");
                                date_learn.setText("Status: not yet tested");
                            }
                        }

                    }

                    status_class.setText(de.getMoTa());
                    danhxung.setText("Danh xưng:\t " + de.getDanhXung());
                    color.setText("Màu đai:\t " + de.getColer());
                    change_class.setText("Thông tin đai " + de.getTen());
                       if(languageS!= null){
                           if(languageS.contains("en")){
                               danhxung.setText("Title:\t " + de.getDanhXung());
                               color.setText("Belt color:\t " + de.getColer());
                               change_class.setText("Belt infor: " + de.getTen());
                           }
                       }
                    Picasso.get().load(de.getLink()).placeholder(R.drawable.photo3x4).error(R.drawable.photo3x4).into(image);

                    hideLoading();
                }else {
                    Toast.makeText(getApplicationContext(),"fail nn",Toast.LENGTH_SHORT).show();
                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<List<DetailsBelt>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"fail nn",Toast.LENGTH_SHORT).show();
            }
        });



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
    public void displayQRCode(String qrCodeUrl) {
        ImageView qrCodeImageView = findViewById(R.id.qrcode);
        Glide.with(this)
                .load(qrCodeUrl)
                .into(qrCodeImageView);
    }
}