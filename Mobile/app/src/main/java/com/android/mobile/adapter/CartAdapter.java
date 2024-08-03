package com.android.mobile.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.DetailClubActivity;
import com.android.mobile.R;
import com.android.mobile.models.Product;
import com.android.mobile.models.ProductModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    private List<ProductModel> productList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNameProduct;
        public TextView txtPriceProduct;
        public TextView txtQuantityProduct;
        public TextView txtNameSupplier;
        public TextView txtCategory;
        public Button btnRemoveCart;
        public ImageButton btnIncreaseQuantity;
        public ImageButton btnDecreaseQuantity;

        public ViewHolder(View view) {
            super(view);
            txtNameProduct = view.findViewById(R.id.txt_name_stored_item);
            txtPriceProduct = view.findViewById(R.id.txt_price_product);
            txtQuantityProduct = view.findViewById(R.id.textview_quantity);
            txtNameSupplier = view.findViewById(R.id.txt_name_supplier);
            txtCategory = view.findViewById(R.id.txt_category_product);
            btnRemoveCart = view.findViewById(R.id.btn_remove_cart);
            btnIncreaseQuantity = view.findViewById(R.id.button_increase);
            btnDecreaseQuantity = view.findViewById(R.id.button_decrease);
        }
    }

    public CartAdapter(Context context, List<ProductModel> data) {
        this.context = context;
        this.productList = data;
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartAdapter.ViewHolder holder, int position) {
        holder.txtNameProduct.setText(productList.get(position).getProductName());
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = currencyFormat.format(Double.parseDouble(productList.get(position).getUnitPrice()));
        holder.txtPriceProduct.setText(formattedPrice);
        holder.txtQuantityProduct.setText(String.valueOf(productList.get(position).getQuantity()));
        holder.txtNameSupplier.setText("Nhà cung cấp " + productList.get(position).getSupplierName());
        holder.txtCategory.setText("Thể loại: " + productList.get(position).getCategoryName());
        holder.btnRemoveCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Remove " + productList.get(holder.getAdapterPosition()).getProductName(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Increase " + productList.get(holder.getAdapterPosition()).getProductName(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.txtQuantityProduct.getText().toString().equals("1")) {
//            holder.btnDecreaseQuantity.setEnabled(false);
//            holder.btnDecreaseQuantity.setBackgroundColor(ContextCompat.getColor(context, R.color.disabled_button));
                    Toast.makeText(context, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Decrease " + productList.get(holder.getAdapterPosition()).getProductName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setData(List<ProductModel> newData) {
        productList.clear();
        productList.addAll(newData);
        notifyDataSetChanged();
    }
}