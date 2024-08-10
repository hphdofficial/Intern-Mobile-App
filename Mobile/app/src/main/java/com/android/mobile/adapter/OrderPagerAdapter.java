package com.android.mobile.adapter;

import android.support.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.mobile.OrderSuccessFragment;

public class OrderPagerAdapter extends FragmentStateAdapter {

    public OrderPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new OrderSuccessFragment();
            case 1:
                return new OrderSuccessFragment();
            case 2:
                return new OrderSuccessFragment();
            case 3:
                return new OrderSuccessFragment();
            case 4:
                return new OrderSuccessFragment();
            default:
                return new OrderSuccessFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}