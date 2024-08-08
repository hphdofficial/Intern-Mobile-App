package com.android.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.R;
import com.android.mobile.models.OrderStatusModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProductShippingAdapter extends RecyclerView.Adapter<ProductShippingAdapter.ViewHolder> {
    private Context context;
    private List<OrderStatusModel.DetailCart> productList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewAmount;
        public ImageView imageViewProduct;
        public TextView textViewQuantity;

        public ViewHolder(View view) {
            super(view);
            textViewName = view.findViewById(R.id.txt_name_shipping_item);
            textViewAmount = view.findViewById(R.id.txt_price_product);
            imageViewProduct = view.findViewById(R.id.imageView4);
            textViewQuantity = view.findViewById(R.id.txt_quantity_shipping_item);
        }
    }

    public ProductShippingAdapter(Context context, List<OrderStatusModel.DetailCart> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public ProductShippingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_shipping, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductShippingAdapter.ViewHolder holder, int position) {
        OrderStatusModel.DetailCart product = productList.get(position);
        holder.textViewName.setText(product.getProduct().getProductName());
        holder.textViewAmount.setText(String.format("%,.0f VND", product.getProduct().getUnitPrice()));
        holder.textViewQuantity.setText("x " + product.getQuantity());

        /// Load product image using Glide
        Glide.with(context)
                .load(product.getProduct().getLink_image())
                .error(R.drawable.product) // Replace 'default_image' with your drawable resource
                .into(holder.imageViewProduct);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
