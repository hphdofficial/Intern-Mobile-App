package com.android.mobile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.mobile.models.ProfileModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.UserApiService;
import com.squareup.picasso.Picasso;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDetailMember extends AppCompatActivity {

    private TextView textViewUsernameValue, textViewTenValue, textViewEmailValue, textViewDienthoaiValue, textViewDiachiValue, textViewGioitinhValue, textViewNgaysinhValue, textViewLastloginValue, textViewHotengiamhoValue, textViewDienthoaiGiamhoValue,textViewChieucaoValue, textViewCannangValue;;
    private ImageView imageViewAvatar;
    private SharedPreferences sharedPreferences;
    private static final String NAME_SHARED = "login_prefs";
    private static final int REQUEST_CODE_GALLERY = 100;
    private static final int REQUEST_CODE_CAMERA = 101;
    private static final int REQUEST_CODE_SELECT_IMAGE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_member);

        // chèn fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(NAME_SHARED, MODE_PRIVATE);

        // Initialize TextViews
        textViewUsernameValue = findViewById(R.id.textViewUsernameValue);
        textViewTenValue = findViewById(R.id.textViewTenValue);
        textViewEmailValue = findViewById(R.id.textViewEmailValue);
        textViewDienthoaiValue = findViewById(R.id.textViewDienthoaiValue);
        textViewDiachiValue = findViewById(R.id.textViewDiachiValue);
        textViewGioitinhValue = findViewById(R.id.textViewGioitinhValue);
        textViewNgaysinhValue = findViewById(R.id.textViewNgaysinhValue);
        textViewLastloginValue = findViewById(R.id.textViewLastloginValue);
        textViewHotengiamhoValue = findViewById(R.id.textViewHotengiamhoValue);
        textViewChieucaoValue = findViewById(R.id.textViewChieucaoValue);
        textViewCannangValue = findViewById(R.id.textViewCannangValue);
        textViewDienthoaiGiamhoValue = findViewById(R.id.textViewDienthoaiGiamhoValue);
        imageViewAvatar = findViewById(R.id.imageViewAvatar);

        imageViewAvatar.setOnClickListener(this::showPopupMenu);

        // Fetch profile information
        fetchProfileInformation();

        // Load avatar for the user
        loadUserAvatar();
    }

    private void fetchProfileInformation() {
        String token = sharedPreferences.getString("access_token", null);
        int memberId = sharedPreferences.getInt("member_id", -1); // Lấy member_id từ SharedPreferences
        if (token != null && memberId != -1) {
            UserApiService apiService = ApiServiceProvider.getUserApiService();
            Call<ProfileModel> call = apiService.getProfile("Bearer " + token);
            call.enqueue(new Callback<ProfileModel>() {
                @Override
                public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                    if (response.isSuccessful()) {
                        ProfileModel profile = response.body();
                        if (profile != null) {
                            textViewUsernameValue.setText(profile.getUsername());
                            textViewTenValue.setText(profile.getTen());
                            textViewEmailValue.setText(profile.getEmail());
                            textViewDienthoaiValue.setText(profile.getDienthoai());
                            textViewDiachiValue.setText(profile.getDiachi());
                            textViewGioitinhValue.setText(profile.getGioitinh());
                            textViewNgaysinhValue.setText(profile.getNgaysinh());
                            textViewLastloginValue.setText(profile.getLastlogin());
                            textViewHotengiamhoValue.setText(profile.getHotengiamho());
                            textViewDienthoaiGiamhoValue.setText(profile.getDienthoai_giamho());
                            textViewChieucaoValue.setText(String.valueOf(profile.getChieucao()));
                            textViewCannangValue.setText(String.valueOf(profile.getCannang()));
                        }
                    } else {
                        Toast.makeText(ActivityDetailMember.this, "Không thể lấy thông tin cá nhân", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProfileModel> call, Throwable t) {
                    Toast.makeText(ActivityDetailMember.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserAvatar() {
        int memberId = sharedPreferences.getInt("member_id", -1);
        if (memberId != -1) {
            String avatarUrl = sharedPreferences.getString("avatar_url_" + memberId, null);
            if (avatarUrl != null) {
                Picasso.get().load(avatarUrl).placeholder(R.drawable.photo3x4).error(R.drawable.photo3x4).into(imageViewAvatar);
            } else {
                imageViewAvatar.setImageResource(R.drawable.photo3x4); // Ảnh mặc định
            }
        }
    }

    public void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_view_image) {
                Intent viewIntent = new Intent(ActivityDetailMember.this, ViewImageActivity.class);
                String avatarUrl = sharedPreferences.getString("avatar_url_" + sharedPreferences.getInt("member_id", -1), null);
                if (avatarUrl != null) {
                    viewIntent.putExtra("imageUri", avatarUrl);
                } else {
                    viewIntent.putExtra("defaultImage", true);
                }
                startActivity(viewIntent);
                return true;
            } else if (itemId == R.id.menu_replace_gallery) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
                return true;
            } else if (itemId == R.id.menu_replace_camera) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                    Intent intent = new Intent(ActivityDetailMember.this, ImageSelectActivity.class);
                    intent.putExtra("imageUri", selectedImageUri.toString());
                    startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
                }
            } else if (requestCode == REQUEST_CODE_CAMERA && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                if (imageBitmap != null) {
                    Uri imageUri = getImageUriFromBitmap(imageBitmap);
                    Intent intent = new Intent(ActivityDetailMember.this, ImageSelectActivity.class);
                    intent.putExtra("imageUri", imageUri.toString());
                    startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
                }
            } else if (requestCode == REQUEST_CODE_SELECT_IMAGE && data != null) {
                String avatarUrl = data.getStringExtra("avatarUrl");
                if (avatarUrl != null) {
                    Picasso.get().load(avatarUrl).placeholder(R.drawable.photo3x4).error(R.drawable.photo3x4).into(imageViewAvatar);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    int memberId = sharedPreferences.getInt("member_id", -1); // Lấy member_id từ SharedPreferences
                    editor.putString("avatar_url_" + memberId, avatarUrl);
                    editor.apply();
                }
            }
        }
    }

    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }
}
