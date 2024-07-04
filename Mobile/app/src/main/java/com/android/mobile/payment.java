package com.android.mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class payment extends AppCompatActivity {
    private FrameLayout frameLayout;
    private ImageView imageView;
    private View borderView;
    private Button payment;
    private ImageView sub_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        frameLayout = findViewById(R.id.frameLayout);
        imageView = findViewById(R.id.payment_momo);
        borderView = findViewById(R.id.borderView);
        payment = findViewById(R.id.payment);
        onImageViewClicked(imageView);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PaymentQR.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        //chèn fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

// Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();
    }
    public void onImageViewClicked(ImageView view) {
        // Kiểm tra nếu imageView đã có border thì loại bỏ, ngược lại thêm border
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (borderView.getVisibility() == View.INVISIBLE) {
                    borderView.setVisibility(View.VISIBLE);
                } else {
                    borderView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
/*    public void GetEventFrament(){
        titleFragment myFragment = (titleFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        sub_menu = myFragment.getView().findViewById(R.id.img_menu);
        sub_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"abc",Toast.LENGTH_SHORT).show();
            }
        });
    }*/

}