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
import java.util.Locale;

public class ProductShippingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Object> items;

    private static final int TYPE_SUPPLIER = 0;
    private static final int TYPE_PRODUCT = 1;

    public static class SupplierViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewSupplierName;

        public SupplierViewHolder(View view) {
            super(view);
            textViewSupplierName = view.findViewById(R.id.txt_supplier_name);
        }
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewAmount;
        public ImageView imageViewProduct;
        public TextView textViewQuantity;

        public ProductViewHolder(View view) {
            super(view);
            textViewName = view.findViewById(R.id.txt_name_shipping_item);
            textViewAmount = view.findViewById(R.id.txt_price_product);
            imageViewProduct = view.findViewById(R.id.imageView4);
            textViewQuantity = view.findViewById(R.id.txt_quantity_shipping_item);
        }
    }

    public ProductShippingAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof String) {
            return TYPE_SUPPLIER;
        } else {
            return TYPE_PRODUCT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SUPPLIER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_supplier_header, parent, false);
            return new SupplierViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_shipping, parent, false);
            return new ProductViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_SUPPLIER) {
            String supplierName = (String) items.get(position);
            SupplierViewHolder supplierViewHolder = (SupplierViewHolder) holder;
            supplierViewHolder.textViewSupplierName.setText(supplierName);
        } else {
            OrderStatusModel.DetailCart product = (OrderStatusModel.DetailCart) items.get(position);
            ProductViewHolder productViewHolder = (ProductViewHolder) holder;
            productViewHolder.textViewName.setText(product.getProduct().getProductName());
            productViewHolder.textViewAmount.setText(String.format("%,.0f VND", product.getProduct().getUnitPrice()));
            productViewHolder.textViewQuantity.setText("x " + product.getQuantity());

            Glide.with(context)
                    .load(product.getProduct().getLink_image())
                    .error(R.drawable.product)
                    .into(productViewHolder.imageViewProduct);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
