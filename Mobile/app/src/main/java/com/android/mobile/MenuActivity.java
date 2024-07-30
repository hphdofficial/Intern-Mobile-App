package com.android.mobile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.mobile.models.ProfileModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.UserApiService;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Method;
import java.sql.Date;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {

    private int placeholderResourceId = R.drawable.photo3x4;

    private ImageView imgAvatarMenu;
    private TextView textViewName;
    private SharedPreferences sharedPreferences;
    private TextView textViewBirthday;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // lưu trữ hình ảnh
        String linkAvatar = "https://firebasestorage.googleapis.com/v0/b/fir-43107.appspot.com/o/avatar-anime-707x600.jpg?alt=media&token=7f20d70e-a0d1-4244-8f35-0f32b69b1c46";
        SharedPreferences sharedPreferences = getSharedPreferences("myImage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("linkImage", linkAvatar);
        editor.apply();  // Lưu thay đổi vào SharedPreferences




        ImageView imageView = findViewById(R.id.img_avatar_menu);
        Picasso.get()
                .load(linkAvatar)
                .placeholder(placeholderResourceId) // Hình ảnh placeholder
                .error(placeholderResourceId) // Hình ảnh sẽ hiển thị nếu tải lỗi
                .into(imageView);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Thêm hoặc thay thế Fragment mới
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Trang Chính");
        myContentE.apply();


        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();


        // Tham chiếu đến các thành phần giao diện
        imgAvatarMenu = findViewById(R.id.img_avatar_menu);
        textViewName = findViewById(R.id.textViewName);
        textViewBirthday= findViewById(R.id.txt_content);

        //click image
        setEventClick();



        // Gọi phương thức loadUserData để hiển thị thông tin người dùng
        loadUserData();
    }


    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                try{
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
                catch(NoSuchMethodException e){
                    Log.e( "onMenuOpened", e.getMessage());
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }


    private TextView text;
    public void onMenuItemClick(View view) {
         text = findViewById(R.id.languageText);
        String language = text.getText()+"";
        if(view.getId() == R.id.btn_change){
           if(language.contains("VN")){
               text.setText("ENG");
           }else text.setText("VN");
        }
    }
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_nav,menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    private LinearLayout btn_lythuyet;
    private LinearLayout btn_club;
    private LinearLayout btn_register;
    private LinearLayout btn_class;
    private LinearLayout btn_new;
    private LinearLayout btn_logout;
    private ImageView img_avatar_menu;
    private ImageView test;
    private ImageView test1;
    private ImageView test2;
    private ImageView test3;
    private ImageView test4;
    private ImageView test5;

    private ConstraintLayout user;
    public void setEventClick(){
        btn_lythuyet = findViewById(R.id.btn_lythuyet);
        btn_club = findViewById(R.id.btn_club);
        btn_register = findViewById(R.id.btn_register);
        btn_class = findViewById(R.id.btn_class);
        btn_new = findViewById(R.id.btn_infor);
        btn_logout = findViewById(R.id.btn_logout);
        user = findViewById(R.id.user);
        test = findViewById(R.id.test);
        test1 = findViewById(R.id.test1);
        test2 = findViewById(R.id.test2);
        test3 = findViewById(R.id.test3);
        test4 = findViewById(R.id.test4);
        test5 = findViewById(R.id.test5);
        img_avatar_menu = findViewById(R.id.img_avatar_menu);
        img_avatar_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ActivityDetailMember.class));
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ActivityDetailMember.class));
            }
        });

        btn_lythuyet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), activity_lessons.class));
            }
        });
        btn_club.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ClubActivity.class));
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterClass.class));
            }
        });
        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ActivityNews.class));
            }
        });
        btn_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyClassActivity.class));
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        test5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        eventAnimationImage();
    }
    private void logout() {
        Intent intent = new Intent(MenuActivity.this, StartActivity.class); // Giả sử StartActivity là trang đăng nhập của bạn
        startActivity(intent);
        finish();
    }




    public void eventAnimationImage(){
        eventMenuItem(test);
        eventMenuItem(test1);
        eventMenuItem(test2);
        eventMenuItem(test3);
        eventMenuItem(test4);
        eventMenuItem(test5);
    }

    public void eventMenuItem(ImageView imageView){

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện animation khi ImageView được chạm
                v.animate()
                        .scaleX(1.2f)  // Phóng to theo trục X
                        .scaleY(1.2f)  // Phóng to theo trục Y
                        .alpha(0.8f)   // Giảm độ mờ (alpha)
                        .setDuration(300)  // Độ dài của animation (milliseconds)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                // Đoạn code sau khi animation kết thúc (nếu cần)
                                v.animate()
                                        .scaleX(1.0f)  // Trở về kích thước ban đầu trên trục X
                                        .scaleY(1.0f)  // Trở về kích thước ban đầu trên trục Y
                                        .alpha(1.0f)   // Trở về độ mờ ban đầu
                                        .setDuration(300)  // Độ dài của animation (milliseconds)
                                        .start();  // Bắt đầu animation
                            }
                        })
                        .start();  // Bắt đầu animation
            }
        });
    }
    private void loadUserData() {
        String token = sharedPreferences.getString("access_token", null);
        int memberId = sharedPreferences.getInt("member_id", -1); // Lấy member_id từ SharedPreferences khi đăng nhập
        if (token != null && memberId != -1) {
            UserApiService apiService = ApiServiceProvider.getUserApiService();
            Call<ProfileModel> call = apiService.getProfile("Bearer " + token);
            call.enqueue(new Callback<ProfileModel>() {
                @Override
                public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                    if (response.isSuccessful()) {
                        ProfileModel profile = response.body();
                        if (profile != null) {


                            SharedPreferences infor = getSharedPreferences("infor", Context.MODE_PRIVATE);
                            SharedPreferences.Editor myContentE = infor.edit();
                            myContentE.putString("name", profile.getUsername());
                            Calendar calendar = Calendar.getInstance();
                            int year = calendar.get(Calendar.YEAR);
                            String date[] = profile.getNgaysinh().split("-");

                            myContentE.putInt("age", year-Integer.parseInt(date[0]));




                            textViewName.setText(profile.getUsername());
                            textViewBirthday.setText(profile.getNgaysinh());

                            // Load avatar từ SharedPreferences cho user cụ thể
                            String avatarUrl = sharedPreferences.getString("avatar_url_" + memberId, null);

                            myContentE.putString("avatar", avatarUrl);
                            myContentE.apply();
                            if (avatarUrl != null) {

                                Picasso.get().load(avatarUrl).placeholder(R.drawable.photo3x4).error(R.drawable.photo3x4).into(imgAvatarMenu);
                            } else {
                                imgAvatarMenu.setImageResource(R.drawable.photo3x4); // Ảnh mặc định
                            }

                        }
                    } else {
                        Toast.makeText(MenuActivity.this, "Không thể lấy thông tin cá nhân", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProfileModel> call, Throwable t) {
                    Toast.makeText(MenuActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }
}