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

import com.android.mobile.models.Class;
import com.android.mobile.models.ClassModelT;
import com.android.mobile.models.Club;
import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.models.HistoryClassModel;
import com.android.mobile.models.ProfileModel;
import com.android.mobile.network.APIServicePayment;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.ClassApiService;
import com.android.mobile.services.ClubApiService;
import com.android.mobile.services.PaymentAPI;
import com.android.mobile.services.UserApiService;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Method;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends BaseActivity {

    private int placeholderResourceId = R.drawable.photo3x4;

    private ImageView imgAvatarMenu;
    private TextView textViewName;
    private SharedPreferences sharedPreferences;
    private TextView textViewBirthday;
    private BlankFragment loadingFragment;
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

        // Get id_club_shared and id_class_shared
        getMyClub();
        getMyClass();

        // Gọi phương thức loadUserData để hiển thị thông tin người dùng
        loadUserData();
    }

    public void getMyClub() {
        String token = sharedPreferences.getString("access_token", null);

        ClubApiService service = ApiServiceProvider.getClubApiService();
        Call<Club> call = service.getDetailClubMember("Bearer" + token);

        call.enqueue(new Callback<Club>() {
            @Override
            public void onResponse(Call<Club> call, Response<Club> response) {
                if (response.isSuccessful()) {
                    Club clb = response.body();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("id_club_shared", clb != null ? clb.getId() : null);
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<Club> call, Throwable t) {
//                Toast.makeText(MenuActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getMyClass() {
        String token = sharedPreferences.getString("access_token", null);

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<List<Class>> call = service.getDetailClassMember("Bearer" + token);

        call.enqueue(new Callback<List<Class>>() {
            @Override
            public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                List<Class> classs = response.body();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("id_class_shared", classs.get(0).getId() != 0 ? String.valueOf(classs.get(0).getId()) : null);
                editor.apply();
//                Toast.makeText(MenuActivity.this, "id_class_shared " + sharedPreferences.getString("id_class_shared", null), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Class>> call, Throwable t) {
//                Toast.makeText(MenuActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
    private LinearLayout btn_historyclass1;
    private LinearLayout btn_product;
    private LinearLayout btn_sup;
    private ImageView img_avatar_menu;
    private ImageView test;
    private ImageView test1;
    private ImageView test2;
    private ImageView test3;
    private ImageView test4;
    private ImageView test5;
    private LinearLayout btn_cart;
    private LinearLayout btn_history;
    private ConstraintLayout user;
    public void setEventClick(){
        btn_lythuyet = findViewById(R.id.btn_lythuyet);
        btn_club = findViewById(R.id.btn_club);
        btn_register = findViewById(R.id.btn_register);
        btn_class = findViewById(R.id.btn_class);
        btn_new = findViewById(R.id.btn_infor);
        btn_logout = findViewById(R.id.btn_logout);
        btn_product = findViewById(R.id.btn_product);
        user = findViewById(R.id.user);
        test = findViewById(R.id.test);
        test1 = findViewById(R.id.test1);
        test2 = findViewById(R.id.test2);
        test3 = findViewById(R.id.test3);
        test4 = findViewById(R.id.test4);
        test5 = findViewById(R.id.test5);
        btn_sup = findViewById(R.id.btn_sup);
        btn_cart = findViewById(R.id.btn_cart);
        btn_history = findViewById(R.id.btn_history);

        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HistoryOrderActivity.class));
            }
        });

        showLoading();

        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CartActivity.class));
            }
        });

        btn_sup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SupplierActivity.class));
            }
        });

        img_avatar_menu = findViewById(R.id.img_avatar_menu);

        btn_historyclass1 = findViewById(R.id.btn_historyclass1);
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
                if (sharedPreferences.getString("id_class_shared", null) != null) {
                    Intent intent = new Intent(getApplicationContext(), DetailClassActivity.class);
                    intent.putExtra("id_class", sharedPreferences.getString("id_class_shared", null));
                    startActivity(intent);
                } else {
                    if (sharedPreferences.getString("id_club_shared", null) != null) {
                        Toast.makeText(MenuActivity.this, "Bạn chưa đăng ký lớp học nào", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), ClassActivity.class));
                    } else {
                        Toast.makeText(MenuActivity.this, "Bạn phải tham gia câu lạc bộ trước tiên", Toast.LENGTH_SHORT).show();
                    }
                }
//                String token = sharedPreferences.getString("access_token", null);
//
//                PaymentAPI apiService = APIServicePayment.getPaymentApiService();
//                Call<List<ClassModelT>> call = apiService.GetClass("Bearer" + token);
//
//                call.enqueue(new Callback<List<ClassModelT>>() {
//                    @Override
//                    public void onResponse(Call<List<ClassModelT>> call, Response<List<ClassModelT>> response) {
//                        if(response.isSuccessful()){
//
//
//                            List<ClassModelT> cl = response.body();
//
//                            if(cl.size() > 0){
//                                Intent i =  new Intent(getApplicationContext(),DetailClassActivity.class);
//                                i.putExtra("id_class",cl.get(0).getId());
//                                startActivity( i);
//                            }else {
//                                startActivity(new Intent(getApplicationContext(), ClassActivity.class));
//                            }
//                        }else {
//                            Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<ClassModelT>> call, Throwable t) {
//
//                    }
//                });
//
//
//             //   startActivity(new Intent(getApplicationContext(), HistoryRegisterClass.class));
            }
        });
        btn_historyclass1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HistoryRegisterClass.class));
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
        // nút vào cửa hàng dụng cụ
        btn_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), activity_items.class));
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
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Kiểm tra nếu checkbox lưu mật khẩu không được chọn
        if (!sharedPreferences.getBoolean("checkbox_save_password", false)) {
            editor.clear(); // Xóa tất cả thông tin đăng nhập
        } else {
            editor.remove("access_token"); // Chỉ xóa token đăng nhập
            editor.remove("token_type");
            editor.remove("expires_in");
            editor.remove("member_id");
        }

        editor.apply();

        Intent intent = new Intent(MenuActivity.this, StartActivity.class); // Chuyển hướng về trang đăng nhập
        startActivity(intent);
        finish();
    }




    public void eventAnimationImage(){
/*        eventMenuItem(test);
        eventMenuItem(test1);
        eventMenuItem(test2);
        eventMenuItem(test3);
        eventMenuItem(test4);
        eventMenuItem(test5);*/
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
                            myContentE.putString("birthday",profile.getNgaysinh());
                            myContentE.putString("phone",profile.getDienthoai());
                            myContentE.putInt("age", year-Integer.parseInt(date[0]));




                            textViewName.setText(profile.getTen());
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
                            hideLoading();

                        }
                    } else {
                        hideLoading();
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
    private void showLoading() {
        loadingFragment = new BlankFragment();
        loadingFragment.show(getSupportFragmentManager(), "loading");
    }
    private void hideLoading() {
        if (loadingFragment != null) {
            loadingFragment.dismiss();
            loadingFragment = null;
        }
    }
}