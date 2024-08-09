package com.android.mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.adapter.HistoryOrderAdapter;
import com.android.mobile.models.OrderModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.OrderApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryOrderActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private HistoryOrderAdapter adapter;
    private List<OrderModel> orderList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private BlankFragment loadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        showLoading();

        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Lịch sử mua hàng");
        myContentE.apply();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new titleFragment());
        fragmentTransaction.commit();

        adapter = new HistoryOrderAdapter(this, orderList);
        recyclerView = findViewById(R.id.recycler_purchase_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchOrderHistory();
        });

        fetchOrderHistory();
    }

    private void fetchOrderHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", null);

        OrderApiService service = ApiServiceProvider.getOrderApiService();
        Call<List<OrderModel>> call = service.getHistoryOrder("Bearer " + token);

        call.enqueue(new Callback<List<OrderModel>>() {
            @Override
            public void onResponse(Call<List<OrderModel>> call, Response<List<OrderModel>> response) {
                hideLoading();

                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    List<OrderModel> orders = response.body();
                    List<OrderModel> orderList = new ArrayList<>();
                    for (OrderModel order : orders) {
                        if (order.getStatus().equals("thành công") && order.getGiao_hang().equals("đã giao hàng")) {
                            orderList.add(order);
                        }
                    }
                    adapter.setData(orderList);
                    Toast.makeText(HistoryOrderActivity.this, "Tải dữ liệu thành công", Toast.LENGTH_SHORT).show();
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<OrderModel>> call, Throwable t) {
                hideLoading();

                swipeRefreshLayout.setRefreshing(false);
                t.printStackTrace();
            }
        });
    }

    private void showLoading() {
        loadingFragment = new BlankFragment();
        loadingFragment.show(getSupportFragmentManager(), "loading");
    }

    private void hideLoading() {
        if (loadingFragment != null) {
            loadingFragment.dismiss();
            loadingFragment = null;
        }
    }
}