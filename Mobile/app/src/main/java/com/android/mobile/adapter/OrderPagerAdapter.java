package com.android.mobile.adapter;

import android.support.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.mobile.OrderCancelFragment;
import com.android.mobile.OrderShippingFragment;
import com.android.mobile.OrderSuccessFragment;
import com.android.mobile.OrderWaitConfirmFragment;
import com.android.mobile.OrderWaitProductFragment;

public class OrderPagerAdapter extends FragmentStateAdapter {
    public OrderPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new OrderWaitConfirmFragment();
            case 1:
                return new OrderWaitProductFragment();
            case 2:
                return new OrderShippingFragment();
            case 3:
                return new OrderSuccessFragment();
            case 4:
                return new OrderCancelFragment();
            default:
                return new OrderWaitConfirmFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}