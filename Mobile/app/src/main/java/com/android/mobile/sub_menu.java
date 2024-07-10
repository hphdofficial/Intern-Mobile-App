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

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;


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
private ImageView image_avatar;
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


        // sự kiện các nút
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here
                int id = item.getItemId();

                if(id == R.id.btn_self){
                    startActivity(new Intent(getContext(), ActivityDetailMember.class));
                }
                if(id == R.id.btn_lythuyet){
                    startActivity(new Intent(getContext(), activity_lessons.class));
                }
                if(id == R.id.btn_club){
                    startActivity(new Intent(getContext(), ClubActivity.class));
                }
                if(id == R.id.btn_registerclass){
                    startActivity(new Intent(getContext(), RegisterClass.class));
                }

                if(id == R.id.btn_infor){
                    startActivity(new Intent(getContext(), ActivityNews.class));
                }
                if(id == R.id.btn_inforsoftware){
                    startActivity(new Intent(getContext(),  AboutActivity.class));
                }
                if(id == R.id.btn_class){
                    startActivity(new Intent(getContext(), MyClassActivity.class));
                }
                if(id == R.id.btn_logout){
                    startActivity(new Intent(getContext(), StartActivity.class));
                }

                if(id == R.id.btn_language){
                }
                // Close the navigation drawer
               return true;
            }
        });


        back_content = rootView.findViewById(R.id.back_content);
        back_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentManager.beginTransaction().hide(sub_menu.this).commit();
            }
        });


        return rootView;
    }
    ConstraintLayout main;
    private LinearLayout back_content;

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

    private int getScreenHeight() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels + 100;

    }
    public void onIconClick(View view) {
    }
}