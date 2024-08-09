package com.android.mobile.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.CartActivity;
import com.android.mobile.R;
import com.android.mobile.models.CartItem;
import com.android.mobile.models.ProductModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.CartApiService;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    private List<ProductModel> productList;
    private CartActivity cartActivity;
    private Boolean isViewMode = false;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNameProduct;
        public TextView txtPriceProduct;
        public TextView txtQuantityProduct;
        public TextView txtNameSupplier;
        public TextView txtCategory;
        public Button btnRemoveCart;
        public ImageButton btnIncreaseQuantity;
        public ImageButton btnDecreaseQuantity;
        public ImageView imageViewProduct;

        public ViewHolder(View view) {
            super(view);
            imageViewProduct = view.findViewById(R.id.imageViewProductCart);
            txtNameProduct = view.findViewById(R.id.txt_name_stored_item);
            txtPriceProduct = view.findViewById(R.id.txt_price_product);
            txtQuantityProduct = view.findViewById(R.id.textview_quantity);
            txtNameSupplier = view.findViewById(R.id.txt_name_supplier);
            txtCategory = view.findViewById(R.id.txt_category_product);
            btnRemoveCart = view.findViewById(R.id.btn_remove_cart);
            btnIncreaseQuantity = view.findViewById(R.id.button_increase);
            btnDecreaseQuantity = view.findViewById(R.id.button_decrease);
        }
    }

    public CartAdapter(Context context, List<ProductModel> data, CartActivity cartActivity) {
        this.context = context;
        this.productList = data;
        this.cartActivity = cartActivity;
    }

    public CartAdapter(Context context, List<ProductModel> data, Boolean viewMode) {
        this.context = context;
        this.productList = data;
        this.isViewMode = viewMode;
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context)
                .load(productList.get(position).getImage_link())
                .error(R.drawable.product)
                .into(holder.imageViewProduct);
        holder.txtNameProduct.setText(productList.get(position).getProductName());
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = currencyFormat.format(Double.parseDouble(productList.get(position).getUnitPrice()));
        holder.txtPriceProduct.setText(formattedPrice);
        holder.txtQuantityProduct.setText(String.valueOf(productList.get(position).getQuantity()));
        holder.txtNameSupplier.setText("Nhà cung cấp " + productList.get(position).getSupplierName());
        holder.txtCategory.setText("Thể loại: " + productList.get(position).getCategoryName());
        holder.btnRemoveCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeProduct(productList.get(position).getProductID());
                Toast.makeText(context, "Xóa sản phẩm " + productList.get(holder.getAdapterPosition()).getProductName(), Toast.LENGTH_SHORT).show();
            }
        });

        if (isViewMode) {
            holder.btnRemoveCart.setVisibility(View.GONE);
            holder.btnIncreaseQuantity.setVisibility(View.GONE);
            holder.btnDecreaseQuantity.setVisibility(View.GONE);
            holder.txtQuantityProduct.setText("x" + productList.get(position).getQuantity());
            int widthInDp = 150;
            int widthInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthInDp, holder.itemView.getResources().getDisplayMetrics());
            ViewGroup.LayoutParams layoutParams = holder.txtNameProduct.getLayoutParams();
            layoutParams.width = widthInPx;
            holder.txtNameProduct.setLayoutParams(layoutParams);
        }

        holder.btnIncreaseQuantity.setEnabled(true);
        holder.btnDecreaseQuantity.setEnabled(true);

        holder.btnIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseQuantity(productList.get(position).getProductID(), holder);
                Toast.makeText(context, "Tăng sản phẩm " + productList.get(holder.getAdapterPosition()).getProductName(), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Đang cập nhật giỏ hàng", Toast.LENGTH_SHORT).show();
                holder.btnIncreaseQuantity.setEnabled(false);
            }
        });

        holder.btnDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.txtQuantityProduct.getText().toString().equals("1")) {
                    Toast.makeText(context, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
                } else {
                    decreaseQuantity(productList.get(position).getProductID(), holder);
                    Toast.makeText(context, "Giảm sản phẩm " + productList.get(holder.getAdapterPosition()).getProductName(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Đang cập nhật giỏ hàng", Toast.LENGTH_SHORT).show();
                    holder.btnDecreaseQuantity.setEnabled(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setData(List<ProductModel> newData) {
        productList.clear();
        productList.addAll(newData);
        notifyDataSetChanged();
    }

    public void removeProduct(int productId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        int memberId = sharedPreferences.getInt("member_id", 0);

        CartApiService service = ApiServiceProvider.getCartApiService();
        Call<JsonObject> call = service.removeProduct(new CartItem(memberId, productId));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    cartActivity.loadProductCart();
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void increaseQuantity(int productId, CartAdapter.ViewHolder holder) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        int memberId = sharedPreferences.getInt("member_id", 0);

        CartApiService service = ApiServiceProvider.getCartApiService();
        Call<JsonObject> call = service.increaseQuantity(new CartItem(memberId, productId));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                holder.btnIncreaseQuantity.setEnabled(true);
                if (response.isSuccessful()) {
                    cartActivity.loadProductCart();
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                holder.btnIncreaseQuantity.setEnabled(true);
                t.printStackTrace();
            }
        });
    }

    public void decreaseQuantity(int productId, CartAdapter.ViewHolder holder) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        int memberId = sharedPreferences.getInt("member_id", 0);

        CartApiService service = ApiServiceProvider.getCartApiService();
        Call<JsonObject> call = service.decreaseQuantity(new CartItem(memberId, productId));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                holder.btnDecreaseQuantity.setEnabled(true);
                if (response.isSuccessful()) {
                    cartActivity.loadProductCart();
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                holder.btnDecreaseQuantity.setEnabled(true);
                t.printStackTrace();
            }
        });
    }
}