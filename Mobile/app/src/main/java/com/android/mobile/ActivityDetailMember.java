package com.android.mobile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.IOException;

public class ActivityDetailMember extends AppCompatActivity {

    private static final int REQUEST_CODE_GALLERY = 100;
    private static final int REQUEST_CODE_CAMERA = 101;
    private ImageView imageViewAvatar; // ImageView for the avatar
    private Uri currentImageUri; // Current URI for the image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_member);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        imageViewAvatar.setOnClickListener(this::showPopupMenu);
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

//    public void showPopupMenu(View v) {
//        PopupMenu popupMenu = new PopupMenu(this, v);
//        popupMenu.setOnMenuItemClickListener(item -> {
//            int itemId = item.getItemId();
//            if (itemId == R.id.menu_view_image) {
//                if (currentImageUri != null) {
//                    Intent viewIntent = new Intent(ActivityDetailMember.this, ViewImageActivity.class);
//                    viewIntent.putExtra("imageUri", currentImageUri.toString());
//                    startActivity(viewIntent);
//                } else {
//                    Toast.makeText(ActivityDetailMember.this, "No image to view", Toast.LENGTH_SHORT).show();
//                }
//                return true;
//            } else if (itemId == R.id.menu_replace_gallery) {
//                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
//                return true;
//            } else if (itemId == R.id.menu_replace_camera) {
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
//                return true;
//            } else {
//                return false;
//            }
//        });
//        popupMenu.inflate(R.menu.image_profile_menu);
//        popupMenu.show();
//    }
public void showPopupMenu(View v) {
    PopupMenu popupMenu = new PopupMenu(this, v);
    popupMenu.setOnMenuItemClickListener(item -> {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_view_image) {
            Intent viewIntent = new Intent(ActivityDetailMember.this, ViewImageActivity.class);
            if (currentImageUri != null) {
                viewIntent.putExtra("imageUri", currentImageUri.toString());
            } else {
                viewIntent.putExtra("defaultImage", true);
            }
            startActivity(viewIntent);
            return true;
        } else if (itemId == R.id.menu_replace_gallery) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
            return true;
        } else if (itemId == R.id.menu_replace_camera) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
            return true;
        } else {
            return false;
        }
    });
    popupMenu.inflate(R.menu.image_profile_menu);
    popupMenu.show();
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_GALLERY && data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    currentImageUri = selectedImageUri;
                    imageViewAvatar.setImageURI(selectedImageUri); // Hiển thị ảnh trong ImageView
                }
                Intent intent = new Intent(ActivityDetailMember.this, ImageSelectActivity.class);
                intent.putExtra("imageUri", selectedImageUri.toString());
                startActivity(intent);
            } else if (requestCode == REQUEST_CODE_CAMERA && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                if (imageBitmap != null) {
                    currentImageUri = getImageUriFromBitmap(imageBitmap);
                    imageViewAvatar.setImageBitmap(imageBitmap); // Hiển thị ảnh trong ImageView
                }
                Intent intent = new Intent(ActivityDetailMember.this, ActivityReplaceCamera.class);
                intent.putExtra("data", imageBitmap);
                startActivity(intent);
            }
        }
    }

    // Phương thức để chuyển Bitmap thành Uri
    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

}
