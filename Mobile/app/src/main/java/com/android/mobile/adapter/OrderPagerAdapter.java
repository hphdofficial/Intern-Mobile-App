package com.android.mobile.adapter;

import android.support.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.mobile.OrderFragment;

public class OrderPagerAdapter extends FragmentStateAdapter {
    public OrderPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new OrderFragment("chờ xác nhận");
            case 1:
                return new OrderFragment("chờ lấy hàng");
            case 2:
                return new OrderFragment("đang giao hàng");
            case 3:
                return new OrderFragment("đã giao hàng");
            case 4:
                return new OrderFragment("đã hủy");
            default:
                return new OrderFragment("chờ xác nhận");
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}