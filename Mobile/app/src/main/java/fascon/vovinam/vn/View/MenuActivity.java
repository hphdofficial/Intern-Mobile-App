package fascon.vovinam.vn.View;

import fascon.vovinam.vn.Model.ProductSaleDownModel;
import fascon.vovinam.vn.Model.addressModel;
import fascon.vovinam.vn.R;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import fascon.vovinam.vn.Model.Class;
import fascon.vovinam.vn.Model.Club;
import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.Model.NewsModel;
import fascon.vovinam.vn.Model.ProductSaleModel;
import fascon.vovinam.vn.Model.ProfileModel;
import fascon.vovinam.vn.Model.network.APIServicePayment;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.ClassApiService;
import fascon.vovinam.vn.Model.services.ClubApiService;
import fascon.vovinam.vn.Model.services.NewsApiService;
import fascon.vovinam.vn.Model.services.PaymentAPI;
import fascon.vovinam.vn.Model.services.UserApiService;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.commons.math3.analysis.function.Max;
import org.apache.poi.ss.formula.functions.T;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends BaseActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private int placeholderResourceId = R.drawable.photo3x4;

/*    private ImageView imgAvatarMenu;
    private TextView textViewName;*/
    private SharedPreferences sharedPreferences;
 //   private TextView textViewBirthday;
    private BlankFragment loadingFragment;
    private LinearLayout linearinfor;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        languageS = sharedPreferences.getString("language",null);
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

        showLoading();


      //  ImageView imageView = findViewById(R.id.img_avatar_menu);
       /* Picasso.get()
                .load(linkAvatar)
                .placeholder(placeholderResourceId) // Hình ảnh placeholder
                .error(placeholderResourceId) // Hình ảnh sẽ hiển thị nếu tải lỗi
                .into(imageView);*/

        linearinfor = findViewById(R.id.linearinfor);
        sale = findViewById(R.id.sale);
        productsale1 = findViewById(R.id.productsale1);
        test1 = findViewById(R.id.test1);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Thêm hoặc thay thế Fragment mới
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        if(languageS == null){
            myContentE.putString("title", "Trang Chính");
        }
        else if(languageS.contains("en")){
            myContentE.putString("title", "Main Page");
        }else {
            myContentE.putString("title", "Trang Chính");
        }

        myContentE.apply();


        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();


        // Tham chiếu đến các thành phần giao diện
/*        imgAvatarMenu = findViewById(R.id.img_avatar_menu);
        textViewName = findViewById(R.id.textViewName);
        textViewBirthday= findViewById(R.id.txt_content);*/

        //click image


