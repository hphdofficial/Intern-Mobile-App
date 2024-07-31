package com.android.mobile;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class NewsDetailActivity extends AppCompatActivity {

    private TextView newsTitle;
    private WebView newsContent;
    private ImageView newsImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        newsTitle = findViewById(R.id.news_title);
        newsContent = findViewById(R.id.news_content);
        newsImage = findViewById(R.id.news_image);

        String title = getIntent().getStringExtra("NewsTitle");
        String content = getIntent().getStringExtra("NewsContent");
        String imageUrl = getIntent().getStringExtra("NewsImage");

        newsTitle.setText(title);

        // Configure WebView settings
        WebSettings webSettings = newsContent.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Load the HTML content into WebView using loadDataWithBaseURL
        newsContent.setWebViewClient(new WebViewClient());
        newsContent.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background) // Placeholder image
                .error(R.drawable.ic_launcher_foreground) // Error image
                .into(newsImage);
    }
}
