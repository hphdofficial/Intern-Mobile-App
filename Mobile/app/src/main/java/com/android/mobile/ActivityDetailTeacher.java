package com.android.mobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ActivityDetailTeacher extends AppCompatActivity {

    private static final int REQUEST_CODE_GALLERY = 100;
    private static final int REQUEST_CODE_CAMERA = 101;
    private ImageView imageViewAvatar; // ImageView for the avatar
    private Uri currentImageUri; // Current URI for the image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_teacher);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageViewAvatar = findViewById(R.id.imageViewAvatar); // Assuming you have an imageViewAvatar ID in your XML
        imageViewAvatar.setOnClickListener(this::showPopupMenu);

//        ImageButton backButton = findViewById(R.id.backButton);
//        backButton.setOnClickListener(v -> {
//            Intent intent = new Intent(ActivityDetailTeacher.this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        });

        //chèn fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

// Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();
    }

    public void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_view_image) {
                    if (currentImageUri != null) {
                        Intent viewIntent = new Intent(ActivityDetailTeacher.this, ViewImageActivity.class);
                        viewIntent.putExtra("imageUri", currentImageUri);
                        startActivity(viewIntent);
                    } else {
                        Toast.makeText(ActivityDetailTeacher.this, "No image to view", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else if (itemId == R.id.menu_replace_gallery) {
                    Intent galleryIntent = new Intent(ActivityDetailTeacher.this, ImageSelectActivity.class);
                    startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
                    return true;
                } else if (itemId == R.id.menu_replace_camera) {
                    Intent cameraIntent = new Intent(ActivityDetailTeacher.this, ActivityReplaceCamera.class);
                    startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
                    return true;
                }
                return false;
            }
        });
        popupMenu.inflate(R.menu.image_profile_menu);
        popupMenu.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_GALLERY && data != null) {
                currentImageUri = data.getData();
                imageViewAvatar.setImageURI(currentImageUri);
            } else if (requestCode == REQUEST_CODE_CAMERA && data != null && data.getExtras() != null) {
                Bundle extras = data.getExtras();
                Uri imageUri = (Uri) extras.getParcelable("data");
                currentImageUri = imageUri;
                imageViewAvatar.setImageURI(currentImageUri);
            }
        }
    }
}
