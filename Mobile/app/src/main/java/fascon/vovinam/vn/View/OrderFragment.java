package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fascon.vovinam.vn.ViewModel.OrderAdapter;
import fascon.vovinam.vn.Model.OrderListModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.OrderApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {
    private OrderApiService apiService;
    private RecyclerView recyclerView;
    private OrderAdapter shippingItemAdapter;
    private String statusOrder;
    private ImageView imgNotify;
    private TextView txtNotify;

    public OrderFragment(String statusOrder) {
        this.statusOrder = statusOrder;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        apiService = ApiServiceProvider.getOrderApiService();
        imgNotify = view.findViewById(R.id.img_notify);
        txtNotify = view.findViewById(R.id.txt_notify);

        shippingItemAdapter = new OrderAdapter(getContext(), new ArrayList<>());
        recyclerView = view.findViewById(R.id.recycler_shipping_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(shippingItemAdapter);

        fetchListOrder();

        return view;
    }

    private void fetchListOrder() {
        shippingItemAdapter.setData(new ArrayList<>());
        imgNotify.setVisibility(View.GONE);
        txtNotify.setText("Loading...");
        if (getView() == null) return;

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);
        apiService.getListOrder("Bearer " + accessToken).enqueue(new Callback<List<OrderListModel>>() {
            @Override
            public void onResponse(Call<List<OrderListModel>> call, Response<List<OrderListModel>> response) {
                if (response.isSuccessful()) {
                    List<OrderListModel> orders = response.body();
                    List<OrderListModel> orderList = new ArrayList<>();
                    if (orders != null && !orders.isEmpty()) {
                        for (OrderListModel order : orders) {
                            if (statusOrder.equalsIgnoreCase(order.getGiao_hang())) {
                                orderList.add(order);
                            }
                        }
                        if (!orderList.isEmpty()) {
                            Collections.sort(orderList, new Comparator<OrderListModel>() {
                                @Override
                                public int compare(OrderListModel o1, OrderListModel o2) {
                                    return o2.getPay_date().compareTo(o1.getPay_date());
                                }
                            });
                            txtNotify.setText("");
                            shippingItemAdapter.setData(orderList);
                        } else {
                            imgNotify.setVisibility(View.VISIBLE);
                            txtNotify.setText("Bạn chưa có đơn hàng nào cả");
                        }
                    } else {
                        imgNotify.setVisibility(View.VISIBLE);
                        txtNotify.setText("Bạn chưa có đơn hàng nào cả");
                    }
                } else {
                    Log.e("Error", response.message());
                    imgNotify.setVisibility(View.VISIBLE);
                    txtNotify.setText("Không thể tải danh sách đơn hàng. Vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<OrderListModel>> call, @NonNull Throwable t) {
                Log.e("Fail", Objects.requireNonNull(t.getMessage()));
                imgNotify.setVisibility(View.VISIBLE);
                txtNotify.setText("Lỗi kết nối. Vui lòng thử lại.");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchListOrder();
    }
}