//        // Get id_club_shared and id_class_shared
//        getMyClub();
//        getMyClass();

        // Gọi phương thức loadUserData để hiển thị thông tin người dùng
      //  loadUserData();
        hotP = findViewById(R.id.hotP);
        menu = findViewById(R.id.menu);
        saleOp = findViewById(R.id.saleOp);
        qick = findViewById(R.id.qick);
        getListNew();
        getProductP();

        AddLayout();
        setEventClick();
        RemoveView();
        ShowMenu();

        String token = sharedPreferences.getString("access_token", null);
        String role = decodeRoleFromToken(token);

        createList();

        createListSaleProduct();
        CreateSale2();
        CreateSale();


        // Get current location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MenuActivity.this);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MenuActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            checkLocationSettings();
        }


    }
    private LinearLayout productsale1;
    private LinearLayout test1;
    public void createListSaleProduct(){
        String token = sharedPreferences.getString("access_token", null);
        String role = decodeRoleFromToken(token);
        if(role.contains("0")){
            PaymentAPI apiService = APIServicePayment.getPaymentApiService();
            Call<List<ProductSaleDownModel>> call = apiService.GetSaleDownProduct();
            call.enqueue(new Callback<List<ProductSaleDownModel>>() {
                @Override
                public void onResponse(Call<List<ProductSaleDownModel>> call, Response<List<ProductSaleDownModel>> response) {
                    if(response.isSuccessful()){
                        List<ProductSaleDownModel> list = response.body();
                        CreateItemSale(list,9);
                    }
                }

                @Override
                public void onFailure(Call<List<ProductSaleDownModel>> call, Throwable t) {

                }
            });


        }
    }



    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                LocationRequest locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setNumUpdates(1).setInterval(10000).setFastestInterval(5000);

                fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult == null) {
//                            Toast.makeText(MenuActivity.this, "Unable to get location", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Location location = locationResult.getLastLocation();
                        if (location != null) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("location_lat", String.valueOf(location.getLatitude()));
                            editor.putString("location_long", String.valueOf(location.getLongitude()));
                            editor.apply();
//                            Toast.makeText(MenuActivity.this, "Truy cập vị trí hiện tại thành công", Toast.LENGTH_SHORT).show();
                        } else {
//                            Toast.makeText(MenuActivity.this, "Không lấy được vị trí hiện tại", Toast.LENGTH_SHORT).show();
                        }
                        fusedLocationProviderClient.removeLocationUpdates(this);
                    }
                }, Looper.getMainLooper());
            } catch (SecurityException e) {
                e.printStackTrace();
                Toast.makeText(this, "SecurityException: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            ActivityCompat.requestPermissions(MenuActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }
    }

    private void checkLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    getCurrentLocation();
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                resolvable.startResolutionForResult(MenuActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                            } catch (ClassCastException e) {
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                getCurrentLocation();
            } else {
//                Toast.makeText(this, "Bạn đã hủy truy cập vị trí hiện tại", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationSettings();
            } else {
//                Toast.makeText(this, "Bạn đã từ chối truy cập vị trí hiện tại", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private LinearLayout hotP;
    private FlexboxLayout menu;
    private LinearLayout sale;
    private List<ProductSaleModel> listP = new ArrayList<>();
    private List<NewsModel> listNew = new ArrayList<>();
    private List<LinearLayout> listMenu = new ArrayList<>();


    public void getProductP(){
        String token = sharedPreferences.getString("access_token", null);
        String role = decodeRoleFromToken(token);
        if(role.contains("0")) {
            PaymentAPI apiService = APIServicePayment.getPaymentApiService();
            Call<List<ProductSaleModel>> call ;
            if(languageS!= null){
                if(languageS.contains("en")){
                    call = apiService.GetSaleProduct("en");
                }else  call = apiService.GetSaleProduct("vi");
            }else  call = apiService.GetSaleProduct("vi");


            call.enqueue(new Callback<List<ProductSaleModel>>() {
                @Override
                public void onResponse(Call<List<ProductSaleModel>> call, Response<List<ProductSaleModel>> response) {
                    if (response.isSuccessful()) {
                        listP = response.body();
                        CreateItemP(listP, 9);
                    }
                }

                @Override
                public void onFailure(Call<List<ProductSaleModel>> call, Throwable t) {

                }
            });


        }



    }
    public void ShowMenu(){
        String token = sharedPreferences.getString("access_token", null);
        String role = decodeRoleFromToken(token);
        if(role.contains("0")){
            productsale1.setVisibility(View.VISIBLE);
            test1.setVisibility(View.VISIBLE);
            // call club
            ClubApiService service = ApiServiceProvider.getClubApiService();
            Call<Club> call = service.getDetailClubMember("Bearer" + token);

            call.enqueue(new Callback<Club>() {
                @Override
                public void onResponse(Call<Club> call, Response<Club> response) {
                    if (response.isSuccessful()) {
                        Club clb = response.body();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("id_club_shared", clb.getId_club());
                        editor.putString("name_clb", clb.getTen());
                        editor.apply();
                        String club = sharedPreferences.getString("id_club_shared",null);
                        if(club !=null){


                            // call my class
                            ClassApiService service = ApiServiceProvider.getClassApiService();
                            Call<List<Class>> callz = service.getDetailClassMember("Bearer" + token);

                            callz.enqueue(new Callback<List<Class>>() {
                                @Override
                                public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                                    List<Class> classs = response.body();
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("name_class",classs.get(0).getTen());
                                    editor.putString("id_class_shared", classs.get(0).getId() != 0 ? String.valueOf(classs.get(0).getId()) : null);
                                    editor.apply();


                                    String myClass = sharedPreferences.getString("id_class_shared",null);
                                    RemoveViewUser();
                                    hideLoading();
                                }


                                @Override
                                public void onFailure(Call<List<Class>> call, Throwable t) {

                                    editor.putString("id_class_shared",null);
                                    ViewUserNotRegister();
                                    hideLoading();

                                }
                            });
                        }
                    }
                    else {
                        ViewUserNotClub();
                        hideLoading();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("id_club_shared", null);
                        editor.apply();
                    }
                }
                @Override
                public void onFailure(Call<Club> call, Throwable t) {
                    hideLoading();
                }
            });

        }else {
            RemoveViewHLV();
            hideLoading();
        }
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
    private LinearLayout btn_order_status;
    private LinearLayout btn_cart;
    private LinearLayout btn_history;
    private LinearLayout btn_approve;

    public void AddLayout(){
        menu.removeAllViews();
        btn_new = CreateLinearLayout(btn_new,"Tin tức võ học","newin");
        btn_lythuyet = CreateLinearLayout(btn_lythuyet,"Lý thuyết võ đạo","booklt");
        btn_club = CreateLinearLayout(btn_club,"Câu lạc bộ","club");
        btn_register = CreateLinearLayout(btn_register,"Đăng ký lớp học","images");
        btn_product = CreateLinearLayout(btn_product,"Sản phẩm","product");
        btn_cart  = CreateLinearLayout(btn_cart,"Giỏ hàng","cart1");
        btn_order_status = CreateLinearLayout(btn_order_status,"Duyệt đơn hàng mới","imaghe");
        btn_approve = CreateLinearLayout(btn_approve,"Duyệt yêu cầu","imaghe");
        btn_history  = CreateLinearLayout(btn_history,"Lịch sử mua hàng","history6");
        btn_class = CreateLinearLayout(btn_class,"Điểm danh lớp học","tick");
        btn_historyclass1 = CreateLinearLayout(btn_historyclass1,"Lịch sử đăng ký môn học","imaghe");

        btn_sup = CreateLinearLayout(btn_sup,"Nhà cung cấp","house");

        btn_logout = CreateLinearLayout(btn_logout,"Đăng xuất","run");


    }
    public void RemoveView(){
        menu.removeView(btn_lythuyet);
        menu.removeView(btn_historyclass1);
        menu.removeView(btn_register);
        menu.removeView(btn_logout);
        menu.removeView(btn_product);
        menu.removeView(btn_cart);
        menu.removeView(btn_new);
        menu.removeView(btn_sup);
        menu.removeView(btn_history);
        menu.removeView(btn_club);
//        menu.removeView(btn_logout);
    }
    public void RemoveViewUser(){
        menu.removeView(btn_order_status);
        menu.removeView(btn_club);
        menu.removeView(btn_class);
        menu.removeView(btn_approve);
    }
    public void ViewUserNotRegister(){
        menu.removeView(btn_order_status);
        menu.removeView(btn_club);
        menu.removeView(btn_class);
        menu.removeView(btn_approve);

    }
    public void ViewUserNotClub(){
        menu.removeView(btn_order_status);
        menu.removeView(btn_class);
        menu.removeView(btn_approve);
    }
    public void RemoveViewHLV(){


    }
    public void createList(){
        List<addressModel> list = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("myAddress", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list",null);
        if(json == null){
            addressModel m = new addressModel("Long An","0000000000",1);
            list.add(m);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            String j = gson.toJson(list);
            editor.putString("list", j);
            editor.apply();
        }

    }
    private String languageS = null;
    private LinearLayout CreateLinearLayout(LinearLayout linear, String content, String linkImage){
        linear = new LinearLayout(this);
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                0, // Width sẽ được xác định bởi layout_flexBasisPercent
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        int screenWidth = getScreenWidth(this);
        linear.setGravity(Gravity.CENTER);
        layoutParams.setMargins(10,0,10,10);
        layoutParams.setFlexBasisPercent(0.30f); // Chiếm 1/3 chiều rộng (33%)
        linear.setLayoutParams(layoutParams);
        linear.setOrientation(LinearLayout.VERTICAL);
        CardView cardView = new CardView(this);
        cardView.setId(View.generateViewId());
        cardView.setLayoutParams(new ViewGroup.LayoutParams(
                screenWidth/4-10,screenWidth/4-10
        ));
        cardView.setCardElevation(4);
        cardView.setRadius(screenWidth/4-10); // Thiết lập corner radius

        // Tạo ImageView và thêm vào CardView
        ImageView imageView = new ImageView(this);
        imageView.setId(View.generateViewId());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        String imageName = linkImage; // Tên tệp hình ảnh mà bạn muốn đặt
        int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        imageView.setBackgroundResource(resourceId);

        cardView.addView(imageView);


        // tạo văn bản
        TextView textView = new TextView(this);

        textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                150
        ));

        String tran = "";
                if(languageS == null){
                     tran = TranslateText(content,0);
                }
                else if(languageS.contains("en")){
                     tran = TranslateText(content,1);
                }else {
                    tran = TranslateText(content,0);
                }
        textView.setText(tran);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(17);
        linear.addView(cardView);
        linear.addView(textView);
        menu.addView(linear);


        return linear;

        // cái 2

    }
    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static String decodeRoleFromToken(String jwtToken) {
        try {
            // Tách token thành các phần: header, payload, và signature
            String[] parts = jwtToken.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid JWT token format");
            }

            // Phần payload là phần thứ 2
            String payload = parts[1];

            // Giải mã payload từ Base64Url
            byte[] decodedBytes = Base64.decode(payload, Base64.URL_SAFE);
            String decodedPayload = new String(decodedBytes, StandardCharsets.UTF_8);

            // Chuyển đổi payload thành JSONObject
            JSONObject jsonObject = new JSONObject(decodedPayload);

            // Trích xuất role từ payload
            return jsonObject.getString("role");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public void getListNew(){

        NewsApiService apiService = ApiServiceProvider.getNewsApiService();
        Call<List<NewsModel>> call;
        if(languageS!= null){
            if(languageS.contains("en")){
                call = apiService.getAnouncements("en");
            }else  call = apiService.getAnouncements("vi");
        }else  call = apiService.getAnouncements("vi");
        call.enqueue(new Callback<List<NewsModel>>() {
            @Override
            public void onResponse(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    listNew.clear(); // Xóa dữ liệu cũ
                    listNew.addAll(response.body());
                    CreateNew(listNew);
                    //filterNews(searchEditText.getText().toString()); // Lọc theo tìm kiếm
                } else {
                 //   showNoNewsMessage();
                }
            }

            @Override
            public void onFailure(Call<List<NewsModel>> call, Throwable t) {
            hideLoading();
              //  Toast.makeText(ActivityNews.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        
    }

    public void CreateItemSale(List<ProductSaleDownModel> list, int size ){
        sale.removeAllViews();
        int screenWidth = getScreenWidth(this);
        int count = 0;
        if(list.size()>0){
            while (count < list.size() && count<size){
                ProductSaleDownModel p = list.get(count);


                LinearLayout linear = new LinearLayout(this);
                FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                        screenWidth/3-10,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setFlexShrink(0);
                linear.setGravity(Gravity.CENTER);
                layoutParams.setMargins(10,0,10,10);
                layoutParams.setFlexBasisPercent(0.30f); // Chiếm 1/3 chiều rộng (33%)
                linear.setLayoutParams(layoutParams);
                linear.setOrientation(LinearLayout.VERTICAL);
                CardView cardView = new CardView(this);
                cardView.setId(View.generateViewId());
                cardView.setLayoutParams(new ViewGroup.LayoutParams(
                        150,150
                ));
                cardView.setCardElevation(4);
                //  cardView.setRadius(150); // Thiết lập corner radius

                // Tạo ImageView và thêm vào CardView
                ImageView imageView = new ImageView(this);
                imageView.setId(View.generateViewId());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                String imageName = p.getLinkImage(); // Tên tệp hình ảnh mà bạn muốn đặt
                if(imageName.length()> 10){

                    Picasso.get()
                            .load(imageName)
                            .placeholder(placeholderResourceId ) // Hình ảnh placeholder
                            .error(placeholderResourceId) // Hình ảnh sẽ hiển thị nếu tải lỗi
                            .into(imageView);
                }


                cardView.addView(imageView);


                // tạo văn bản
                TextView textView = new TextView(this);

                textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                textView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        100
                ));
                textView.setText(p.getName());
                if(languageS!= null){
                    if(languageS.contains("en")){
                        textView.setText(p.getTenenglish());
                    }
                }
                textView.setTextColor(Color.BLUE);
                textView.setTextSize(12);

                TextView textView1 = new TextView(this);

                textView1.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);

                textView1.setText("Giá: " + String.format("%,.0f đ", p.getPrice()));
                textView1.setTextColor(Color.GRAY);
                textView1.setTextSize(12);

                TextView textView2 = new TextView(this);

                textView2.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                String down = Double.parseDouble(p.getSale())*100+"";
                down = down.replace(".0","");
                Double money = p.getPrice() - Double.parseDouble(down)/100*p.getPrice();

                textView2.setText("Giảm giá: "+down+"% " + "\n Tổng: " + String.format("%,.0f đ", money));
                textView2.setTextColor(Color.RED);
                textView2.setTextSize(12);

                if(languageS != null){
                    if(languageS.contains("en")){
                        textView1.setText(TranslateText("Giá",1)+": " + String.format("%,.0f đ", p.getPrice()));
                        textView2.setText(TranslateText("Giảm giá",1)+": "+down+"% " + "\n"+ TranslateText("Tổng",1)+":"  + String.format("%,.0f đ", money));
                    }
                }
                linear.addView(cardView);
                linear.addView(textView);
                linear.addView(textView1);
                linear.addView(textView2);

                linear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent iten = new Intent(getApplicationContext(),activity_item_detail.class);
                        iten.putExtra("id",p.getId());
                        iten.putExtra("IDSupplier",0);
                        iten.putExtra("categoryName",p.getNameSup());
                        startActivity(iten);
                    }
                });




                sale.addView(linear);

                count = count+1;

            }

            //setContentView(linearinfor);
        }
    }
    private int max = Integer.MAX_VALUE;
    private int max1= Integer.MAX_VALUE;
    private Spinner saleOp;
    private Spinner qick;
    public void CreateSale() {

        List<String> list = new ArrayList<>();
        // Thêm dữ liệu mẫu vào danh sách voucher
        if(languageS == null){
            list.add("Hiển thị 9");
            list.add("Hiển thị 30");
            list.add("Hiển thị tất cả");
        }else if(languageS.contains("en")){
            list.add("Show 9");
            list.add("Show 30");
            list.add("Show all");
        }else {
            list.add("Hiển thị 9");
            list.add("Hiển thị 30");
            list.add("Hiển thị tất cả");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.layout_textviewsize, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        saleOp.setAdapter(adapter);
        saleOp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();

                if(selectedItem.contains("9"))
                    max1 = 9;
                else
                if(selectedItem.contains("30"))
                    max1 = 30;
                else
                    max1 = Integer.MAX_VALUE;
                PaymentAPI apiService = APIServicePayment.getPaymentApiService();
                Call<List<ProductSaleDownModel>> call = apiService.GetSaleDownProduct();
                call.enqueue(new Callback<List<ProductSaleDownModel>>() {
                    @Override
                    public void onResponse(Call<List<ProductSaleDownModel>> call, Response<List<ProductSaleDownModel>> response) {
                        if(response.isSuccessful()){
                            List<ProductSaleDownModel> list = response.body();
                            CreateItemSale(list,max1);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ProductSaleDownModel>> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    public void CreateSale2() {

        List<String> list = new ArrayList<>();
        // Thêm dữ liệu mẫu vào danh sách voucher
        if(languageS == null){
            list.add("Hiển thị 9");
            list.add("Hiển thị 30");
            list.add("Hiển thị tất cả");
        }else if(languageS.contains("en")){
            list.add("Show 9");
            list.add("Show 30");
            list.add("Show all");
        }else {
            list.add("Hiển thị 9");
            list.add("Hiển thị 30");
            list.add("Hiển thị tất cả");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.layout_textviewsize, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qick.setAdapter(adapter);
        qick.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();

                if(selectedItem.contains("9"))
                    max = 9;
                else
                if(selectedItem.contains("30"))
                    max = 30;
                else
                    max = Integer.MAX_VALUE;

                PaymentAPI apiService = APIServicePayment.getPaymentApiService();
                Call<List<ProductSaleModel>> call ;
                if(languageS!= null){
                    if(languageS.contains("en")){
                        call = apiService.GetSaleProduct("en");
                    }else  call = apiService.GetSaleProduct("vi");
                }else  call = apiService.GetSaleProduct("vi");

                call.enqueue(new Callback<List<ProductSaleModel>>() {
                    @Override
                    public void onResponse(Call<List<ProductSaleModel>> call, Response<List<ProductSaleModel>> response) {
                        if(response.isSuccessful()){
                            listP = response.body();
                            CreateItemP(listP,max);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ProductSaleModel>> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void CreateItemP(List<ProductSaleModel> list,int size){
        hotP.removeAllViews();
        int screenWidth = getScreenWidth(this);
        int count = 0;
        if(list.size()>0){
            while (count < list.size() && count<=size){
                ProductSaleModel p = list.get(count);
                LinearLayout linear = new LinearLayout(this);
                FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                        screenWidth/3-10, // Width sẽ được xác định bởi layout_flexBasisPercent
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                linear.setGravity(Gravity.CENTER);
                layoutParams.setMargins(10,0,10,10);
                layoutParams.setFlexBasisPercent(0.30f); // Chiếm 1/3 chiều rộng (33%)
                linear.setLayoutParams(layoutParams);
                linear.setOrientation(LinearLayout.VERTICAL);
                CardView cardView = new CardView(this);
                cardView.setId(View.generateViewId());
                cardView.setLayoutParams(new ViewGroup.LayoutParams(
                        150,150
                ));
                cardView.setCardElevation(4);
              //  cardView.setRadius(150); // Thiết lập corner radius

                // Tạo ImageView và thêm vào CardView
                ImageView imageView = new ImageView(this);
                imageView.setId(View.generateViewId());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                String imageName = p.getLinkImage(); // Tên tệp hình ảnh mà bạn muốn đặt
                if(imageName.length()> 10) {
                    Picasso.get().load(imageName).placeholder(R.drawable.photo3x4).error(R.drawable.photo3x4).into(imageView);
                }
                cardView.addView(imageView);


                // tạo văn bản
                TextView textView = new TextView(this);

                textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                textView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        100
                ));
                textView.setText(p.getName());
                if(languageS!= null){
                    if(languageS.contains("en")){
                        textView.setText(p.getTenenglish());
                    }
                }
                textView.setTextColor(Color.BLUE);
                textView.setTextSize(12);

                TextView textView1 = new TextView(this);

                textView1.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                textView1.setText("Giá: " + String.format("%,.0f đ", p.getPrice()));
                textView1.setTextColor(Color.RED);
                textView1.setTextSize(12);

                TextView textView2 = new TextView(this);

                textView2.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                textView2.setText("Đã bán: "+p.getQuantity());
                textView2.setTextColor(Color.GRAY);
                textView2.setTextSize(12);
                if(languageS != null){
                    if(languageS.contains("en")){
                        textView1.setText("Price: " + String.format("%,.0f đ", p.getPrice()));
                        textView2.setText(TranslateText("Đã bán",1)+": "+p.getQuantity());
                    }
                }
                linear.addView(cardView);
                linear.addView(textView);
                linear.addView(textView1);
                linear.addView(textView2);

                linear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent iten = new Intent(getApplicationContext(),activity_item_detail.class);
                        iten.putExtra("id",p.getId());
                        iten.putExtra("IDSupplier",p.getCategoryId());
                        iten.putExtra("categoryName",p.getNameSup());
                        startActivity(iten);
                    }
                });




                hotP.addView(linear);

                count = count+1;

            }

            //setContentView(linearinfor);
        }

    }

    public void CreateNew(List<NewsModel> list){
        linearinfor.removeAllViews();
        TextView t = new TextView(getApplicationContext());

        String s = sharedPreferences.getString("language",null);
        if (s != null){
            if(s.contains("en")){
                t.setText("Notification" + " (NEW)");
            }
            else {
                t.setText("Thông báo" + " (NEW)");
            }

        }
        else {
            t.setText("Thông báo" + " (NEW)");
        }
        t.setTextColor(Color.RED);
        t.setTextSize(20);
        // Áp dụng animation chữ chạy
        // Tạo ValueAnimator để thay đổi màu sắc
        ValueAnimator colorAnimator = ValueAnimator.ofArgb(Color.YELLOW, Color.RED);
        colorAnimator.setDuration(1000); // Thay đổi màu trong 1 giây
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE); // Lặp lại vô hạn
        colorAnimator.setRepeatMode(ValueAnimator.REVERSE); // Đảo ngược màu

        // Cập nhật màu chữ của TextView
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int color = (int) animator.getAnimatedValue();
                t.setTextColor(color);
            }
        });
        String token = sharedPreferences.getString("access_token", null);
        String role = decodeRoleFromToken(token);
        // Bắt đầu animation
        colorAnimator.start();

        linearinfor.addView(t);
        int count = 1;
        int k = 0;
        if(list.size()>0){
            if(role.contains("0")){
                while (count<=list.size()  && k < 3){

                    NewsModel news = list.get(list.size()-count);
                    if(!news.getType().contains("giang-vien")){
                        TextView textView = new TextView(getApplicationContext());
                        textView.setPadding(10,5,0,0);
                        textView.setId(news.getId());
                        textView.setText("* "+news.getTenvi());

                        if (s != null){
                            if(s.contains("en")) {
                                textView.setText("* "+news.getTenen());
                            }

                        }
                        textView.setTextColor(Color.RED);
                        textView.setTextSize(18);
                        String imageUrl = "http://tambinh.websinhvien.net/thumbs/340x280x1/upload/news/" + news.getPhoto();
                        // event
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(), NewsDetailActivity.class);
                                intent.putExtra("NewsTitle", news.getTenvi());
                                intent.putExtra("NewsContent", news.getNoidungvi());
                                intent.putExtra("NewsImage", imageUrl); // Pass the full image URL
                                startActivity(intent);
                            }
                        });
                        linearinfor.addView(textView);
                        k+=1;
                        ObjectAnimator colorAnim = ObjectAnimator.ofInt(
                                textView, "textColor",
                                Color.RED, Color.BLUE, Color.GREEN
                        );

                        // Sử dụng ArgbEvaluator để chuyển đổi màu mượt mà
                        colorAnim.setEvaluator(new ArgbEvaluator());
                        colorAnim.setDuration(3000);

                    }

                    count = count+1;
                    // Thiết lập thời gian chuyển đổi màu


                }
            }else{
                k = 0;
                while (count<=list.size() && k<3){

                    NewsModel news = list.get(list.size()-count);
                    if(news.getType().contains("giang-vien")){

                        TextView textView = new TextView(getApplicationContext());
                        textView.setPadding(10,5,0,0);
                        textView.setId(news.getId());
                        textView.setText("* "+news.getTenvi());
                        textView.setTextColor(Color.RED);
                        if (s != null){
                            if(s.contains("en")) {
                                textView.setText("* "+news.getTenen());
                            }

                        }
                        textView.setTextSize(18);
                        String imageUrl = "http://tambinh.websinhvien.net/thumbs/340x280x1/upload/news/" + news.getPhoto();
                        // event
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(), NewsDetailActivity.class);
                                intent.putExtra("NewsTitle", news.getTenvi());
                                intent.putExtra("NewsContent", news.getNoidungvi());
                                intent.putExtra("NewsImage", imageUrl); // Pass the full image URL
                                startActivity(intent);
                            }
                        });
                        linearinfor.addView(textView);
                        k+=1;
                        ObjectAnimator colorAnim = ObjectAnimator.ofInt(
                                textView, "textColor",
                                Color.RED, Color.BLUE, Color.GREEN
                        );

                        // Sử dụng ArgbEvaluator để chuyển đổi màu mượt mà
                        colorAnim.setEvaluator(new ArgbEvaluator());
                        colorAnim.setDuration(3000);

                    }

                    count = count+1;
                    // Thiết lập thời gian chuyển đổi màu


                }
            }


            //setContentView(linearinfor);
        }
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
                    editor.putString("id_club_shared", clb.getId_club());
                    editor.apply();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("id_club_shared", null);
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<Club> call, Throwable t) {
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
            }

            @Override
            public void onFailure(Call<List<Class>> call, Throwable t) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("id_class_shared", null);
                editor.apply();
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

           SharedPreferences.Editor edit =  sharedPreferences.edit();

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


    private ImageView img_avatar_menu;
    private ImageView test;

    private ImageView test2;
    private ImageView test3;
    private ImageView test4;
    private ImageView test5;

    private ConstraintLayout user;
    public void setEventClick(){


        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), OrderActivity.class));
            }
        });



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

      //  img_avatar_menu = findViewById(R.id.img_avatar_menu);



        btn_lythuyet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), activity_chapters.class));
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
//                        Toast.makeText(MenuActivity.this, "Bạn chưa đăng ký lớp học nào", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), ClassActivity.class));
                    } else {
//                        Toast.makeText(MenuActivity.this, "Bạn phải tham gia câu lạc bộ trước tiên", Toast.LENGTH_SHORT).show();
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
        btn_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ApproveActivity.class));

            }
        });
        btn_order_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ApproveOrderActivity.class));

            }
        });
