package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;

import fascon.vovinam.vn.ViewModel.BaseActivity;

import java.io.IOException;

public class profiletest extends BaseActivity {

    private static final int REQUEST_CODE_GALLERY = 100;
    private static final int REQUEST_CODE_CAMERA = 101;

    private ImageView imageView;
    private Uri currentImageUri; // Biến để lưu trữ URI của ảnh hiện tại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiletest);
        imageView = findViewById(R.id.imageProfile);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    public void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_view_image) {
                    if (currentImageUri != null) {
                        Intent viewIntent = new Intent(profiletest.this, ViewImageActivity.class);
                        viewIntent.putExtra("imageUri", currentImageUri);
                        startActivity(viewIntent);
                    } else {
                        Toast.makeText(profiletest.this, "No image to view", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else if (itemId == R.id.menu_replace_gallery) {
                    Intent galleryIntent = new Intent(profiletest.this, ImageSelectActivity.class);
                    startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
                    return true;
                } else if (itemId == R.id.menu_replace_camera) {
                    Intent cameraIntent = new Intent(profiletest.this, ActivityReplaceCamera.class);
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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_GALLERY && data != null) {
                currentImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), currentImageUri);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CODE_CAMERA && data != null && data.getExtras() != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                currentImageUri = getImageUri(bitmap); // Chuyển đổi bitmap thành URI
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }
}
