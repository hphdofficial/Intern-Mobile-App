package com.android.mobile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.models.ClassModelTest;
import com.android.mobile.models.ProfileModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.UserApiService;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDetailMember extends BaseActivity {

    private TextView textViewUsernameValue, textViewTenValue, textViewEmailValue, textViewDienthoaiValue, textViewDiachiValue, textViewGioitinhValue, textViewNgaysinhValue, textViewLastloginValue, textViewHotengiamhoValue, textViewDienthoaiGiamhoValue, textViewChieucaoValue, textViewCannangValue, classInfo;
    private ImageView imageViewAvatar;
    private Button buttonEditPassword, buttonEditInfo;
    private SharedPreferences sharedPreferences;
    private static final String NAME_SHARED = "login_prefs";
    private static final int REQUEST_CODE_GALLERY = 100;
    private static final int REQUEST_CODE_CAMERA = 101;
    private static final int REQUEST_CODE_SELECT_IMAGE = 102;
    private static final int REQUEST_CODE_CAMERA_PERMISSION = 200;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_member);

        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Thông tin thành viên");
        myContentE.apply();

        // Chèn fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(NAME_SHARED, MODE_PRIVATE);

        // Initialize Views
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
        buttonEditPassword = findViewById(R.id.button_edit_password);
        buttonEditInfo = findViewById(R.id.button_edit_info);

        classInfo = findViewById(R.id.classInfo);

        imageViewAvatar.setOnClickListener(this::showPopupMenu);

        buttonEditPassword.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityDetailMember.this, UpdatePassword.class);
            intent.putExtra("email", textViewEmailValue.getText().toString());
            startActivity(intent);
        });
        buttonEditInfo.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityDetailMember.this, UpdateInfoMember.class);
            intent.putExtra("username", textViewUsernameValue.getText().toString());
            intent.putExtra("email", textViewEmailValue.getText().toString());
            intent.putExtra("ten", textViewTenValue.getText().toString());
            intent.putExtra("dienthoai", textViewDienthoaiValue.getText().toString());
            intent.putExtra("diachi", textViewDiachiValue.getText().toString());
            intent.putExtra("gioitinh", textViewGioitinhValue.getText().toString());
            intent.putExtra("ngaysinh", textViewNgaysinhValue.getText().toString());
            intent.putExtra("hotengiamho", textViewHotengiamhoValue.getText().toString());
            intent.putExtra("dienthoaigiamho", textViewDienthoaiGiamhoValue.getText().toString());
            intent.putExtra("chieucao", textViewChieucaoValue.getText().toString());
            intent.putExtra("cannang", textViewCannangValue.getText().toString());
            startActivity(intent);
        });

        // Fetch profile information
        fetchProfileInformation();
        // Fetch class information
    }

    private void fetchClassInformation() {
        String token = sharedPreferences.getString("access_token", null);
        if (token != null) {
            UserApiService apiService = ApiServiceProvider.getUserApiService();
            Call<List<ClassModelTest>> call = apiService.getUserRegisteredClasses("Bearer " + token);
            call.enqueue(new Callback<List<ClassModelTest>>() {
                @Override
                public void onResponse(Call<List<ClassModelTest>> call, Response<List<ClassModelTest>> response) {
                    if (response.isSuccessful()) {
                        List<ClassModelTest> classes = response.body();
                        if (classes != null && !classes.isEmpty()) {
                            ClassModelTest classModel = classes.get(0);
                            String classDetails = "Tên: " + classModel.getTen() + "\n" +
                                    "Thời gian: " + classModel.getThoigian() + "\n" +
                                    "Giá tiền: " + classModel.getGiatien() + "\n" +
                                    "Điện thoại: " + classModel.getDienthoai() + "\n" +
                                    "CLB: " + classModel.getClub() + "\n" +
                                    "Giảng viên: " + classModel.getGiangvien();
                            classInfo.setText(classDetails);
                        } else {
                            classInfo.setText("Bạn chưa đăng ký khóa học nào.");
                        }
                    } else {
                        classInfo.setText("Bạn chưa đăng ký khóa học nào.");
                    }
                }

                @Override
                public void onFailure(Call<List<ClassModelTest>> call, Throwable t) {
                    classInfo.setText("Bạn chưa đăng ký khóa học nào.");
                }
            });
        } else {
            classInfo.setText("Chưa đăng nhập");
        }
    }


    private void showPopupMenu(View v) {
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
                requestStoragePermission();
                return true;
            } else if (itemId == R.id.menu_replace_camera) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
                } else {
                    openCamera();
                }
                return true;
            } else {
                return false;
            }
        });
        popupMenu.inflate(R.menu.image_profile_menu);
        popupMenu.show();
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Storage permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchProfileInformation() {
        String token = sharedPreferences.getString("access_token", null);
        int memberId = sharedPreferences.getInt("member_id", -1);
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
                            textViewChieucaoValue.setText(profile.getChieucao());
                            textViewCannangValue.setText(profile.getCannang());
                            String avatarUrl = profile.getAvatarUrl();
                            if (avatarUrl != null) {
                                Picasso.get().load(avatarUrl).placeholder(R.drawable.photo3x4).error(R.drawable.photo3x4).into(imageViewAvatar);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("avatar_url_" + memberId, avatarUrl);
                                editor.apply();
                            }
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
                    int memberId = sharedPreferences.getInt("member_id", -1);
                    editor.putString("avatar_url_" + memberId, avatarUrl);
                    editor.apply();
                }
            }
        }
    }

    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Fetch profile information mỗi khi Activity được hiển thị lại
        fetchProfileInformation();
        fetchClassInformation();
    }

}