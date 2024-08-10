//package com.android.mobile;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.android.mobile.adapter.BaseActivity;
//import com.android.mobile.adapter.ShippingOrderAdapter;
//import com.android.mobile.models.OrderListModel;
//import com.android.mobile.network.ApiServiceProvider;
//import com.android.mobile.services.UserApiService;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class ShippingOrderActivity extends BaseActivity {
//
//    private RecyclerView recyclerView;
//    private TextView noOrdersTextView;
//    private ShippingOrderAdapter shippingItemAdapter;
//    private List<OrderListModel> orderList;
//    private UserApiService apiService;
//    private BlankFragment loadingFragment;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.fragment_order);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
//        SharedPreferences.Editor myContentE = myContent.edit();
//        myContentE.putString("title", "Đơn hàng đang vận chuyển");
//        myContentE.apply();
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container, new titleFragment());
//        fragmentTransaction.commit();
//
//        recyclerView = findViewById(R.id.recycler_shipping_item);
//        noOrdersTextView = findViewById(R.id.no_orders_text);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // Initialize API service
//        apiService = ApiServiceProvider.getUserApiService();
//
//        // Fetch orders
//        fetchAllOrders();
//    }
//
//    private void showLoading() {
//        if (loadingFragment == null) {
//            loadingFragment = new BlankFragment();
//            loadingFragment.show(getSupportFragmentManager(), "loading");
//        }
//    }
//
//    private void hideLoading() {
//        if (loadingFragment != null) {
//            loadingFragment.dismiss();
//            loadingFragment = null;
//        }
//    }
//
//    private void fetchAllOrders() {
//        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
//        String accessToken = sharedPreferences.getString("access_token", null);
//
//        showLoading(); // Hiển thị loading
//        apiService.getAllOrders("Bearer " + accessToken).enqueue(new Callback<List<OrderListModel>>() {
//            @Override
//            public void onResponse(Call<List<OrderListModel>> call, Response<List<OrderListModel>> response) {
//                hideLoading(); // Ẩn loading
//                if (response.isSuccessful()) {
//                    orderList = response.body();
//                    if (orderList != null && !orderList.isEmpty()) {
//                        // Lọc các đơn hàng có trạng thái "chưa giao hàng"
//                        List<OrderListModel> undeliveredOrders = new ArrayList<>();
//                        for (OrderListModel order : orderList) {
//                            if ("chưa giao hàng".equalsIgnoreCase(order.getGiao_hang())) {
//                                undeliveredOrders.add(order);
//                            }
//                        }
//
//                        if (!undeliveredOrders.isEmpty()) {
//                            // Sắp xếp các đơn hàng theo ngày thanh toán giảm dần
//                            Collections.sort(undeliveredOrders, new Comparator<OrderListModel>() {
//                                @Override
//                                public int compare(OrderListModel o1, OrderListModel o2) {
//                                    return o2.getPay_date().compareTo(o1.getPay_date());
//                                }
//                            });
//
//                            // Ẩn TextView nếu có đơn hàng
//                            noOrdersTextView.setVisibility(View.GONE);
//
//                            // Cập nhật Adapter với danh sách đã lọc
//                            shippingItemAdapter = new ShippingOrderAdapter(ShippingOrderActivity.this, undeliveredOrders);
//                            recyclerView.setAdapter(shippingItemAdapter);
//                        } else {
//                            // Hiển thị TextView nếu không có đơn hàng nào chưa giao hàng
//                            noOrdersTextView.setVisibility(View.VISIBLE);
//                            Toast.makeText(ShippingOrderActivity.this, "Không có đơn hàng nào chưa giao hàng", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        // Hiển thị thông báo TextView khi không có hóa đơn nào
//                        noOrdersTextView.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    noOrdersTextView.setVisibility(View.VISIBLE);
//                    // Xử lý lỗi khi phản hồi không thành công
//                    Toast.makeText(ShippingOrderActivity.this, "Không thể tải đơn hàng", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<OrderListModel>> call, Throwable t) {
//                hideLoading(); // Ẩn loading khi có lỗi xảy ra
//                // Xử lý lỗi khi có lỗi xảy ra trong quá trình gọi API
//                Toast.makeText(ShippingOrderActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//}