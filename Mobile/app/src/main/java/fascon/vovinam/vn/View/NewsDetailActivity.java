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
        myContentE.putString("title", "Chi tiết tin tức"); // Mặc định là tiếng Việt
        myContentE.apply();

        // Lấy ngôn ngữ hiện tại từ SharedPreferences
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language", "vn"); // Mặc định là tiếng Việt

        // Cập nhật tiêu đề trang dựa trên ngôn ngữ
        if (languageS != null && languageS.contains("en")) {
            myContentE.putString("title", "News Detail");
            myContentE.apply();
        }

        // Khởi tạo các view
        newsTitle = findViewById(R.id.news_title);
        newsContent = findViewById(R.id.news_content);
        newsImage = findViewById(R.id.news_image);

        // Lấy dữ liệu từ Intent
        String titleVi = getIntent().getStringExtra("NewsTitleVi");
        String contentVi = getIntent().getStringExtra("NewsContentVi");
        String titleEn = getIntent().getStringExtra("NewsTitleEn");
        String contentEn = getIntent().getStringExtra("NewsContentEn");
        String imageUrl = getIntent().getStringExtra("NewsImage");

        // Hiển thị nội dung dựa trên ngôn ngữ đã chọn
        if (languageS != null && languageS.equals("en")) {
            // Ngôn ngữ là tiếng Anh
            newsTitle.setText(titleEn);
            setWebViewContent(newsContent, contentEn);
        } else {
            // Mặc định là tiếng Việt
            newsTitle.setText(titleVi);
            setWebViewContent(newsContent, contentVi);
        }

        // Load hình ảnh sử dụng Glide
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground) // Hình ảnh chờ
                .error(R.drawable.newsvovietdao) // Hình ảnh lỗi
                .into(newsImage);

        // Chèn fragment tiêu đề
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.commit();
    }

    // Phương thức hỗ trợ hiển thị nội dung vào WebView
    private void setWebViewContent(WebView webView, String content) {
        if (content != null && !content.isEmpty()) {
            // Giải mã nội dung HTML
            String decodedContent = Html.fromHtml(content).toString();
            // Thêm CSS cơ bản để cải thiện giao diện nội dung
            String css = "<style> img{display: inline;height: auto;max-width: 100%;} body {font-size: 16px; line-height: 1.6;} </style>";
            String htmlContent = "<html><head>" + css + "</head><body>" + decodedContent + "</body></html>";

            // Cấu hình WebView
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            // Load nội dung HTML vào WebView
            webView.setWebViewClient(new WebViewClient());
            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null);
        }
    }

}
