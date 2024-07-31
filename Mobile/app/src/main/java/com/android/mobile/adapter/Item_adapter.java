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
import com.android.mobile.models.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class Item_adapter extends RecyclerView.Adapter<Item_adapter.ViewHolder>{
    Context context;

    ProductModel[] ProductList;

    public Item_adapter(Context context, ProductModel[] ProductList) {
        this.context = context;
        this.ProductList = ProductList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_item_new, viewGroup, false);
        return new Item_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String txtItemName = ProductList[i].getProductName();
        viewHolder.txtProductName.setText(txtItemName);

        String txtItemPrice = ProductList[i].getUnitPrice();
        viewHolder.txtProductPrice.setText(txtItemPrice+ " VND");

        int txtProductInStock = ProductList[i].getUnitsInStock();
        viewHolder.txtProductInStock.setText("Còn: "+txtProductInStock);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, activity_item_detail.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ProductList.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName;
        TextView txtProductPrice;
        TextView txtProductInStock;

        ViewHolder(View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            txtProductInStock = itemView.findViewById(R.id.txtProductInStock);
        }
    }
}
