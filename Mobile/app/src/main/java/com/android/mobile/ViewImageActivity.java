package com.android.mobile;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ViewImageActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        imageView = findViewById(R.id.imageView);

        boolean useDefaultImage = getIntent().getBooleanExtra("defaultImage", false);
        if (useDefaultImage) {
            imageView.setImageResource(R.drawable.photo3x4);
        } else {
            String imageUriString = getIntent().getStringExtra("imageUri");
            if (imageUriString != null) {
                Picasso.get().load(imageUriString).into(imageView);
            }
        }
    }
}
