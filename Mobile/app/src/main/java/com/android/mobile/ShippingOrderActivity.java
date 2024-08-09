package com.android.mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.adapter.ShippingOrderAdapter;
import com.android.mobile.models.OrderStatusModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.UserApiService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShippingOrderActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private TextView noOrdersTextView;
    private ShippingOrderAdapter shippingItemAdapter;
    private List<OrderStatusModel> orderList;
    private UserApiService apiService;
    private BlankFragment loadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shipping_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Đơn hàng đang vận chuyển");
        myContentE.apply();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new titleFragment());
        fragmentTransaction.commit();

        recyclerView = findViewById(R.id.recycler_shipping_item);
        noOrdersTextView = findViewById(R.id.no_orders_text);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize API service
        apiService = ApiServiceProvider.getUserApiService();

        // Fetch orders
        fetchAllOrders();
    }

    private void showLoading() {
        if (loadingFragment == null) {
            loadingFragment = new BlankFragment();
            loadingFragment.show(getSupportFragmentManager(), "loading");
        }
    }

    private void hideLoading() {
        if (loadingFragment != null) {
            loadingFragment.dismiss();
            loadingFragment = null;
        }
    }

    private void fetchAllOrders() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        showLoading(); // Hiển thị loading
        apiService.getAllOrders("Bearer " + accessToken).enqueue(new Callback<List<OrderStatusModel>>() {
            @Override
            public void onResponse(Call<List<OrderStatusModel>> call, Response<List<OrderStatusModel>> response) {
                hideLoading(); // Ẩn loading
                if (response.isSuccessful()) {
                    orderList = response.body();
                    if (orderList != null && !orderList.isEmpty()) {
                        // Sort orders by pay_date in descending order
                        Collections.sort(orderList, new Comparator<OrderStatusModel>() {
                            @Override
                            public int compare(OrderStatusModel o1, OrderStatusModel o2) {
                                return o2.getPay_date().compareTo(o1.getPay_date());
                            }
                        });
                        noOrdersTextView.setVisibility(View.GONE); // Ẩn TextView nếu có đơn hàng
                        shippingItemAdapter = new ShippingOrderAdapter(ShippingOrderActivity.this, orderList);
                        recyclerView.setAdapter(shippingItemAdapter);
                    } else {
                        // Hiển thị thông báo TextView khi không có hóa đơn nào
                        noOrdersTextView.setVisibility(View.VISIBLE);
                    }
                } else {
                    noOrdersTextView.setVisibility(View.VISIBLE);
                    // Xử lý lỗi khi phản hồi không thành công
                    // Toast.makeText(ShippingOrderActivity.this, "Bạn chưa có hóa đơn nào", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderStatusModel>> call, Throwable t) {
                hideLoading(); // Ẩn loading khi có lỗi xảy ra
                // Xử lý lỗi khi có lỗi xảy ra trong quá trình gọi API
                Toast.makeText(ShippingOrderActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
