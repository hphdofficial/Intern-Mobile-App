package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.Model.ProfileModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.UserApiService;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDetailMember extends BaseActivity {

    private TextView textViewUsernameValue, textViewTenValue, textViewEmailValue, textViewDienthoaiValue, textViewDiachiValue, textViewGioitinhValue, textViewNgaysinhValue, textViewLastloginValue, textViewHotengiamhoValue, textViewDienthoaiGiamhoValue, textViewChieucaoValue, textViewCannangValue, classInfo, clubInfo;
    private ImageView imageViewAvatar;
    private Button buttonEditPassword, buttonEditInfo,btnDetailClass, btnDetailClub;

    private CardView cardViewClub,classCard;

    private SharedPreferences sharedPreferences;
    private static final String NAME_SHARED = "login_prefs";
    private static final int REQUEST_CODE_GALLERY = 100;
    private static final int REQUEST_CODE_CAMERA = 101;
    private static final int REQUEST_CODE_SELECT_IMAGE = 102;
    private static final int REQUEST_CODE_CAMERA_PERMISSION = 200;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 201;

    private BlankFragment loadingFragment;
    private String languageS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_member);

        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Thông tin thành viên");
        myContentE.apply();
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language",null);
        if(languageS!= null){
            if(languageS.contains("en")){
                myContentE.putString("title", "User Information");
                myContentE.apply();
            }
        }

        // Chèn fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
//        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
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

        btnDetailClass = findViewById(R.id.btnDetailClass);
        btnDetailClub = findViewById(R.id.btnDetailClb);

        classCard = findViewById(R.id.classCard);
        cardViewClub = findViewById(R.id.clubCard);

        clubInfo = findViewById(R.id.clubInfo);
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
//        fetchClassInformation();

        showClub();
        textViewUsernameLabel = findViewById(R.id.textViewUsernameLabel);
        textViewTenLabel = findViewById(R.id.textViewTenLabel);
        textViewDienthoaiLabel = findViewById(R.id.textViewDienthoaiLabel);
        textViewDiachiLabel = findViewById(R.id.textViewDiachiLabel);
        textViewGioitinhLabel = findViewById(R.id.textViewGioitinhLabel);
        textViewNgaysinhLabel = findViewById(R.id.textViewNgaysinhLabel);
        textViewChieucaoLabel = findViewById(R.id.textViewChieucaoLabel);
        textViewCannangLabel = findViewById(R.id.textViewCannangLabel);
        textViewHotengiamhoLabel = findViewById(R.id.textViewHotengiamhoLabel);
        textViewDienthoaiGiamhoLabel = findViewById(R.id.textViewDienthoaiGiamhoLabel);
        textViewLastloginLabel = findViewById(R.id.textViewLastloginLabel);
        clubTitle = findViewById(R.id.clubTitle);
        classTitle = findViewById(R.id.classTitle);

        if(languageS != null){
            if(languageS.contains("en")){
                buttonEditInfo.setText("Edit Information");
                buttonEditPassword.setText("Edit Password");
                textViewUsernameLabel.setText("Login Name");
                textViewTenLabel.setText("Name");
                textViewDienthoaiLabel.setText("Phone");
                textViewDiachiLabel.setText("Phone");
                textViewGioitinhLabel.setText("Gender");
                textViewNgaysinhLabel.setText("Birthday");
                textViewChieucaoLabel.setText("Height");
                textViewCannangLabel.setText("Weight");
                textViewHotengiamhoLabel.setText("Guardian Name");
                textViewDienthoaiGiamhoLabel.setText("Guardian Phone");
                textViewLastloginLabel.setText("Last login");
                clubTitle.setText("My Club");
                classTitle.setText("Class Learned");
                btnDetailClub.setText("Show Club Details");
                btnDetailClass.setText("Show Class Detail");
            }
        }

    }
private TextView textViewUsernameLabel;
    private TextView textViewTenLabel;
    private TextView textViewDienthoaiLabel;
    private TextView textViewDiachiLabel;
    private TextView textViewGioitinhLabel;
    private TextView textViewNgaysinhLabel;
    private TextView textViewChieucaoLabel;
    private TextView textViewCannangLabel;
    private TextView textViewHotengiamhoLabel;
    private TextView textViewDienthoaiGiamhoLabel;
    private TextView textViewLastloginLabel;
    private TextView clubTitle;
    private TextView classTitle;


    private void showClub() {
        SharedPreferences abc = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String myClub = abc.getString("name_clb",null);
        btnDetailClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetailClassActivity.class);
                intent.putExtra("id_class", abc.getString("id_class_shared", null));
                startActivity(intent);
            }
        });
        btnDetailClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetailClubActivity.class);
                intent.putExtra("id_club", abc.getString("id_club_shared", null));
                startActivity(intent);
            }
        });
        if (myClub != null) {
            String nameClass = abc.getString("name_class",null);
            if (nameClass != null) {
                classInfo.setText("Lớp của bạn: "+nameClass);
                classCard.setVisibility(View.VISIBLE);
                if(languageS != null){
                    if(languageS.contains("en")){
                        classInfo.setText("My class: "+nameClass);
                    }
                }

            }
            else {
                classCard.setVisibility(View.GONE);
            }

            clubInfo.setText("Câu lạc bộ của bạn: "+myClub);
            if(languageS != null){
                if(languageS.contains("en")){
                    clubInfo.setText("My club: "+myClub);
                }
            }

            cardViewClub.setVisibility(View.VISIBLE);
        }
        else {
            cardViewClub.setVisibility(View.GONE);
        }
    }

