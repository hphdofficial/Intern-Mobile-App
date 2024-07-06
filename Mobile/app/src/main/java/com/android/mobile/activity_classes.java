package com.android.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.adapter.Class_adapter;
import com.android.mobile.adapter.Club_adapter;
import com.android.mobile.models.Class;

import java.util.ArrayList;
import java.util.Date;

public class activity_classes extends AppCompatActivity {
    Date today = new Date();
    Class class1 = new Class(400000, today, today, "Tự vệ", 3, "Hữu lợi");
    Class class2 = new Class(400000, today, today, "Tự vệ 1", 3, "Hữu lợi");
    Class class3 = new Class(400000, today, today, "Tự vệ 2", 6, "Hữu lợi");
    Class class4 = new Class(400000, today, today, "Tự vệ 3", 6, "Hữu lợi");
    ArrayList<Class> classes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_classes);

        classes.add(class1);
        classes.add(class2);
        classes.add(class3);
        classes.add(class4);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        Class_adapter classAdapter = new Class_adapter(this, classes);
        RecyclerView recyclerView = findViewById(R.id.recycler_class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(classAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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