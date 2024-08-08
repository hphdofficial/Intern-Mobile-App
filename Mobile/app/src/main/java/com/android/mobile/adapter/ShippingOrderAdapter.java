package com.android.mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.BlankFragment;
import com.android.mobile.OrderDetailsDialogFragment;
import com.android.mobile.R;
import com.android.mobile.SupplierInfoActivity;
import com.android.mobile.models.OrderStatusModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.UserApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShippingOrderAdapter extends RecyclerView.Adapter<ShippingOrderAdapter.ViewHolder> {
    Context context;
    private List<OrderStatusModel> data;
    private UserApiService apiService;
    private BlankFragment loadingFragment;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewStatus;
        public TextView textViewSupplier;
        public RecyclerView recyclerProductList;
        public TextView totalPrice;
        public Button buttonConfirm;
        public Button buttonDetails;

        public ViewHolder(View view) {
            super(view);
            textViewStatus = view.findViewById(R.id.textView13);
            textViewSupplier = view.findViewById(R.id.textView10);
            recyclerProductList = view.findViewById(R.id.recycler_product_list);
            totalPrice = view.findViewById(R.id.total_price);
            buttonConfirm = view.findViewById(R.id.button3);
            buttonDetails = view.findViewById(R.id.button_details);
        }
    }

    public ShippingOrderAdapter(Context context, List<OrderStatusModel> data) {
        this.context = context;
        this.data = data;
        apiService = ApiServiceProvider.getUserApiService();
    }

    @Override
    public ShippingOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shipping_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShippingOrderAdapter.ViewHolder holder, int position) {
        OrderStatusModel order = data.get(position);
        holder.textViewStatus.setText(order.getGiao_hang());

        // Set supplier name
        if (!order.getDetail_carts().isEmpty()) {
            holder.textViewSupplier.setText(order.getDetail_carts().get(0).getProduct().getSupplierName());
        }

        // Add click listener for supplier name
        holder.textViewSupplier.setOnClickListener(v -> {
            if (!order.getDetail_carts().isEmpty()) {
                int supplierId = order.getDetail_carts().get(0).getProduct().getSupplierID();
                Intent intent = new Intent(context, SupplierInfoActivity.class);
                intent.putExtra("SupplierID", supplierId);
                context.startActivity(intent);
            }
        });

        // Setup product list
        holder.recyclerProductList.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerProductList.setAdapter(new ProductShippingAdapter(context, order.getDetail_carts()));

        // Calculate total price
        double totalPrice = 0;
        for (OrderStatusModel.DetailCart cart : order.getDetail_carts()) {
            totalPrice += cart.getProduct().getUnitPrice() * cart.getQuantity();
        }
        holder.totalPrice.setText(String.format("Tổng giá: %,.0f VND", totalPrice));

        // Check delivery status and update button text and color
        if ("đã giao hàng".equals(order.getGiao_hang())) {
            holder.buttonConfirm.setText("Đã nhận hàng");
            holder.buttonConfirm.setBackgroundColor(ContextCompat.getColor(context, R.color.orange_button_color)); // Set button color to orange
            holder.buttonConfirm.setTextColor(ContextCompat.getColor(context, R.color.white)); // Set button text color to white
            holder.buttonConfirm.setEnabled(false); // Disable button if already received
        } else {
            holder.buttonConfirm.setText("Xác nhận nhận hàng");
            holder.buttonConfirm.setBackgroundColor(ContextCompat.getColor(context, R.color.special_color)); // Set button color to default
            holder.buttonConfirm.setTextColor(ContextCompat.getColor(context, R.color.black)); // Set button text color to black (or any other default color)
            holder.buttonConfirm.setEnabled(true); // Enable button if not yet received

            holder.buttonConfirm.setOnClickListener(v -> {
                int adapterPosition = holder.getAdapterPosition();
                String txnRef = order.getTxn_ref(); // Lấy txn_ref từ order

                // Log txnRef and order ID for debugging
                System.out.println("txnRef: " + txnRef);
                System.out.println("Order ID: " + order.getId());

                showLoading(); // Hiển thị loading

                if ("Thanh toán khi nhận hàng".equals(order.getOrder_info())) {
                    // Update status to 'thành công' and 'giao_hang' to 'đã giao hàng'
                    order.setStatus("thành công");
                    order.setGiao_hang("đã giao hàng");
                    apiService.updateDeliveryStatus(txnRef).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            hideLoading();
                            if (response.isSuccessful()) {
                                notifyItemChanged(adapterPosition);
                                Toast.makeText(context, "Trạng thái đơn hàng đã được cập nhật thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Không thể cập nhật trạng thái đơn hàng", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            hideLoading();
                            Toast.makeText(context, "Lỗi khi cập nhật trạng thái đơn hàng", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Existing logic for updating delivery status
                    apiService.updateDeliveryStatus(txnRef).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                // After updating the delivery status, check the order status again
                                apiService.searchOrder(txnRef).enqueue(new Callback<OrderStatusModel>() { // sử dụng txnRef
                                    @Override
                                    public void onResponse(Call<OrderStatusModel> call, Response<OrderStatusModel> response) {
                                        hideLoading(); // Ẩn loading
                                        if (response.isSuccessful() && response.body() != null) {
                                            OrderStatusModel updatedOrder = response.body();
                                            order.setGiao_hang(updatedOrder.getGiao_hang());
                                            notifyItemChanged(adapterPosition);
                                            Toast.makeText(context, "Trạng thái giao hàng đã được cập nhật thành công", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Không thể cập nhật trạng thái giao hàng", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<OrderStatusModel> call, Throwable t) {
                                        hideLoading(); // Ẩn loading khi có lỗi xảy ra
                                        Toast.makeText(context, "Lỗi khi kiểm tra lại trạng thái giao hàng", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                hideLoading(); // Ẩn loading khi có lỗi xảy ra
                                Toast.makeText(context, "Cập nhật trạng thái giao hàng thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            hideLoading();
                            Toast.makeText(context, "Lỗi khi cập nhật trạng thái giao hàng", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        // Set the order details button
        holder.buttonDetails.setOnClickListener(v -> {
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            OrderDetailsDialogFragment dialogFragment = OrderDetailsDialogFragment.newInstance(order);
            dialogFragment.show(fragmentManager, "OrderDetailsDialogFragment");
        });
    }

    private void showLoading() {
        if (loadingFragment == null) {
            loadingFragment = new BlankFragment();
            loadingFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "loading");
        }
    }

    private void hideLoading() {
        if (loadingFragment != null) {
            loadingFragment.dismiss();
            loadingFragment = null;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
