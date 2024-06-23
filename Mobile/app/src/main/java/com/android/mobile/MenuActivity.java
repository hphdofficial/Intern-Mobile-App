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

    private ImageView img_menu;
    private ImageView img_language;
    private TextView txt_title;
    private ImageView imageViewAvatar;
    private TextView textViewName;
    private TextView txt_content;
    private BottomNavigationView bottomNavigationView;
    LinearLayout sub_menu;
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
        img_menu = findViewById(R.id.img_menu);
        img_language = findViewById(R.id.img_language);
        txt_title = findViewById(R.id.txt_title);
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        textViewName = findViewById(R.id.textViewName);
        txt_content = findViewById(R.id.txt_content);
        sub_menu = findViewById(R.id.sub_menu);
        camera = findViewById(R.id.camera);
        language = findViewById(R.id.language);
        main = findViewById(R.id.main);
        CreateFracmentSubMenu();
        restartView();
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartView();
            }
        });
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub_menu.setVisibility(View.VISIBLE);
            }
        });
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
        img_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language.setVisibility(View.GONE);
            }
        });
    }
    public void CreateFracmentSubMenu(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new sub_menu())
                .commit();
        sub_menu.setVisibility(View.GONE);

    }
    public void CreateCamera(){
        camera.setVisibility(View.GONE);
    }
    public void restartView(){
        sub_menu.setVisibility(View.GONE);
        camera.setVisibility(View.GONE);
        language.setVisibility(View.GONE);

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