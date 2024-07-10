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
import com.android.mobile.models.Item;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ItemSupplierProductAdapter extends RecyclerView.Adapter<ItemSupplierProductAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Item> itemList;

    public ItemSupplierProductAdapter(Context context, ArrayList<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_supplier_product, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Item item = itemList.get(i);
        viewHolder.txtItemName.setText(item.getName());
        viewHolder.txtItemPrice.setText(item.getPrice() + " VND");
        viewHolder.txtItemSupplier.setText(item.getSupplier());

        // Sử dụng Glide để tải ảnh từ URL hoặc từ resource nếu không có URL
        if (item.getImg().isEmpty()) {
            viewHolder.imgItem.setImageResource(R.drawable.gang1); // gang1.jpg phải có trong res/drawable
        } else {
            Glide.with(context).load(item.getImg()).into(viewHolder.imgItem);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtItemName;
        TextView txtItemPrice;
        TextView txtItemSupplier;
        ImageView imgItem;

        ViewHolder(View itemView) {
            super(itemView);
            txtItemName = itemView.findViewById(R.id.txtItemName);
            txtItemPrice = itemView.findViewById(R.id.txtItemPrice);
            txtItemSupplier = itemView.findViewById(R.id.txtItemSupplier);
            imgItem = itemView.findViewById(R.id.imgItem);
        }
    }
}
