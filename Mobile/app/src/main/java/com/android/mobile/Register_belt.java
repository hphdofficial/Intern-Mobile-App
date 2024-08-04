package com.android.mobile;

import android.content.Context;
import android.content.Intent;
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
import com.android.mobile.adapter.Chapter_adapter;
import com.android.mobile.models.Belt;
import com.android.mobile.models.Chapter;

import java.util.ArrayList;

public class Register_belt extends BaseActivity {


    ArrayList<Belt> chapters = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        chapters.add(new Belt("Đai trắng","Đã học"));
        chapters.add(new Belt("Đai đen","Đã học"));

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");




        Adapter_Register_belt chapterAdapter = new Adapter_Register_belt(this, chapters);
        RecyclerView recyclerView = findViewById(R.id.chapter_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chapterAdapter);

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
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();
    }
}