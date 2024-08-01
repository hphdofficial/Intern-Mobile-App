package com.android.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.R;
import com.android.mobile.models.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    private List<Product> productList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNameProduct;
        public TextView txtPriceProduct;

        public ViewHolder(View view) {
            super(view);
            txtNameProduct = view.findViewById(R.id.txt_name_stored_item);
            txtPriceProduct = view.findViewById(R.id.txt_price_product);
        }
    }

    public CartAdapter(Context context, List<Product> data) {
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
        holder.txtNameProduct.setText(productList.get(position).getName());
        holder.txtPriceProduct.setText(String.valueOf(productList.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setData(List<Product> newData) {
        productList.clear();
        productList.addAll(newData);
        notifyDataSetChanged();
    }
}