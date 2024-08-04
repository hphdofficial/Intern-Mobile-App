package com.android.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.models.ProductModel;
import com.android.mobile.models.SupplierModelOption;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.CatagoryApiService;
import com.android.mobile.services.ProductApiService;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_item_detail extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView imageItem = findViewById(R.id.imageItem);
        TextView txtItemName = findViewById(R.id.txtItemName);
        TextView txtItemPrice = findViewById(R.id.txtItemPrice);
        TextView txtItemSupplier = findViewById(R.id.txtItemSupplier);
        TextView txtItemInStock = findViewById(R.id.txtItemInStock);
        Button btnDanhGia = findViewById(R.id.btnDanhGia);

        btnDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                startActivity(intent);
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

// Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();

        Intent intent = getIntent();
        int idProduct = intent.getIntExtra("id", -1);
        int idSupplier = intent.getIntExtra("IDSupplier", -1);
        //Fetch Tên nhà cung cấp

        CatagoryApiService apiService2 = ApiServiceProvider.getCatagoryApiService();
        apiService2.getSupplier(idSupplier).enqueue(new Callback<SupplierModelOption>() {
            @Override
            public void onResponse(Call<SupplierModelOption> call, Response<SupplierModelOption> response) {
                if(response.isSuccessful()){
                    SupplierModelOption supplier = response.body();
                    txtItemSupplier.setText(supplier.getSupplierName());
                }else {
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SupplierModelOption> call, Throwable throwable) {

            }
        });

        //Fetch thông tin sản phẩm
        ProductApiService apiService = ApiServiceProvider.getProductApiService();
        apiService.show(idProduct).enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                if(response.isSuccessful()){
                    ProductModel product = response.body();
                    txtItemName.setText(product.getProductName());
                    txtItemPrice.setText(product.getUnitPrice() + " VND");
                    txtItemInStock.setText(product.getUnitsInStock() +"");
                    String image = product.getImage_link();
                    System.out.println("ABC" + image);
                    if (image != null) {
                        Picasso.get().load(image).placeholder(R.drawable.photo3x4).into(imageItem);
                    }
                }else {
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable throwable) {
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + throwable.getMessage());
            }
        });



    }
}