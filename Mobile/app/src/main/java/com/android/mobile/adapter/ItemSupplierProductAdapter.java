// ItemSupplierProductAdapter.java
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
import com.android.mobile.models.SupplierModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.SupplierApiService;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemSupplierProductAdapter extends RecyclerView.Adapter<ItemSupplierProductAdapter.ViewHolder> {
    Context context;
    List<ProductModel> productList;

    public ItemSupplierProductAdapter(Context context, List<ProductModel> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_supplier_product, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ProductModel product = productList.get(i);

        viewHolder.txtItemName.setText(product.getProductName());
        viewHolder.txtItemPrice.setText(product.getUnitPrice() + " VND");

        // Fetch supplier name
        SupplierApiService apiService = ApiServiceProvider.getSupplierApiService();
        apiService.getSupplier(product.getSupplierID()).enqueue(new Callback<SupplierModel>() {
            @Override
            public void onResponse(Call<SupplierModel> call, Response<SupplierModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    viewHolder.txtItemSupplier.setText(response.body().getSupplierName());
                } else {
                    viewHolder.txtItemSupplier.setText("Unknown Supplier");
                }
            }

            @Override
            public void onFailure(Call<SupplierModel> call, Throwable t) {
                viewHolder.txtItemSupplier.setText("Unknown Supplier");
            }
        });

        String imageLink = product.getImage_link();

        if (imageLink != null && !imageLink.trim().isEmpty()) {
            Picasso.get()
                    .load(imageLink)
                    .placeholder(R.drawable.product) // Hình ảnh placeholder khi đang tải
                    .error(R.drawable.logo_vovinam)  // Hình ảnh hiển thị khi có lỗi
                    .into(viewHolder.imgItem);
        } else {
            viewHolder.imgItem.setImageResource(R.drawable.product); // Sử dụng hình ảnh mặc định
        }

        viewHolder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, activity_item_detail.class);
            intent.putExtra("id", product.getProductID());
            intent.putExtra("IDSupplier", product.getSupplierID());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
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
