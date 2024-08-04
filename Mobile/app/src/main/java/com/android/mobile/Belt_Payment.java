package com.android.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.models.BeltModel;
import com.android.mobile.models.DetailsBelt;
import com.android.mobile.network.APIServicePayment;
import com.android.mobile.services.PaymentAPI;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Belt_Payment extends BaseActivity {

    private int idBelt;
    private int idCurrent;

    private TextView change_class;
    private TextView status_class;
    private TextView money;
    private TextView date;
    private ImageView image;
    private TextView color;
    private TextView danhxung;
    private Button buttonRegister;
    private LinearLayout linear;
    private  TextView date_learn;
    private LinearLayout linear1;
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

        change_class = findViewById(R.id.change_class);
        status_class = findViewById(R.id.status);
        money = findViewById(R.id.total);
        date = findViewById(R.id.date1);
        image = findViewById(R.id.image);
        color = findViewById(R.id.color);
        danhxung = findViewById(R.id.danhxung);
        buttonRegister = findViewById(R.id.buttonRegister);
        linear = findViewById(R.id.linear);
        linear1 = findViewById(R.id.linear1);
        date_learn = findViewById(R.id.date_learn);

        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Thông tin đai");
        myContentE.apply();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();

        PaymentAPI apiService = APIServicePayment.getPaymentApiService();


        if (extras != null) {
            // Lấy dữ liệu từ Bundle
             idBelt = extras.getInt("id");
             idCurrent = extras.getInt("current");



         //   Toast.makeText(getApplicationContext(),idBelt+"",Toast.LENGTH_SHORT).show();
        }
        Call<List<DetailsBelt>> callBell = apiService.getBeltInfo(idBelt);
        callBell.enqueue(new Callback<List<DetailsBelt>>() {
            @Override
            public void onResponse(Call<List<DetailsBelt>> call, Response<List<DetailsBelt>> response) {
                   if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"true",Toast.LENGTH_SHORT).show();
                    List<DetailsBelt> list = response.body();
                       DetailsBelt  de = list.get(0);
                    if(idBelt  > idCurrent+1 || idBelt <= idCurrent){
                        buttonRegister.setVisibility(View.GONE);
                        linear.setVisibility(View.GONE);
                        linear1.setVisibility(View.GONE);
                        if(idBelt >idCurrent+1 ){
                            date_learn.setText("Tình trạng: chưa thi");
                        }else {
                            date_learn.setText("Tình trạng: đã thi");
                        }
                    }else {

                        buttonRegister.setVisibility(View.VISIBLE);
                        linear.setVisibility(View.VISIBLE);
                        linear1.setVisibility(View.VISIBLE);

                        money.setText(idBelt*200000+ "VND");
                        date.setText(" chờ thông báo về email");
                        date_learn.setText("Tình trạng: chưa thi");

                    }
                    status_class.setText(de.getMoTa());
                    danhxung.setText("Danh xưng:\t " + de.getDanhXung());
                    color.setText("Màu đai:\t " + de.getColer());
                    change_class.setText("Thông tin đai " + de.getTen());
                    Picasso.get().load(de.getLink()).placeholder(R.drawable.photo3x4).error(R.drawable.photo3x4).into(image);


                }else {
                    Toast.makeText(getApplicationContext(),"fail nn",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<DetailsBelt>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"fail nn",Toast.LENGTH_SHORT).show();
            }
        });


    }
}