package com.android.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.adapter.CartAdapter;
import com.android.mobile.models.CartItem;
import com.android.mobile.models.ProductModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.CartApiService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<ProductModel> productList = new ArrayList<>();
    private Button btnPayCart;
    private TextView txtSumQuantity;
    private TextView txtSumPrice;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BlankFragment loadingFragment;
    private ImageView imgNotify;
    private TextView txtNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Giỏ hàng");
        myContentE.apply();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new titleFragment());
        fragmentTransaction.commit();

        txtSumQuantity = findViewById(R.id.txt_sum_quantity);
        txtSumPrice = findViewById(R.id.txt_sum_price);
        imgNotify = findViewById(R.id.img_notify);
        txtNotify = findViewById(R.id.txt_notify);

        adapter = new CartAdapter(this, productList, this);
        recyclerView = findViewById(R.id.recycler_stored_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new SlideInLeftAnimator());

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadProductCart();
        });

        loadProductCart();

        btnPayCart = findViewById(R.id.btn_thanhtoan);
        btnPayCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, Purchase.class);
                intent.putParcelableArrayListExtra("product_list", new ArrayList<>(productList));
                startActivity(intent);
            }
        });
    }

    public void loadProductCart() {
        showLoading();

        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", null);
        int memberId = sharedPreferences.getInt("member_id", 0);

        updateTotalPrice(memberId);

        CartApiService service = ApiServiceProvider.getCartApiService();
        Call<JsonObject> call = service.getCart("Bearer" + token, memberId);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    JsonObject jsonResponse = response.body();
                    Gson gson = new Gson();
                    Type cartItemListType = new TypeToken<List<CartItem>>() {
                    }.getType();
                    Map<String, List<CartItem>> responseMap = gson.fromJson(jsonResponse, new TypeToken<Map<String, List<CartItem>>>() {
                    }.getType());
                    List<CartItem> cartItems = responseMap.get("cart");
                    List<ProductModel> products = new ArrayList<>();

                    for (CartItem item : cartItems) {
                        boolean found = false;
                        for (ProductModel pro : products) {
                            if (item.getProduct_id() == pro.getProductID()) {
                                pro.setQuantity(pro.getQuantity() + item.getQuantity());
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            ProductModel product = item.getProduct();
                            products.add(new ProductModel(product.getProductID(), product.getProductName(), product.getUnitPrice(), product.getImage_link(), product.getCategoryName(), product.getSupplierName(), product.getSale(), item.getQuantity()));
                        }
                    }

                    txtSumQuantity.setText("Số lượng: " + products.size() + " sản phẩm");
//                    updateTotalPrice(memberId);

                    productList = new ArrayList<>(products);
                    adapter.setData(products);

                    if (products.isEmpty()) {
                        imgNotify.setVisibility(View.VISIBLE);
                        txtNotify.setVisibility(View.VISIBLE);
                    }
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hideLoading();

                swipeRefreshLayout.setRefreshing(false);
                t.printStackTrace();
            }
        });
    }

    public void updateTotalPrice(int member_id) {
        CartApiService service = ApiServiceProvider.getCartApiService();
        Call<JsonObject> call = service.getTotalPrice(member_id);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hideLoading();

                if (response.isSuccessful()) {
                    JsonObject jsonResponse = response.body();
                    Gson gson = new Gson();
                    int totalPrice = jsonResponse.get("total_price").getAsInt();
                    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                    String formattedSumPrice = currencyFormat.format(totalPrice);
                    txtSumPrice.setText("Tổng tiền: " + formattedSumPrice);
                    Toast.makeText(CartActivity.this, "Cập nhật giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hideLoading();

                t.printStackTrace();
            }
        });
    }

    private void showLoading() {
        loadingFragment = new BlankFragment();
        loadingFragment.show(getSupportFragmentManager(), "loading");
    }

    private void hideLoading() {
        if (loadingFragment != null) {
            loadingFragment.dismiss();
            loadingFragment = null;
        }
    }
}