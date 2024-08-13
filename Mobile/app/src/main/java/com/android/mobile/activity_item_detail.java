package com.android.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.adapter.Item_adapter;
import com.android.mobile.models.ProductModel;
import com.android.mobile.models.SupplierModelOption;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.CatagoryApiService;
import com.android.mobile.services.ProductApiService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_item_detail extends BaseActivity {
    private BlankFragment loadingFragment;
    TextView editQuantity;
    int quantityInStock;
    private List<ProductModel> productList = new ArrayList<>();
    private Item_adapter itemAdapter;
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

        Intent intent = getIntent();
        int idProduct = intent.getIntExtra("id", -1);
        int idSupplier = intent.getIntExtra("IDSupplier", -1);
        String categoryName = intent.getStringExtra("categoryName");

        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Chi tiết dụng cụ");
        myContentE.putString("categoryName", categoryName);
        myContentE.apply();

        ImageView imageItem = findViewById(R.id.imageItem);
        TextView txtItemName = findViewById(R.id.txtItemName);
        TextView txtItemPrice = findViewById(R.id.txtItemPrice);
        TextView txtItemSupplier = findViewById(R.id.txtItemSupplier);
        TextView txtItemInStock = findViewById(R.id.txtItemInStock);
        TextView txtItemPriceSale = findViewById(R.id.txtItemPriceSale);
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



        btnDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                intent.putExtra("productId", idProduct);
                startActivity(intent);
            }
        });




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

                                FragmentAfterAddCart choiceFragment = new FragmentAfterAddCart();

                                // Bắt đầu một FragmentTransaction để thêm Fragment vào layout
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                                // Thêm Fragment vào layout (ví dụ layout với id là fragment_container)
                                transaction.add(R.id.fragment_container_choice, choiceFragment);

                                // Thêm Fragment vào backstack nếu muốn
                                transaction.addToBackStack(null);

                                // Hoàn tất việc thêm Fragment
                                transaction.commit();
                                Toast.makeText(activity_item_detail.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();

                            }else {
                                hideLoading();
                                System.out.println("On Response Fail");
                                Toast.makeText(activity_item_detail.this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable throwable) {
                            hideLoading();
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
                    hideLoading();
                }else {
                    hideLoading();
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SupplierModelOption> call, Throwable throwable) {
                System.out.println("Active: Call Lỗi Category");
                hideLoading();
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
                    if (image.isEmpty() || image.equals(" ")) {
                        imageItem.setImageResource(R.drawable.logo_vovinam);
                    }else{
                        Picasso.get().load(image).placeholder(R.drawable.logo_vovinam).into(imageItem);

                    }

                    String sale = product.getSale();
                    if(sale.equals("0")){
                        txtItemPriceSale.setVisibility(View.INVISIBLE);
                    }else {
                        txtItemPriceSale.setVisibility(View.VISIBLE);

                        txtItemPrice.setPaintFlags(txtItemPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        float percent = Float.parseFloat(sale.toString());
                        System.out.println("ABC"+percent);
                        int intPercent = (int) (percent*100);
                        System.out.println("ABC"+intPercent);
                        int txtProductPriceSale = Integer.parseInt(product.getUnitPrice()) - (Integer.parseInt(product.getUnitPrice())*intPercent)/100;
                        txtItemPriceSale.setText("-"+intPercent+"% "+txtProductPriceSale+" VND");
                    }

                    SharedPreferences sharedPreferences = getSharedPreferences("myContent", Context.MODE_PRIVATE);
                    String categoryNameSave = sharedPreferences.getString("categoryName", "Găng tay");
                    FetchProductsByCategory(categoryNameSave);
                    hideLoading();
                }else {
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());
                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable throwable) {
                hideLoading();
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

    private void FetchProductsByCategory(String categoryName){

        ProductApiService apiService = ApiServiceProvider.getProductApiService();
        apiService.getByCategory(categoryName).enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if(response.isSuccessful()){
                    productList.clear(); // Clear existing data
                    productList.addAll(response.body());
                    itemAdapter = new Item_adapter(getApplicationContext(), productList);
                    RecyclerView recyclerView = findViewById(R.id.recycler_item);
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    recyclerView.setAdapter(itemAdapter);

                    hideLoading();
                }else {
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());
                    Toast.makeText(activity_item_detail.this, "Lấy dữ liệu thất bại, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable throwable) {
                hideLoading();
                Toast.makeText(activity_item_detail.this, "Lỗi kết nối mạng, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + throwable.getMessage());
            }
        });
    }
}