//        test5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                logout();
//            }
//        });

      //  eventAnimationImage();

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




                          /*  textViewName.setText(profile.getTen());
                            textViewBirthday.setText(profile.getNgaysinh());*/

                            // Load avatar từ SharedPreferences cho user cụ thể
                            String avatarUrl = sharedPreferences.getString("avatar_url_" + memberId, null);

                            myContentE.putString("avatar", avatarUrl);
                            myContentE.apply();
                            if (avatarUrl != null) {

                             //   Picasso.get().load(avatarUrl).placeholder(R.drawable.photo3x4).error(R.drawable.photo3x4).into(imgAvatarMenu);
                            } else {
                             //   imgAvatarMenu.setImageResource(R.drawable.photo3x4); // Ảnh mặc định
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
        SetLanguage();
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

    public void SetLanguage(){

        SharedPreferences shared = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
       String s = shared.getString("language",null);
       if(s == null){
           translateVN();
       }else if(s.contains("en")){
           translateEng();
       }else {
           translateVN();
       }
    }
    public void translateEng(){

        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Main Page");
        myContentE.apply();
        TextView test = findViewById(R.id.test);
        test.setText("Discounted products");

        TextView productsale = findViewById(R.id.productsale);
        productsale.setText("Best selling product");


    }
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
    public void translateVN(){
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Trang chính");
        myContentE.apply();
        TextView test = findViewById(R.id.test);
        test.setText("Sản phẩm giảm giá");

        TextView productsale = findViewById(R.id.productsale);
        productsale.setText("Sản phẩm bán chạy");

    }
}