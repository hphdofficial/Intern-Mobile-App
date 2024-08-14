package com.android.mobile;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.mobile.models.OrderListModel;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

public class OrderDetailFragment extends DialogFragment {

    private static final String ARG_ORDER = "order";
    private OrderListModel order;

    public static OrderDetailFragment newInstance(OrderListModel order) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ORDER, new Gson().toJson(order));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String orderJson = getArguments().getString(ARG_ORDER);
            order = new Gson().fromJson(orderJson, OrderListModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);
        if (getDialog() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView orderId = view.findViewById(R.id.order_id);
        TextView txnRef = view.findViewById(R.id.txn_ref);
        TextView orderInfo = view.findViewById(R.id.order_info);
        TextView orderAmount = view.findViewById(R.id.order_amount);
        TextView orderStatus = view.findViewById(R.id.order_status);
        TextView orderPayDate = view.findViewById(R.id.order_pay_date);
        ImageView qrCode = view.findViewById(R.id.qr_code); // ImageView để hiển thị QR code
        Button buttonClose = view.findViewById(R.id.button_close);

        orderId.setText(String.format("%d", order.getId()));
        txnRef.setText(order.getTxn_ref());
        orderInfo.setText(order.getOrder_info());
        orderAmount.setText(String.format("%,.0f VND", order.getAmount()));
        orderStatus.setText(order.getStatus());
        orderPayDate.setText(order.getPay_date());

        // Tính toán lại tổng số tiền sau khi đã áp dụng giảm giá
        double totalAmount = 0;
        for (OrderListModel.DetailCart cart : order.getDetail_carts()) {
            double unitPrice = cart.getProduct().getUnitPrice();
            String sale = cart.getProduct().getSale(); // Lấy giá trị giảm giá
            double percent = sale != null ? Double.parseDouble(sale) : 0;
            double priceAfterDiscount = unitPrice - (unitPrice * percent);
            totalAmount += priceAfterDiscount * cart.getQuantity();
        }

        // Hiển thị tổng số tiền sau khi giảm giá
        orderAmount.setText(String.format("%,.0f VND", totalAmount));

        // Sử dụng Glide để tải ảnh QR code từ URL
        if (order.getQr_link() != null && !order.getQr_link().isEmpty()) {
            Glide.with(this)
                    .load(order.getQr_link())
                    .into(qrCode);
        } else {
            qrCode.setVisibility(View.GONE); // Ẩn ImageView nếu không có QR link
        }

        buttonClose.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}