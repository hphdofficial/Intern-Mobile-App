package com.android.mobile.adapter;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        // Hiển thị hộp thoại xác nhận thoát ứng dụng
        super.onBackPressed();
        BaseActivity.super.onBackPressed();
    }
}