package com.android.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class activity_item_chapter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_chapter);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        ImageButton btnBack = findViewById(R.id.back_button);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), activity_chapters.class);
                startActivity(intent1);
            }
        });

        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText(title);
    }
}