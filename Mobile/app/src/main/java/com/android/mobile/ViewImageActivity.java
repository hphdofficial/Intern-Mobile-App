package com.android.mobile;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewImageActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        imageView = findViewById(R.id.imageView);

        // Check if the default image flag is passed
        boolean useDefaultImage = getIntent().getBooleanExtra("defaultImage", false);
        if (useDefaultImage) {
            imageView.setImageResource(R.drawable.imgprofile);
        } else {
            // Get the image URI from the intent
            String imageUriString = getIntent().getStringExtra("imageUri");
            if (imageUriString != null) {
                Uri imageUri = Uri.parse(imageUriString);
                imageView.setImageURI(imageUri);
            }
        }
    }

}
