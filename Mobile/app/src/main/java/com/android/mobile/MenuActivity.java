package com.android.mobile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {

    private ImageView imageViewAvatar;
    private TextView textViewName;
    private TextView txt_content;
    private BottomNavigationView bottomNavigationView;
    LinearLayout camera;
    LinearLayout language;
    ConstraintLayout main;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        textViewName = findViewById(R.id.textViewName);
        txt_content = findViewById(R.id.txt_content);
        camera = findViewById(R.id.camera);
        findViewById(R.id.action_item1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"abc",Toast.LENGTH_SHORT).show();
            }
        });
        imageViewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.setVisibility(View.VISIBLE);
            }
        });


        //thêm frament
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new titleFragment());
        fragmentTransaction.commit();
    }

    public void CreateCamera(){
        camera.setVisibility(View.GONE);
    }
    public void restartView(){

        camera.setVisibility(View.GONE);


    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restartView();
    }
}