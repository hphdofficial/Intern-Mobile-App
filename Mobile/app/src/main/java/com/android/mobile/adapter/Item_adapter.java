package com.android.mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_item, viewGroup, false);
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

        String txtItemCategory = ProductList.get(i).getCategoryName();
        System.out.println(txtItemCategory);

        String image = ProductList.get(i).getImage_link();

        int amazing = ProductList.get(i).getNoibat();
        if(amazing != 1){
            viewHolder.txtAmazing.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.txtAmazing.setVisibility(View.VISIBLE);
        }

        String sale = ProductList.get(i).getSale();

        if(sale.equals("0")){
            viewHolder.txtProductPriceSale.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.txtProductPriceSale.setVisibility(View.VISIBLE);

            viewHolder.txtProductPrice.setPaintFlags(viewHolder.txtProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            float percent = Float.parseFloat(sale.toString());
            System.out.println("ABC"+percent);
            int intPercent = (int) (percent*100);
            System.out.println("ABC"+intPercent);
            int txtProductPriceSale = Integer.parseInt(txtItemPrice) - (Integer.parseInt(txtItemPrice)*intPercent)/100;
            viewHolder.txtProductPriceSale.setText("-"+intPercent+"% "+txtProductPriceSale+" VND");
        }

        if(image.isEmpty() || image.equals(" ")){
            viewHolder.imgProductImage.setImageResource(R.drawable.logo_vovinam);
        }else{
            Picasso.get().load(image).placeholder(R.drawable.logo_vovinam).into(viewHolder.imgProductImage);
        }


//        if (!image.isEmpty() || !image.equals(" ")) {
//            Picasso.get().load(image).placeholder(R.drawable.logo_vovinam).into(viewHolder.imgProductImage);
//        }else{
//            viewHolder.imgProductImage.setImageResource(R.drawable.logo_vovinam);
//        }

        int idProduct = ProductList.get(i).getProductID();
        int idSupplier = ProductList.get(i).getSupplierID();

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, activity_item_detail.class);
                intent.putExtra("id", idProduct);
                intent.putExtra("IDSupplier", idSupplier);
                intent.putExtra("categoryName", txtItemCategory);
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
        TextView txtProductPriceSale;
        ImageView imgProductImage;
        TextView txtAmazing;

        ViewHolder(View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtItemName);
            txtProductPrice = itemView.findViewById(R.id.txtItemPrice);
            txtProductPriceSale = itemView.findViewById(R.id.txtItemPriceSale);
            //Text hàng tồn
            txtProductInStock = itemView.findViewById(R.id.txtItemSupplier);
            imgProductImage = itemView.findViewById(R.id.imgItem);
            txtAmazing = itemView.findViewById(R.id.txtAmazing);
        }
    }
}
