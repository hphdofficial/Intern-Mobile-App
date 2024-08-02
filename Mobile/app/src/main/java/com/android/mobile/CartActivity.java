package com.android.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.adapter.CartAdapter;
import com.android.mobile.adapter.ClubAdapter;
import com.android.mobile.models.CartItem;
import com.android.mobile.models.Club;
import com.android.mobile.models.Product;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.CartApiService;
import com.android.mobile.services.ClubApiService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<Product> productList = new ArrayList<>();
    private Button btnThanhToan;

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

        adapter = new CartAdapter(this, productList);
        recyclerView = findViewById(R.id.recycler_stored_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vdm92aW5hbW1vaS00YmVkYjZkZDFjMDUuaGVyb2t1YXBwLmNvbS9hcGkvYXV0aC9sb2dpbiIsImlhdCI6MTcyMjQ3NTUyNiwiZXhwIjoxNzIyNTYxOTI2LCJuYmYiOjE3MjI0NzU1MjYsImp0aSI6IktKRVQxRldZMFJLWnJVb2MiLCJzdWIiOiIyNTciLCJwcnYiOiIxMDY2NmI2ZDAzNThiMTA4YmY2MzIyYTg1OWJkZjk0MmFmYjg4ZjAyIiwibWVtYmVyX2lkIjoyNTcsInJvbGUiOjB9.DaR7nqpNZNDztXD31H48D2diLATO8qp4el5uWeiBpqE";

        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
//                String token = sharedPreferences.getString("access_token", null);

        CartApiService service = ApiServiceProvider.getCartApiService();
        Call<JsonObject> call = service.getCart("Bearer" + token, 257);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonResponse = response.body();
                    Gson gson = new Gson();
                    Type cartItemListType = new TypeToken<List<CartItem>>() {
                    }.getType();
                    Map<String, List<CartItem>> responseMap = gson.fromJson(jsonResponse, new TypeToken<Map<String, List<CartItem>>>() {
                    }.getType());
                    List<CartItem> cartItems = responseMap.get("cart");
                    List<Product> products = new ArrayList<>();
                    for (CartItem item : cartItems) {
                        products.add(item.getProduct());
                        Log.e("name", item.getProduct().getProductName());
                    }
                    adapter.setData(products);
                    Toast.makeText(CartActivity.this, "Success " + response.message(), Toast.LENGTH_SHORT).show();
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });

        btnThanhToan = findViewById(R.id.btn_thanhtoan);
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(CartActivity.this, CartActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("id_club", clubList.get(position).getId_club());
//                intent.putExtras(bundle);
//                startActivity(intent);
                Toast.makeText(CartActivity.this, "Pressed button thanhtoan", Toast.LENGTH_SHORT).show();
            }
        });
    }
}