package com.android.mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.mobile.R;
import com.android.mobile.adapter.ShippingOrderAdapter;
import com.android.mobile.models.OrderStatusModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.UserApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {

    private static final String ARG_STATUS = "status";
    private RecyclerView recyclerView;
    private ShippingOrderAdapter shippingItemAdapter;
    private List<OrderStatusModel> orderList;
    private UserApiService apiService;

    public static OrderFragment newInstance(String status) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_shipping_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        apiService = ApiServiceProvider.getUserApiService();

        String status = getArguments().getString(ARG_STATUS);
        fetchOrders(status);

        return view;
    }

    private void fetchOrders(String status) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        apiService.getAllOrders("Bearer " + accessToken).enqueue(new Callback<List<OrderStatusModel>>() {
            @Override
            public void onResponse(Call<List<OrderStatusModel>> call, Response<List<OrderStatusModel>> response) {
                if (response.isSuccessful()) {
                    orderList = response.body();
                    if (orderList != null && !orderList.isEmpty()) {
                        // Lọc các đơn hàng có trạng thái "chưa giao hàng"
                        List<OrderStatusModel> filteredOrders = new ArrayList<>();
                        for (OrderStatusModel order : orderList) {
                            if ("chưa giao hàng".equalsIgnoreCase(order.getGiao_hang())) {
                                filteredOrders.add(order);
                            }
                        }

                        // Cập nhật adapter với danh sách đơn hàng đã lọc
                        shippingItemAdapter = new ShippingOrderAdapter(getContext(), filteredOrders);
                        recyclerView.setAdapter(shippingItemAdapter);
                    } else {
                        Toast.makeText(getContext(), "Không có đơn hàng nào", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Không thể tải đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderStatusModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

