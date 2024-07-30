
package com.android.mobile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.mobile.models.ReponseModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.UserApiService;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageSelectActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 101;
    private ImageView selectedImageView;
    private Uri selectedImageUri;
    private SharedPreferences sharedPreferences;
    private static final String NAME_SHARED = "login_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        selectedImageView = findViewById(R.id.selectedImage);
        Button saveButton = findViewById(R.id.saveButton);

        sharedPreferences = getSharedPreferences(NAME_SHARED, MODE_PRIVATE);

        selectedImageView.setOnClickListener(view -> openGallery());

        saveButton.setOnClickListener(view -> {
            if (selectedImageUri != null) {
                uploadImage(selectedImageUri);
            } else {
                Toast.makeText(ImageSelectActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("imageUri") != null) {
            selectedImageUri = Uri.parse(intent.getStringExtra("imageUri"));
            selectedImageView.setImageURI(selectedImageUri);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                selectedImageUri = data.getData();
                selectedImageView.setImageURI(selectedImageUri);
            }
        }
    }

    private String getRealPathFromUri(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
    }

    private void uploadImage(Uri imageUri) {
        String token = sharedPreferences.getString("access_token", null);
        int memberId = sharedPreferences.getInt("member_id", -1); // Lấy member_id từ SharedPreferences
        if (token != null && memberId != -1 && imageUri != null) {
            String filePath = getRealPathFromUri(imageUri);
            if (filePath != null) {
                File file = new File(filePath);
                RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

                UserApiService apiService = ApiServiceProvider.getUserApiService();
                Call<ReponseModel> call = apiService.uploadAvatar("Bearer " + token, body);
                call.enqueue(new Callback<ReponseModel>() {
                    @Override
                    public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ImageSelectActivity.this, "Upload thành công", Toast.LENGTH_SHORT).show();
                            String avatarUrl = response.body().getAvatar();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("avatar_url_" + memberId, avatarUrl);
                            editor.apply();

                            // Cập nhật giao diện với URL mới
                            Picasso.get().load(avatarUrl).placeholder(R.drawable.photo3x4).error(R.drawable.photo3x4).into(selectedImageView);

                            // Truyền URL về ActivityDetailMember
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("avatarUrl", avatarUrl);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        } else {
                            Toast.makeText(ImageSelectActivity.this, "Upload thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReponseModel> call, Throwable t) {
                        Toast.makeText(ImageSelectActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Không thể truy cập đường dẫn file", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
