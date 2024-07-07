package com.android.mobile;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class titleFragment extends Fragment {

    LinearLayout sub_menu;
    private TextView title;
    ConstraintLayout main;
    private ImageView img_menu;
    private ImageView img_language;
    public static titleFragment newInstance() {
        return new titleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_title, container, false);
        rootView.bringToFront();
        title = rootView.findViewById(R.id.txt_title);
        main =  rootView.findViewById(R.id.main);
        sub_menu = rootView.findViewById(R.id.sub_menu);
        img_menu = rootView.findViewById(R.id.img_menu);
        title.setText("Nội dung tiêu đề");
        CreateFracmentSubMenu();


        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartView();
            }
        });
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sub_menu.setVisibility(View.VISIBLE);
            }
        });

        return rootView;

    }
    public void restartView(){
        sub_menu.setVisibility(View.GONE);


    }

    public void CreateFracmentSubMenu(){
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new sub_menu())
                .commit();
        sub_menu.setVisibility(View.GONE);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

    @Override
    public void onResume() {
        super.onResume();
        restartView();
    }
}