//    private void fetchClassInformation() {
//        String token = sharedPreferences.getString("access_token", null);
//        if (token != null) {
//            UserApiService apiService = ApiServiceProvider.getUserApiService();
//            Call<List<ClassModelTest>> call = apiService.getUserRegisteredClasses("Bearer " + token);
//            call.enqueue(new Callback<List<ClassModelTest>>() {
//                @Override
//                public void onResponse(Call<List<ClassModelTest>> call, Response<List<ClassModelTest>> response) {
//                    if (response.isSuccessful()) {
//                        List<ClassModelTest> classes = response.body();
//                        if (classes != null && !classes.isEmpty()) {
//                            ClassModelTest classModel = classes.get(0);
//                            String classDetails = "Tên: " + classModel.getTen() + "\n" +
//                                    "Thời gian: " + classModel.getThoigian() + "\n" +
//                                    "Giá tiền: " + classModel.getGiatien() + "\n" +
//                                    "Điện thoại: " + classModel.getDienthoai() + "\n" +
//                                    "CLB: " + classModel.getClub() + "\n" +
//                                    "Giảng viên: " + classModel.getGiangvien();
//                            classInfo.setText(classDetails);
//                        } else {
//                            classInfo.setText("Bạn chưa đăng ký khóa học nào.");
//                        }
//                    } else {
//                        classInfo.setText("Bạn chưa đăng ký khóa học nào.");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<List<ClassModelTest>> call, Throwable t) {
//                    classInfo.setText("Bạn chưa đăng ký khóa học nào.");
//                }
//            });
//        } else {
//            classInfo.setText("Chưa đăng nhập");
//        }
//    }

    public String TranslateText(String text, int k){
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.language);

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            String s = "";
            while ((line = reader.readLine()) != null) {
                if(line.contains(text)){
                    String temp[] = line.split(",");
                    if(k == 0){
                        s =  temp[0];
                    } else s= temp[1];
                    break;
                }

            }

            reader.close();
            return s;
            // Use the fileContent string
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void showPopupMenu(View v) {

        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.image_profile_menu, popupMenu.getMenu());
        if(languageS!= null){
            if(languageS.contains("en")){

                Menu menu = popupMenu.getMenu();

                for (int i = 0; i < menu.size(); i++) {
                    // Get the MenuItem at the current index
                    MenuItem menuItem = menu.getItem(i);

                    // Get the title of the MenuItem
                    String itemTitle = menuItem.getTitle().toString();
                    menuItem.setTitle(TranslateText(itemTitle,1));


                }
            }
        }

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
        showLoading();
        String token = sharedPreferences.getString("access_token", null);
        int memberId = sharedPreferences.getInt("member_id", -1);
        if (token != null && memberId != -1) {
            UserApiService apiService = ApiServiceProvider.getUserApiService();
            Call<ProfileModel> call = apiService.getProfile("Bearer " + token);
            call.enqueue(new Callback<ProfileModel>() {
                @Override
                public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                    hideLoading();
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
                            textViewChieucaoValue.setText(profile.getChieucao());
                            textViewCannangValue.setText(profile.getCannang());

                            // Tính toán tuổi dựa trên ngày sinh
                            int age = getAgeFromBirthdate(profile.getNgaysinh());

                            if (age >= 18) {
                                // Ẩn thông tin giám hộ nếu tuổi >= 18
                                textViewHotengiamhoValue.setVisibility(View.GONE);
                                textViewDienthoaiGiamhoValue.setVisibility(View.GONE);
                            } else {
                                // Hiển thị thông tin giám hộ nếu tuổi < 18
                                textViewHotengiamhoValue.setText(profile.getHotengiamho());
                                textViewDienthoaiGiamhoValue.setText(profile.getDienthoai_giamho());
                                textViewHotengiamhoValue.setVisibility(View.VISIBLE);
                                textViewDienthoaiGiamhoValue.setVisibility(View.VISIBLE);
                            }

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
                    hideLoading();
                    Toast.makeText(ActivityDetailMember.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            hideLoading();
            Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }

    private int getAgeFromBirthdate(String birthdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(birthdate);
            Calendar dob = Calendar.getInstance();
            dob.setTime(date);
            Calendar today = Calendar.getInstance();
            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            return age;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
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
                    hideLoading(); // Ensure loading is hidden after image selection
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

//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Fetch profile information mỗi khi Activity được hiển thị lại
//        fetchProfileInformation();
//        fetchClassInformation();
//    }

    private void showLoading() {
        if (loadingFragment == null) {
            loadingFragment = new BlankFragment();
            loadingFragment.show(getSupportFragmentManager(), "loading");
        }
    }

    private void hideLoading() {
        if (loadingFragment != null && !isFinishing() && !isDestroyed()) {
            loadingFragment.dismissAllowingStateLoss();
            loadingFragment = null;
        }
    }


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
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String ngaysinh = intent.getStringExtra("ngaysinh");
        if (ngaysinh != null) {
            // Cập nhật trực tiếp thông tin ngày sinh từ Intent nếu có
            textViewNgaysinhValue.setText(ngaysinh);
            // Tính toán tuổi và ẩn hiện thông tin giám hộ
            int age = getAgeFromBirthdate(ngaysinh);
            if (age >= 18) {
                textViewHotengiamhoValue.setVisibility(View.GONE);
                textViewDienthoaiGiamhoValue.setVisibility(View.GONE);
            } else {
                textViewHotengiamhoValue.setVisibility(View.VISIBLE);
                textViewDienthoaiGiamhoValue.setVisibility(View.VISIBLE);
            }
        }
    }
}