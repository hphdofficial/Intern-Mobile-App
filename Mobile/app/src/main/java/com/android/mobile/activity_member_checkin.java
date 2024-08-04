package com.android.mobile;

import android.os.Bundle;
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

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.adapter.Checked_adapter;
import com.android.mobile.models.Checkin;

import java.util.ArrayList;
import java.util.Date;

public class activity_member_checkin extends BaseActivity {
    private int count = 0;
    Date today = new Date();
    Checkin checkin = new Checkin(true, today);
    Checkin checkin1 = new Checkin(false, today);
    Checkin checkin2 = new Checkin(false, today);
    Checkin checkin3 = new Checkin(true, today);
    Checkin checkin4 = new Checkin(true, today);
    ArrayList<Checkin> checkins = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_member_checkin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        checkins.add(checkin);
        checkins.add(checkin1);
        checkins.add(checkin2);
        checkins.add(checkin3);
        checkins.add(checkin4);

        for (Checkin check: checkins){
            if(check.isHienDien()){
                count++;
            }
        }
        TextView txtSoNgayHienDien = findViewById(R.id.txtCheckin);
        txtSoNgayHienDien.setText(count + " Ngày");

        Checked_adapter checkedAdapter = new Checked_adapter(checkins, this);
        RecyclerView recyclerView = findViewById(R.id.recycler_checkin);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(checkedAdapter);

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