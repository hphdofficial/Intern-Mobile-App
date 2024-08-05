package com.android.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private BlankFragment loadingFragment;
    EditText editQuantity;
    int quantityInStock;
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

        showLoading();

        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Chi tiết dụng cụ");
        myContentE.apply();

        ImageView imageItem = findViewById(R.id.imageItem);
        TextView txtItemName = findViewById(R.id.txtItemName);
        TextView txtItemPrice = findViewById(R.id.txtItemPrice);
        TextView txtItemSupplier = findViewById(R.id.txtItemSupplier);
        TextView txtItemInStock = findViewById(R.id.txtItemInStock);
        editQuantity = findViewById(R.id.editQuantity);
        Button btnDanhGia = findViewById(R.id.btnDanhGia);
        Button btnMua = findViewById(R.id.btnBuy);
        ImageButton btnDecre = findViewById(R.id.btnDecre);
        ImageButton btnIncre = findViewById(R.id.btnIncre);

        btnIncre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementNumber();
            }
        });

        btnDecre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrementNumber();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", null);
        int member_id = sharedPreferences.getInt("member_id", -1);


        Intent intent = getIntent();
        int idProduct = intent.getIntExtra("id", -1);
        int idSupplier = intent.getIntExtra("IDSupplier", -1);



        btnMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading();
                String currentTextQuantity = editQuantity.getText().toString();
                int currentNumberQuantity = Integer.parseInt(currentTextQuantity);
                if(quantityInStock > currentNumberQuantity){
                    ProductApiService apiService = ApiServiceProvider.getProductApiService();
                    apiService.addToCart(token,member_id, idProduct, currentNumberQuantity).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()){
                                hideLoading();
                                Toast.makeText(activity_item_detail.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(activity_item_detail.this, CartActivity.class);
                                startActivity(intent1);
                            }else {

                                System.out.println("On Response Fail");
                                Toast.makeText(activity_item_detail.this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable throwable) {
                            System.out.println("On Failure Fail");
                            Toast.makeText(activity_item_detail.this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    hideLoading();
                    Toast.makeText(activity_item_detail.this, "Quá số hàng trong kho, không thể thêm", Toast.LENGTH_SHORT).show();
                }


            }
        });


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


        //Fetch Tên nhà cung cấp

        CatagoryApiService apiService2 = ApiServiceProvider.getCatagoryApiService();
        apiService2.getSupplier(idSupplier).enqueue(new Callback<SupplierModelOption>() {
            @Override
            public void onResponse(Call<SupplierModelOption> call, Response<SupplierModelOption> response) {
                if(response.isSuccessful()){
                    SupplierModelOption supplier = response.body();
                    txtItemSupplier.setText(supplier.getSupplierName());

                    // Thêm sự kiện onClickListener cho txtItemSupplier
                    txtItemSupplier.setOnClickListener(view -> {
                        Intent supplierIntent = new Intent(activity_item_detail.this, SupplierInfoActivity.class);
                        supplierIntent.putExtra("SupplierID", idSupplier);
                        startActivity(supplierIntent);
                    });
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
                    quantityInStock = product.getUnitsInStock();
                    String image = product.getImage_link();
                    System.out.println("ABC" + image);
                    if (image != null) {
                        Picasso.get().load(image).placeholder(R.drawable.logo_vovinam).into(imageItem);
                    }else{
                        imageItem.setImageResource(R.drawable.logo_vovinam);
                    }

                    hideLoading();
                }else {
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());
                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable throwable) {
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + throwable.getMessage());
            }
        });



    }
    private void incrementNumber() {
        String currentText = editQuantity.getText().toString();
        int currentNumber = Integer.parseInt(currentText);
        currentNumber++;
        editQuantity.setText(String.valueOf(currentNumber));
    }

    private void decrementNumber() {
        String currentText = editQuantity.getText().toString();
        int currentNumber = Integer.parseInt(currentText);
        if (currentNumber > 1) {
            currentNumber--;
            editQuantity.setText(String.valueOf(currentNumber));
        } else {
            Toast.makeText(this, "Số lượng không bé hơn 1", Toast.LENGTH_SHORT).show();
        }
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