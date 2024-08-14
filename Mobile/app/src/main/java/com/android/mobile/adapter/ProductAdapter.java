package com.android.mobile.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.R;
import com.android.mobile.models.Product;
import com.android.mobile.models.ProductModel;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ItemViewHolder> {
    Context context;

    private List<ProductModel> list ;
    public ProductAdapter(Context context, List<ProductModel> list ){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ProductAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_product, parent, false);
        return new ItemViewHolder(view);
    }
    private int placeholderResourceId = R.drawable.avatar_anime;
    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ItemViewHolder holder, int position) {
        ProductModel item = list.get(position);

//        holder.name_product.setText(item.getName());
//        holder.type_product.setText(item.getType());
//        holder.supplier_product.setText(item.getSupplier());
//        holder.price_product.setText("Giá tiền: "+item.getPrice()+"");
//        holder.quantity_product.setText("x"+item.getQuantity()+"");
        Glide.with(context)
                .load(item.getImage_link())
                .error(R.drawable.product)
                .into(holder.image_product);
        holder.name_product.setText(item.getProductName());
        holder.type_product.setText("Loại: " + item.getCategoryName());
        holder.supplier_product.setText("Nhà cung cấp: " + item.getSupplierName());
        holder.price_product.setText("Giá tiền: " + item.getUnitPrice() + "");
        holder.quantity_product.setText("x" + item.getQuantity());


        /*Picasso.get()
                .load(holder.image_product)
                .placeholder(placeholderResourceId) // Hình ảnh placeholder
                .error(placeholderResourceId) // Hình ảnh sẽ hiển thị nếu tải lỗi
                .into(holder.image_product);*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        // TextView textViewItem;
        ImageView image_product;
        TextView name_product;
        TextView type_product;
        TextView supplier_product;
        TextView price_product;
        TextView quantity_product;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // textViewItem = itemView.findViewById(R.id.textViewItem);
            image_product = itemView.findViewById(R.id.image_product);
            name_product = itemView.findViewById(R.id.name_product);
            type_product = itemView.findViewById(R.id.type_product);
            supplier_product = itemView.findViewById(R.id.supplier_product);
            price_product = itemView.findViewById(R.id.price_product);
            quantity_product = itemView.findViewById(R.id.quantity_product);


        }
    }
}