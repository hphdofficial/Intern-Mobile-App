package com.android.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.models.ProductModel;
import com.android.mobile.models.TheoryModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.ProductApiService;
import com.android.mobile.services.TheoryApiService;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.ui.PlayerView;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_item_chapter extends BaseActivity {
    private BlankFragment loadingFragment;
    private ExoPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_chapter);

        Intent intent = getIntent();
        int idTheory = intent.getIntExtra("id", -1);

        TextView txtTheoryTittle = findViewById(R.id.txtTheoryTitle);
        TextView txtTheoryContent = findViewById(R.id.txtTheoryContent);



        //Fetch thông tin lý thuyết
        showLoading();
        TheoryApiService apiService = ApiServiceProvider.getTheoryApiService();
        apiService.getMartialArtsTheoryDetail(idTheory).enqueue(new Callback<TheoryModel>() {
            @Override
            public void onResponse(Call<TheoryModel> call, Response<TheoryModel> response) {
                if(response.isSuccessful()){
                    TheoryModel theory = response.body();
                    txtTheoryTittle.setText(theory.getTenvi());
                    txtTheoryContent.setText("Bài tập gồm: "+theory.getNoidungvi());
                    String videoPath = theory.getLink_video();

                    player = new ExoPlayer.Builder(getApplicationContext()).build();
                    PlayerView playerView = findViewById(R.id.webView);
                    playerView.setPlayer(player);

                    Uri uri = Uri.parse(videoPath);
                    MediaItem mediaItem = MediaItem.fromUri(uri);
                    player.setMediaItem(mediaItem);
                    player.prepare();
                    player.play();

                    hideLoading();
                }else {
                    hideLoading();
                    Toast.makeText(activity_item_chapter.this, "Lý thuyết hiện không khả dụng", Toast.LENGTH_SHORT).show();
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<TheoryModel> call, Throwable throwable) {
                hideLoading();
                Toast.makeText(activity_item_chapter.this, "Lỗi kết nối, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + throwable.getMessage());
            }
        });


        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Bài giảng lý thuyết");
        myContentE.apply();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

// Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
//        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();
    }

    private void showLoading() {
        loadingFragment = new BlankFragment();
        loadingFragment.show(getSupportFragmentManager(), "loading");
    }
    private void hideLoading() {
        if (loadingFragment != null) {
            loadingFragment.dismiss();
            loadingFragment = null;
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        player.setPlayWhenReady(false);
        player.release();
        player = null;
    }

}