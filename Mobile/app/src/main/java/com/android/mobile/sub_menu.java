package com.android.mobile;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mobile.models.Item;
import com.android.mobile.models.ProfileModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.UserApiService;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link sub_menu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class sub_menu extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private NavigationView navigationView;
    private int placeholderResourceId = R.drawable.photo3x4;

     ImageView image_avatar;
    private SharedPreferences sharedPreferences;
    private TextView textViewName;
    private TextView textViewBirthday;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private  ImageView avatar_sub;
    public sub_menu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment sub_menu.
     */
    // TODO: Rename and change types and number of parameters
    public static sub_menu newInstance(String param1, String param2) {
        sub_menu fragment = new sub_menu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_sub_menu, container, false);

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);

        // Initialize views
/*        textViewName = rootView.findViewById(R.id.textViewName);
        textViewBirthday = rootView.findViewById(R.id.txt_content);*/
        image_avatar = rootView.findViewById(R.id.image_avatar_sub);

        // Load user data
      //  loadUserData();

        // xét chiều ngang và dọc
        rootView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,getScreenHeight()
               ));

        // lấy link image
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("myImage", Context.MODE_PRIVATE);
        String linkImage = sharedPreferences.getString("linkImage", "default_value");
        ImageView imageView = rootView.findViewById(R.id.image_avatar_sub);
        Picasso.get()
                .load(linkImage)
                .placeholder(placeholderResourceId ) // Hình ảnh placeholder
                .error(placeholderResourceId) // Hình ảnh sẽ hiển thị nếu tải lỗi
                .into(imageView);
        navigationView = rootView.findViewById(R.id.nav_view);


        // xét icon item đủ màu
        navigationView.setItemIconTintList(null);
        Menu menu = navigationView.getMenu();
        ShowMenu(menu);

        String token = sharedPreferences.getString("access_token", null);
        String role = decodeRoleFromToken(token);
        // sự kiện các nút
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here
                int id = item.getItemId();

                if(id == R.id.btn_home){
                    startActivity(new Intent(getContext(), MenuActivity.class));
                }

                if(id == R.id.btn_register_up){

                    startActivity(new Intent(getContext(), Register_belt.class));
                }
                if(id == R.id.btn_self){
                    startActivity(new Intent(getContext(), ActivityDetailMember.class));
                }
                if(id == R.id.btn_lythuyet){
                    startActivity(new Intent(getContext(), activity_chapters.class));                }
                if(id == R.id.btn_club){

                    startActivity(new Intent(getContext(), ClubActivity.class));
                }
                if(id == R.id.btn_registerclass){
                    if (sharedPreferences.getString("id_class_shared", null) == null) {
                        startActivity(new Intent(getContext(), ClassActivity.class));
                    } else {
                        Toast.makeText(getActivity(), "Bạn đã đăng ký lớp học rồi", Toast.LENGTH_SHORT).show();
                    }
//                    startActivity(new Intent(getContext(), ClassActivity.class));
                }

                if(id == R.id.btn_infor){
                    startActivity(new Intent(getContext(), ActivityNews.class));
                }
                if(id == R.id.btn_inforsoftware){
                    startActivity(new Intent(getContext(),  AboutActivity.class));
                }
                if(id == R.id.btn_class){

                    //Các lớp học giảng viên đang dạy điểm danh
                    startActivity(new Intent(getContext(), MyClassActivity.class));
                    //Đã đăng ký lớp học (Học viên)
//                    startActivity(new Intent(getContext(), activity_member_checkin.class));
                }
                if(id == R.id.btn_logout){

                    logout();
                }

                if(id == R.id.btn_language){
                }
                // Close the navigation drawer
               return true;
            }
        });


        back_content = rootView.findViewById(R.id.back_content);
        txt_name = rootView.findViewById(R.id.txt_name);
        txt_age = rootView.findViewById(R.id.txt_age);
        image_avatar_sub = rootView.findViewById(R.id.image_avatar_sub);
        back_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentManager.beginTransaction().hide(sub_menu.this).commit();
            }
        });


        // đẩy tên và tuổi
        SharedPreferences infor = getActivity().getSharedPreferences("infor", Context.MODE_PRIVATE);
        String name = infor.getString("name","Default");
        int age = infor.getInt("age",0);

        txt_name.setText(name);
        txt_age.setText(age+" tuổi");
        String avatarUrl = infor.getString("avatar",null);
      //  Toast.makeText(getContext(),avatarUrl,Toast.LENGTH_SHORT).show();

        if (avatarUrl != null) {
            Picasso.get().load(avatarUrl).placeholder(R.drawable.photo3x4).error(R.drawable.photo3x4).into(image_avatar_sub);
        } else {
            image_avatar_sub.setImageResource(R.drawable.photo3x4); // Ảnh mặc định
        }

        return rootView;
    }
    public void ShowMenu(Menu menu){
        String token = sharedPreferences.getString("access_token", null);
        String role = decodeRoleFromToken(token);
        if(role.contains("0")){
            String club = sharedPreferences.getString("id_club_shared",null);
            if(club !=null){
                String myClass = sharedPreferences.getString("id_class_shared",null);
                if(myClass != null){
                    MenuItem item = menu.findItem(R.id.btn_club);
                    item.setVisible(false);
                    MenuItem item2 =  menu.findItem(R.id.btn_register_up);
                    item2.setVisible(false);
                    //  RemoveViewUser();
                }else {
                    MenuItem item =  menu.findItem(R.id.btn_club);
                    item.setVisible(false);
                    MenuItem item1 =  menu.findItem(R.id.btn_class);
                    item1.setVisible(false);
                    // ViewUserNotRegister();
                }
            }else {
                MenuItem item =  menu.findItem(R.id.btn_registerclass);
                item.setVisible(false);
                MenuItem item1 =  menu.findItem(R.id.btn_class);
                item1.setVisible(false);
                MenuItem item2 =  menu.findItem(R.id.btn_register_up);
                item2.setVisible(false);

            }
        }else {
            MenuItem item =  menu.findItem(R.id.btn_registerclass);
            item.setVisible(false);

            //  RemoveViewHLV();
        }
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
    ConstraintLayout main;
    private LinearLayout back_content;
    private TextView txt_name;
    private  TextView txt_age;
    private  ImageView image_avatar_sub;
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem item = menu.findItem(R.id.btn_self);


            item.setEnabled(true);
            item.getIcon().setAlpha(255);

    }

    /*    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.sub_menu_options, menu);
        Log.e("zzz","aaa");
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }*/
    private void logout() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
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

        Intent intent = new Intent(getActivity(), StartActivity.class); // Chuyển hướng về trang đăng nhập
        startActivity(intent);
        getActivity().finish();
    }


    private void loadUserData() {
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
                            textViewName.setText(profile.getTen());
                            textViewBirthday.setText(profile.getNgaysinh());

                            // Load avatar từ SharedPreferences cho user cụ thể
                            String avatarUrl = sharedPreferences.getString("avatar_url_" + memberId, null);
                            if (avatarUrl != null) {
                                Picasso.get().load(avatarUrl).placeholder(R.drawable.photo3x4).error(R.drawable.photo3x4).into(image_avatar);
                            } else {
                                image_avatar.setImageResource(R.drawable.photo3x4); // Ảnh mặc định
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), "Không thể lấy thông tin cá nhân", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProfileModel> call, Throwable t) {
                    Toast.makeText(getActivity(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }

    private int getScreenHeight() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels + 100;

    }
    public void onIconClick(View view) {
    }
}