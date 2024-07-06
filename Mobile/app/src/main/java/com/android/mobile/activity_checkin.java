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

import com.android.mobile.adapter.Checkin_adapter;
import com.android.mobile.models.Member;

import java.util.ArrayList;

public class activity_checkin extends AppCompatActivity {
    Member member1 = new Member("Lam cấp", "000", "Thuy");
    Member member2 = new Member("Lam cấp", "001", "Thuy 1");
    Member member3 = new Member("Lam cấp", "002", "Thuy 2");
    Member member4 = new Member("Lam cấp", "003", "Thuy 3");
    Member member5 = new Member("Lam cấp", "004", "Thuy 4");
    ArrayList<Member> members = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkin);

        members.add(member1);
        members.add(member2);
        members.add(member3);
        members.add(member4);
        members.add(member5);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        Checkin_adapter checkinAdapter = new Checkin_adapter(this, members);
        RecyclerView recyclerView = findViewById(R.id.recycler_checkin);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(checkinAdapter);

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