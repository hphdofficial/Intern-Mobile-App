package fascon.vovinam.vn.ViewModel;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import fascon.vovinam.vn.Model.Product;
import fascon.vovinam.vn.View.AddItemFragment;
import fascon.vovinam.vn.View.ApproveOrderActivity;
import fascon.vovinam.vn.View.CartActivity;
import fascon.vovinam.vn.R;
import fascon.vovinam.vn.Model.CartItem;
import fascon.vovinam.vn.Model.ProductModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.CartApiService;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
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
    private Boolean isEditMode = false;
    private Boolean isAddMode = false;

    private String languageS;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNameProduct;
        public TextView txtPriceProduct;
        public TextView txtPriceSale;
        public TextView txtSale;
        public TextView txtQuantityProduct;
        public TextView txtNameSupplier;
        public TextView txtCategory;
        public Button btnRemoveCart;
        public Button btnAddProduct;
        public ImageButton btnIncreaseQuantity;
        public ImageButton btnDecreaseQuantity;
        public ImageView imageViewProduct;

        public ViewHolder(View view) {
            super(view);
            imageViewProduct = view.findViewById(R.id.imageViewProductCart);
            txtNameProduct = view.findViewById(R.id.txt_name_stored_item);
            txtPriceProduct = view.findViewById(R.id.txt_price_product);
            txtPriceSale = view.findViewById(R.id.txt_price_sale);
            txtSale = view.findViewById(R.id.txt_sale);
            txtQuantityProduct = view.findViewById(R.id.textview_quantity);
            txtNameSupplier = view.findViewById(R.id.txt_name_supplier);
            txtCategory = view.findViewById(R.id.txt_category_product);
            btnRemoveCart = view.findViewById(R.id.btn_remove_cart);
            btnAddProduct = view.findViewById(R.id.btn_add_product);
            btnIncreaseQuantity = view.findViewById(R.id.button_increase);
            btnDecreaseQuantity = view.findViewById(R.id.button_decrease);

        }
    }

    public CartAdapter(Context context, List<ProductModel> data, CartActivity cartActivity) {
        this.context = context;
        this.productList = data;
        this.cartActivity = cartActivity;
    }

    public CartAdapter(Context context, List<ProductModel> data, boolean isEdit) {
        this.context = context;
        this.productList = data;
        this.isEditMode = isEdit;
    }

    public CartAdapter(Context context, List<ProductModel> data, boolean isEdit, boolean isAdd) {
        this.context = context;
        this.productList = data;
        this.isEditMode = isEdit;
        this.isAddMode = isAdd;
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        Glide.with(context)
                .load(productList.get(position).getImage_link())
                .error(R.drawable.product)
                .into(holder.imageViewProduct);
        holder.txtNameProduct.setText(productList.get(position).getProductName());
        String formattedPrice = currencyFormat.format(Double.parseDouble(productList.get(position).getUnitPrice()));
        holder.txtPriceProduct.setText(formattedPrice);
        holder.txtPriceProduct.setPaintFlags(holder.txtPriceProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        float percent = Float.parseFloat(productList.get(position).getSale().toString());
        int intPercent = (int) (percent * 100);
        int txtProductPriceSale = Integer.parseInt(productList.get(position).getUnitPrice()) - (Integer.parseInt(productList.get(position).getUnitPrice()) * intPercent) / 100;

        String formattedPriceSale = currencyFormat.format(Double.parseDouble(String.valueOf(txtProductPriceSale)));
        holder.txtPriceSale.setText(formattedPriceSale);
        holder.txtSale.setText("-" + intPercent + "%");
        holder.txtQuantityProduct.setText(String.valueOf(productList.get(position).getQuantity()));
        holder.txtNameSupplier.setText("Nhà cung cấp " + productList.get(position).getSupplierName());
        holder.txtCategory.setText("Thể loại: " + productList.get(position).getCategoryName());

        SharedPreferences shared = context.getSharedPreferences("login_prefs", context.MODE_PRIVATE);
        languageS = shared.getString("language",null);

        if(languageS != null){
            if (languageS.contains("en")){
                holder.txtNameSupplier.setText("Supplier " + productList.get(position).getSupplierName());
                holder.txtCategory.setText("Type: " + productList.get(position).getCategoryName());
                holder.btnRemoveCart.setText("Delete");
            }
        }
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
        holder.btnRemoveCart.setEnabled(true);

        if (!isEditMode) {
            holder.btnIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.btnIncreaseQuantity.setEnabled(false);
                    increaseQuantity(productList.get(position).getProductID(), holder);
                    Toast.makeText(context, "Tăng sản phẩm " + productList.get(holder.getAdapterPosition()).getProductName(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Đang cập nhật giỏ hàng...", Toast.LENGTH_SHORT).show();
                }
            });

            holder.btnDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.txtQuantityProduct.getText().toString().equals("1")) {
                        Toast.makeText(context, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
                    } else {
                        holder.btnDecreaseQuantity.setEnabled(false);
                        decreaseQuantity(productList.get(position).getProductID(), holder);
                        Toast.makeText(context, "Giảm sản phẩm " + productList.get(holder.getAdapterPosition()).getProductName(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "Đang cập nhật giỏ hàng...", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.btnRemoveCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.btnRemoveCart.setEnabled(false);
                    removeProduct(productList.get(position).getProductID(), holder);
                    Toast.makeText(context, "Xóa sản phẩm " + productList.get(holder.getAdapterPosition()).getProductName(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Đang cập nhật giỏ hàng...", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (!isAddMode) {
            holder.btnIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String currentQuantity = holder.txtQuantityProduct.getText().toString();
                    int newQuantity = Integer.parseInt(currentQuantity) + 1;
                    holder.txtQuantityProduct.setText(String.valueOf(newQuantity));
                    productList.get(position).setQuantity(newQuantity);
                }
            });

            holder.btnDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String currentQuantity = holder.txtQuantityProduct.getText().toString();
                    if (currentQuantity.equals("1")) {
                        Toast.makeText(v.getContext(), "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
                    } else {
                        int newQuantity = Integer.parseInt(currentQuantity) - 1;
                        holder.txtQuantityProduct.setText(String.valueOf(newQuantity));
                        productList.get(position).setQuantity(newQuantity);
                    }
                }
            });

            holder.btnRemoveCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.remove(position);
                    notifyDataSetChanged();
                }
            });
        } else {
            holder.btnRemoveCart.setVisibility(View.GONE);
            holder.btnIncreaseQuantity.setVisibility(View.GONE);
            holder.btnDecreaseQuantity.setVisibility(View.GONE);
            holder.txtQuantityProduct.setVisibility(View.GONE);
            holder.btnAddProduct.setVisibility(View.VISIBLE);
            holder.btnAddProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Đã thêm sản phẩm vào đơn hàng", Toast.LENGTH_SHORT).show();
                    ProductModel product = new ProductModel(
                            productList.get(position).getProductID(),
                            productList.get(position).getProductName(),
                            productList.get(position).getUnitPrice(),
                            productList.get(position).getImage_link(),
                            productList.get(position).getCategoryName(),
                            productList.get(position).getSupplierName(),
                            productList.get(position).getSale(),
                            1
                    );
                    saveProductToSharedPreferences(context, product);
                    productList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
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

    public List<ProductModel> getProductList() {
        return productList;
    }

    public void saveProductToSharedPreferences(Context context, ProductModel product) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("product_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String jsonProductList = sharedPreferences.getString("saved_product_list", null);
        Type type = new TypeToken<List<ProductModel>>() {}.getType();
        List<ProductModel> productList;

        if (jsonProductList != null) {
            productList = gson.fromJson(jsonProductList, type);
        } else {
            productList = new ArrayList<>();
        }

        productList.add(product);

        String updatedJsonProductList = gson.toJson(productList);
        editor.putString("saved_product_list", updatedJsonProductList);
        editor.apply();
    }

    public void removeProduct(int productId, CartAdapter.ViewHolder holder) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        int memberId = sharedPreferences.getInt("member_id", 0);

        CartApiService service = ApiServiceProvider.getCartApiService();
        Call<JsonObject> call = service.removeProduct(new CartItem(memberId, productId));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                holder.btnRemoveCart.setEnabled(true);
                if (response.isSuccessful()) {
                    cartActivity.loadProductCart();
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                holder.btnRemoveCart.setEnabled(true);
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