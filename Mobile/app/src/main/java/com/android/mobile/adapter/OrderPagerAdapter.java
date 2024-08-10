package com.android.mobile.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.mobile.OrderFragment;

public class OrderPagerAdapter extends FragmentStateAdapter {

    public OrderPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return OrderFragment.newInstance("Mới đặt");
            case 1:
                return OrderFragment.newInstance("Đang xử lý");
            case 2:
                return OrderFragment.newInstance("Đang giao");
            case 3:
                return OrderFragment.newInstance("Đã giao");
            case 4:
                return OrderFragment.newInstance("Đã hủy");
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 5; // Tổng số tab
    }
}
