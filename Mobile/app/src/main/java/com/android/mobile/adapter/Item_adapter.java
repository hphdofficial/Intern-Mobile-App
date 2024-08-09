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
import com.android.mobile.activity_item_detail;
import com.android.mobile.models.ProductModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Item_adapter extends RecyclerView.Adapter<Item_adapter.ViewHolder>{
    Context context;

    List<ProductModel> ProductList;

    public Item_adapter(Context context, List<ProductModel> ProductList) {
        this.context = context;
        this.ProductList = ProductList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_supplier_product, viewGroup, false);
        return new Item_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String txtItemName = ProductList.get(i).getProductName();
        viewHolder.txtProductName.setText(txtItemName);

        String txtItemPrice = ProductList.get(i).getUnitPrice();
        viewHolder.txtProductPrice.setText(txtItemPrice+ " VND");

        int txtProductInStock = ProductList.get(i).getUnitsInStock();
        viewHolder.txtProductInStock.setText("Còn: "+txtProductInStock);

        String image = ProductList.get(i).getImage_link();
        if (image != null) {
            Picasso.get().load(image).placeholder(R.drawable.logo_vovinam).into(viewHolder.imgProductImage);
        }else{
            viewHolder.imgProductImage.setImageResource(R.drawable.logo_vovinam);
        }

        int idProduct = ProductList.get(i).getProductID();
        int idSupplier = ProductList.get(i).getSupplierID();

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, activity_item_detail.class);
                intent.putExtra("id", idProduct);
                intent.putExtra("IDSupplier", idSupplier);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ProductList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName;
        TextView txtProductPrice;
        TextView txtProductInStock;
        ImageView imgProductImage;

        ViewHolder(View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtItemName);
            txtProductPrice = itemView.findViewById(R.id.txtItemPrice);

            //Text hàng tồn
            txtProductInStock = itemView.findViewById(R.id.txtItemSupplier);
            imgProductImage = itemView.findViewById(R.id.imgItem);
        }
    }
}
