package com.android.mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;

public class NewsDetailActivity extends AppCompatActivity {

    private TextView newsTitle;
    private WebView newsContent;
    private ImageView newsImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Chi tiết tin tức");
        myContentE.apply();

        // chèn fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();

        newsTitle = findViewById(R.id.news_title);
        newsContent = findViewById(R.id.news_content);
        newsImage = findViewById(R.id.news_image);

        String title = getIntent().getStringExtra("NewsTitle");
        String content = getIntent().getStringExtra("NewsContent");
        String imageUrl = getIntent().getStringExtra("NewsImage");

        newsTitle.setText(title);

        // Decode HTML content
        String decodedContent = Html.fromHtml(content).toString();

        // Add some basic CSS to make the content look better
        String css = "<style> img{display: inline;height: auto;max-width: 100%;} body {font-size: 16px; line-height: 1.6;} </style>";

        // Combine the CSS with the decoded HTML content
        String htmlContent = "<html><head>" + css + "</head><body>" + decodedContent + "</body></html>";

        // Configure WebView settings
        WebSettings webSettings = newsContent.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Load the HTML content into WebView using loadDataWithBaseURL
        newsContent.setWebViewClient(new WebViewClient());
        newsContent.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null);

        // Load the image using Glide
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background) // Placeholder image
                .error(R.drawable.ic_launcher_foreground) // Error image
                .into(newsImage);
    }
}
