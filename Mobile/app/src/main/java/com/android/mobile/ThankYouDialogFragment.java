package com.android.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ThankYouDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_thank_you, container, false);

        Button buttonViewHistory = view.findViewById(R.id.buttonViewHistory);
        Button buttonGoHome = view.findViewById(R.id.buttonGoHome);

        // Xử lý sự kiện nút "Xem lịch sử mua hàng"
        buttonViewHistory.setOnClickListener(v -> {
            // Chuyển sang activity lịch sử mua hàng
            Intent intent = new Intent(getActivity(), HistoryOrderActivity.class);
            startActivity(intent);
            dismiss();
        });

        // Xử lý sự kiện nút "Trở về trang chủ"
        buttonGoHome.setOnClickListener(v -> {
            // Chuyển sang activity trang chủ
            Intent intent = new Intent(getActivity(), StartActivity.class);
            startActivity(intent);
            dismiss();
        });

        return view;
    }
}
