package com.android.mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.mobile.adapter.HistoryOrderAdapter;
import com.android.mobile.adapter.ShippingOrderAdapter;
import com.android.mobile.models.OrderModel;
import com.android.mobile.models.OrderStatusModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.OrderApiService;
import com.android.mobile.services.UserApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderSuccessFragment extends Fragment {
    private RecyclerView recyclerView;
    private ShippingOrderAdapter adapter;
    private List<OrderStatusModel> orderList = new ArrayList<>();
    private BlankFragment loadingFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_shipping_order, container, false);

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        adapter = new ShippingOrderAdapter(getContext(), orderList);
        recyclerView = view.findViewById(R.id.recycler_shipping_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        fetchOrderSuccess();

        return view;
    }

    private void fetchOrderSuccess() {
        if (getView() == null) return;
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", null);

        UserApiService service = ApiServiceProvider.getUserApiService();
        Call<List<OrderStatusModel>> call = service.getAllOrders("Bearer " + token);

        call.enqueue(new Callback<List<OrderStatusModel>>() {
            @Override
            public void onResponse(Call<List<OrderStatusModel>> call, Response<List<OrderStatusModel>> response) {

                if (response.isSuccessful()) {
                    List<OrderStatusModel> orders = response.body();
                    List<OrderStatusModel> orderList = new ArrayList<>();
                    for (OrderStatusModel order : orders) {
                        if (order.getStatus().equals("thành công") && order.getGiao_hang().equals("đã giao hàng")) {
                            orderList.add(order);
                        }
                    }
                    adapter.setData(orderList);
                    Toast.makeText(getContext(), "Tải dữ liệu thành công 123", Toast.LENGTH_SHORT).show();
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<OrderStatusModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchOrderSuccess();
    }
}