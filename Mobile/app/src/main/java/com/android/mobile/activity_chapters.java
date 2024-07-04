package com.android.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.mobile.adapter.Chapter_adapter;
import com.android.mobile.models.Chapter;

import java.util.ArrayList;

public class activity_chapters extends AppCompatActivity {
    Chapter chapter1 = new Chapter("Khởi quyền", 100, "Bài tập gồm bộ: đấm thẳng, chém số 1, 2, 3, đá thẳng, song móc, song múc, song đầm, chỏ số 1, 2", "");
    Chapter chapter2 = new Chapter("Bóp cổ trước lối 1 và 2", 100, "Bài tập gồm bộ: đấm thẳng, chém số 1, 2, 3, đá thẳng, song móc, song múc, song đầm, chỏ số 1, 2", "");
    Chapter chapter3 = new Chapter("Bóp cổ trước lối 1 và 2", 100, "Bài tập gồm bộ: đấm thẳng, chém số 1, 2, 3, đá thẳng, song móc, song múc, song đầm, chỏ số 1, 2", "");
    Chapter chapter4 = new Chapter("Bóp cổ trước lối 1 và 2", 100, "Bài tập gồm bộ: đấm thẳng, chém số 1, 2, 3, đá thẳng, song móc, song múc, song đầm, chỏ số 1, 2", "");
    Chapter chapter5 = new Chapter("Bóp cổ trước lối 1 và 2", 100, "Bài tập gồm bộ: đấm thẳng, chém số 1, 2, 3, đá thẳng, song móc, song múc, song đầm, chỏ số 1, 2", "");
    Chapter chapter6 = new Chapter("Bóp cổ trước lối 1 và 2", 100, "Bài tập gồm bộ: đấm thẳng, chém số 1, 2, 3, đá thẳng, song móc, song múc, song đầm, chỏ số 1, 2", "");
    Chapter chapter7 = new Chapter("Bóp cổ trước lối 1 và 2", 100, "Bài tập gồm bộ: đấm thẳng, chém số 1, 2, 3, đá thẳng, song móc, song múc, song đầm, chỏ số 1, 2", "");
    Chapter chapter8 = new Chapter("Bóp cổ trước lối 1 và 2", 100, "Bài tập gồm bộ: đấm thẳng, chém số 1, 2, 3, đá thẳng, song móc, song múc, song đầm, chỏ số 1, 2", "");
    ArrayList<Chapter> chapters = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        chapters.add(chapter1);
        chapters.add(chapter2);
        chapters.add(chapter3);
        chapters.add(chapter4);
        chapters.add(chapter5);
        chapters.add(chapter6);
        chapters.add(chapter7);
        chapters.add(chapter8);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText(title);

        ImageButton btnBack = findViewById(R.id.back_button);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), activity_lessons.class);
                startActivity(intent1);
            }
        });



        Chapter_adapter chapterAdapter = new Chapter_adapter(this, chapters);
        RecyclerView recyclerView = findViewById(R.id.chapter_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chapterAdapter);
    }
}