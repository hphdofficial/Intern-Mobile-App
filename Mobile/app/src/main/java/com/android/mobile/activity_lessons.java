package com.android.mobile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.mobile.adapter.lesson_adapter;
import com.android.mobile.models.Lesson;

import java.util.ArrayList;

public class activity_lessons extends AppCompatActivity {

    Lesson lesson1 = new Lesson("Tự vệ", "Võ sinh", "Đai màu võ phục Vovinam", "3 tháng");
    Lesson lesson2 = new Lesson("Nhập môn", "Võ sinh", "Đai màu võ phục Vovinam", "3 tháng");
    Lesson lesson3 = new Lesson("Lam đai đệ nhất cấp", "Võ sinh", "Đai màu võ phục Vovinam", "3 tháng");
    Lesson lesson4 = new Lesson("Lam đai đệ nhị cấp", "Môn sinh", "Đai màu võ phục Vovinam", "3 tháng");
    Lesson lesson5 = new Lesson("Lam đai đệ tam cấp", "Võ sinh", "Đai màu võ phục Vovinam", "6 tháng");
    ArrayList<Lesson> lessons = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);


        lessons.add(lesson1);
        lessons.add(lesson2);
        lessons.add(lesson3);
        lessons.add(lesson4);
        lessons.add(lesson5);


        lesson_adapter lesson_adapter = new lesson_adapter(this, lessons);
        RecyclerView recyclerView = findViewById(R.id.lesson_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(lesson_adapter);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

// Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();
    }
}