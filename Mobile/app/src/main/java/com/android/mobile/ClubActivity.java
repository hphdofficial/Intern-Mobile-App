package com.android.mobile;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.adapter.Club_adapter;
import com.android.mobile.models.Club;

import java.util.ArrayList;

public class ClubActivity extends AppCompatActivity {
    Club club1 = new Club("Trần Hưng Đạo, Q10, TPHCM", "Trần Hưng Đạo");
    Club club2 = new Club("Trần Hưng Đạo, Q12, TPHCM", "Trần Hưng Đạo 1");
    Club club3 = new Club("Trần Hưng Đạo, Q3, TPHCM", "Trần Hưng Đạo 2");
    Club club4 = new Club("Trần Hưng Đạo, Q2, TPHCM", "Trần Hưng Đạo 3");
    ArrayList<Club> clubs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_club);
        clubs.add(club1);
        clubs.add(club2);
        clubs.add(club3);
        clubs.add(club4);

        Club_adapter clubAdapter = new Club_adapter(this, clubs);
        RecyclerView recyclerView = findViewById(R.id.recycler_location);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(clubAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
}