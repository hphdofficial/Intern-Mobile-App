package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import com.bumptech.glide.Glide;

public class NewsDetailActivity extends BaseActivity {

    private TextView newsTitle;
    private WebView newsContent;
    private ImageView newsImage;
    private String languageS;
    private TextView text;
    public void onMenuItemClick(View view) {
        text = findViewById(R.id.languageText);
        String language = text.getText()+"";
        if(view.getId() == R.id.btn_change){
            SharedPreferences sga = getSharedPreferences("login_prefs", MODE_PRIVATE);
            SharedPreferences.Editor edit =  sga.edit();

            if(language.contains("VN")){
                edit.putString("language","en");
                text.setText("ENG");
            }else {
                edit.putString("language","vn");
                text.setText("VN");
            }
            edit.apply();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Chi tiết tin tức");
        myContentE.apply();
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language",null);
        if(languageS!= null){
            if(languageS.contains("en")){
                myContentE.putString("title", "News Detail");
                myContentE.apply();
            }
        }
        // chèn fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
//        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
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
                .placeholder(R.drawable.ic_launcher_foreground) // Placeholder image
                .error(R.drawable.newsvovietdao) // Error image
                .into(newsImage);
    }
}
