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
import com.android.mobile.models.OrderModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.OrderApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderSuccessFragment extends Fragment {
    private RecyclerView recyclerView;
    private HistoryOrderAdapter adapter;
    private List<OrderModel> orderList = new ArrayList<>();
    private BlankFragment loadingFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_success, container, false);

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        showLoading();

        adapter = new HistoryOrderAdapter(getContext(), orderList);
        recyclerView = view.findViewById(R.id.recycler_purchase_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        fetchOrderSuccess();

        return view;
    }

    private void fetchOrderSuccess() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", null);

        OrderApiService service = ApiServiceProvider.getOrderApiService();
        Call<List<OrderModel>> call = service.getHistoryOrder("Bearer " + token);

        call.enqueue(new Callback<List<OrderModel>>() {
            @Override
            public void onResponse(Call<List<OrderModel>> call, Response<List<OrderModel>> response) {
                hideLoading();

                if (response.isSuccessful()) {
                    List<OrderModel> orders = response.body();
                    List<OrderModel> orderList = new ArrayList<>();
                    for (OrderModel order : orders) {
                        if (order.getStatus().equals("thành công") && order.getGiao_hang().equals("đã giao hàng")) {
                            orderList.add(order);
                        }
                    }
                    adapter.setData(orderList);
                    Toast.makeText(getContext(), "Tải dữ liệu thành công", Toast.LENGTH_SHORT).show();
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<OrderModel>> call, Throwable t) {
                hideLoading();

                t.printStackTrace();
            }
        });
    }

    private void showLoading() {
        loadingFragment = new BlankFragment();
        loadingFragment.show(getParentFragmentManager(), "loading");
    }

    private void hideLoading() {
        if (loadingFragment != null) {
            loadingFragment.dismiss();
            loadingFragment = null;
        }
    }
}