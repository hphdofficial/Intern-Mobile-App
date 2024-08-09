package com.android.mobile.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.HistoryOrderActivity;
import com.android.mobile.R;
import com.android.mobile.models.OrderModel;
import com.android.mobile.models.Product;
import com.android.mobile.models.ProductModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.OrderApiService;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryOrderAdapter extends RecyclerView.Adapter<HistoryOrderAdapter.ViewHolder> {
    Context context;
    private List<OrderModel> orderList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTransactionCode;
        public TextView txtOrderCode;
        public TextView txtBank;
        public TextView txtTotal;
        public TextView txtDate;
        public Button btnViewProduct;

        public ViewHolder(View view) {
            super(view);
            txtTransactionCode = view.findViewById(R.id.txt_transaction_code);
            txtOrderCode = view.findViewById(R.id.txt_name_history_order);
            txtBank = view.findViewById(R.id.txt_bank_code);
            txtTotal = view.findViewById(R.id.txt_price_order);
            txtDate = view.findViewById(R.id.txt_pay_date);
            btnViewProduct = view.findViewById(R.id.btn_view_list_product);
        }
    }

    public HistoryOrderAdapter(Context context, List<OrderModel> data) {
        this.context = context;
        this.orderList = data;
    }

    @Override
    public HistoryOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryOrderAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (orderList.get(position).getTransaction_no() != null) {
            holder.txtTransactionCode.setText("Mã giao dịch " + orderList.get(position).getTransaction_no());
        } else {
            holder.txtTransactionCode.setText("Mã giao dịch " + orderList.get(position).getTxn_ref());
        }
        holder.txtOrderCode.setText("Đơn hàng " + orderList.get(position).getTxn_ref());
        if (orderList.get(position).getBank_code() != null) {
            holder.txtBank.setText("Loại thanh toán: " + orderList.get(position).getBank_code());
        } else {
            holder.txtBank.setText("Loại thanh toán: Trực tiếp");
        }
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = currencyFormat.format(Double.parseDouble(orderList.get(position).getAmount()));
        holder.txtTotal.setText(formattedPrice);
        holder.txtDate.setText(convertDateFormat(orderList.get(position).getPay_date()));
        holder.btnViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListProduct(orderList.get(position).getId());
                Toast.makeText(context, "Đang tải...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void setData(List<OrderModel> newData) {
        orderList.clear();
        orderList.addAll(newData);
        notifyDataSetChanged();
    }

    public static String convertDateFormat(String inputDate) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "HH:mm:ss dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String formattedDate = null;
        try {
            date = inputFormat.parse(inputDate);
            formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    private void viewListProduct(int orderId) {
        Dialog productDialog = new Dialog(context);
        productDialog.setContentView(R.layout.dialog_list_product);

        ImageButton closeButton = productDialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDialog.dismiss();
            }
        });
        productDialog.show();

        List<ProductModel> productList = new ArrayList<>();
        CartAdapter adapter = new CartAdapter(context, productList, true);
        RecyclerView recyclerView = productDialog.findViewById(R.id.recycler_view_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        OrderApiService service = ApiServiceProvider.getOrderApiService();
        Call<List<ProductModel>> call = service.getListProductOrder(orderId);

        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if (response.isSuccessful()) {
                    List<ProductModel> products = response.body();
                    adapter.setData(products);
                    Toast.makeText(context, "Tải dữ liệu thành công", Toast.LENGTH_SHORT).show();
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}