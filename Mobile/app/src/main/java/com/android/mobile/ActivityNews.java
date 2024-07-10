package com.android.mobile;

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

import com.android.mobile.adapter.NewsAdapter;
import com.android.mobile.models.NewsModel;

import java.util.ArrayList;
import java.util.List;

public class ActivityNews extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news);

        RecyclerView recyclerView = findViewById(R.id.itemNews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<NewsModel> newsList = new ArrayList<>();
        newsList.add(new NewsModel("Bài viết 1: Tin tức mới", R.drawable.imagenews));
        newsList.add(new NewsModel("Bài viết 2: Võ Vovinam", R.drawable.imagenews));
        newsList.add(new NewsModel("Bài viết 2: Võ Vovinam", R.drawable.imagenews));
        newsList.add(new NewsModel("Bài viết 2: Võ Vovinam", R.drawable.imagenews));

        NewsAdapter adapter = new NewsAdapter(newsList);
        recyclerView.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        // chèn fragment
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
