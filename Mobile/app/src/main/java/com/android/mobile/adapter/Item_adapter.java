package com.android.mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.R;
import com.android.mobile.activity_item_chapter;
import com.android.mobile.activity_item_detail;
import com.android.mobile.models.Chapter;
import com.android.mobile.models.Item;

import java.util.ArrayList;

public class Item_adapter extends RecyclerView.Adapter<Item_adapter.ViewHolder>{
    Context context;

    ArrayList<Item> itemList = new ArrayList<>();

    public Item_adapter(Context context, ArrayList<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_item, viewGroup, false);
        return new Item_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String txtItemName = itemList.get(i).getName();
        viewHolder.txtItemName.setText(txtItemName);

        int txtItemPrice = itemList.get(i).getPrice();
        System.out.println(txtItemPrice);
        viewHolder.txtItemPrice.setText(txtItemPrice+ " VND");

        String txtItemSupplier = itemList.get(i).getSupplier();
        viewHolder.txtItemSupplier.setText(txtItemSupplier);

        String txtItemDateProduct = itemList.get(i).getDateProduct();
        String txtItemMaterial = itemList.get(i).getMaterial();
        String txtItemUse = itemList.get(i).getUse();
        String txtItemType = itemList.get(i).getType();
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, activity_item_detail.class);
                intent.putExtra("name", txtItemName);
                intent.putExtra("price", txtItemPrice);
                intent.putExtra("supplier", txtItemSupplier);
                intent.putExtra("dateProduct", txtItemDateProduct);
                intent.putExtra("material", txtItemMaterial);
                intent.putExtra("use", txtItemUse);
                intent.putExtra("type", txtItemType);
                context.startActivity(intent);
            }
        });
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
