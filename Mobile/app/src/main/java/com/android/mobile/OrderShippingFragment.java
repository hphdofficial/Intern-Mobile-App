package com.android.mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.adapter.OrderAdapter;
import com.android.mobile.models.OrderListModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.UserApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderShippingFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView noOrdersTextView;
    private OrderAdapter shippingItemAdapter;
    private List<OrderListModel> orderList;
    private UserApiService apiService;
    private BlankFragment loadingFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        recyclerView = view.findViewById(R.id.recycler_shipping_item);
        noOrdersTextView = view.findViewById(R.id.no_orders_text);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize API service
        apiService = ApiServiceProvider.getUserApiService();

        // Fetch orders
        fetchAllOrders();

        return view;
    }


    private void fetchAllOrders() {
        if (getView() == null) return;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);
        apiService.getListOrder("Bearer " + accessToken).enqueue(new Callback<List<OrderListModel>>() {
            @Override
            public void onResponse(Call<List<OrderListModel>> call, Response<List<OrderListModel>> response) {
                if (response.isSuccessful()) {
                    List<OrderListModel> orderList = response.body();
                    if (orderList != null && !orderList.isEmpty()) {
                        // Lọc các đơn hàng có trạng thái "chưa giao hàng"
                        List<OrderListModel> undeliveredOrders = new ArrayList<>();
                        for (OrderListModel order : orderList) {
                            if ("đang giao hàng".equalsIgnoreCase(order.getGiao_hang())) {
                                undeliveredOrders.add(order);
                            }
                        }

                        if (!undeliveredOrders.isEmpty()) {
                            // Sắp xếp các đơn hàng theo ngày thanh toán giảm dần
                            Collections.sort(undeliveredOrders, new Comparator<OrderListModel>() {
                                @Override
                                public int compare(OrderListModel o1, OrderListModel o2) {
                                    return o2.getPay_date().compareTo(o1.getPay_date());
                                }
                            });

                            // Ẩn TextView nếu có đơn hàng
                            noOrdersTextView.setVisibility(View.GONE);
                        } else {
                            // Hiển thị TextView nếu không có đơn hàng nào
                            noOrdersTextView.setVisibility(View.VISIBLE);
                        }

                        // Cập nhật Adapter với danh sách đã lọc
                        if (shippingItemAdapter == null) {
                            shippingItemAdapter = new OrderAdapter(getContext(), undeliveredOrders);
                            recyclerView.setAdapter(shippingItemAdapter);
                            Toast.makeText(getContext(), "Adapter Null", Toast.LENGTH_SHORT).show();

                        } else {
                            shippingItemAdapter.setData(undeliveredOrders);
                            Toast.makeText(getContext(), "Adapter Not Null", Toast.LENGTH_SHORT).show();

                        }
                    }
                } else {
                    // Xử lý lỗi khi phản hồi không thành công
                    Toast.makeText(getContext(), "Không thể tải đơn hàng", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<OrderListModel>> call, Throwable t) {
                // Xử lý lỗi khi có lỗi xảy ra trong quá trình gọi API
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        fetchAllOrders();
    }
}
