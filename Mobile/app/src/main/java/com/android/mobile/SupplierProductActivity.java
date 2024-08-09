package com.android.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.adapter.ItemSupplierProductAdapter;
import com.android.mobile.models.ProductModel;
import com.android.mobile.models.SupplierModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.SupplierApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplierProductActivity extends BaseActivity {
    private SharedPreferences sharedPreferences;
    private static final String NAME_SHARED = "myContent";
    private static final String KEY_TITLE = "title";
    private static final String VALUE_INFO = "SupplierProduct";
    private BlankFragment loadingFragment;

    private List<ProductModel> products = new ArrayList<>();
    private ItemSupplierProductAdapter itemAdapter;
    private TextView txtSupplierName, txtSupplierAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supplier_product);

        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Sản phẩm nhà cung cấp ");
        myContentE.apply();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(NAME_SHARED, MODE_PRIVATE);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        txtSupplierName = findViewById(R.id.txtSupplierName);
        txtSupplierAddress = findViewById(R.id.txtSupplierAddress);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();

        itemAdapter = new ItemSupplierProductAdapter(this, products);
        RecyclerView recyclerView = findViewById(R.id.item_supplier_product);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(itemAdapter);

        int supplierID = getIntent().getIntExtra("SupplierID", -1);
        fetchSupplierInfo(supplierID);
        fetchProductsBySupplier(supplierID);
    }

    private void fetchSupplierInfo(int supplierID) {
        if (supplierID == -1) {
            Toast.makeText(this, "Invalid Supplier ID", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();
        SupplierApiService apiService = ApiServiceProvider.getSupplierApiService();
        apiService.getSupplier(supplierID).enqueue(new Callback<SupplierModel>() {
            @Override
            public void onResponse(Call<SupplierModel> call, Response<SupplierModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SupplierModel supplier = response.body();
                    txtSupplierName.setText(supplier.getSupplierName());
                    txtSupplierAddress.setText(supplier.getAddress());
                    hideLoading();
                } else {
                    hideLoading();
                    // Handle error
                    Toast.makeText(SupplierProductActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SupplierModel> call, Throwable t) {
                hideLoading();
                // Handle failure
                Toast.makeText(SupplierProductActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchProductsBySupplier(int supplierID) {
        if (supplierID == -1) {
            Toast.makeText(this, "Invalid Supplier ID", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();
        SupplierApiService apiService = ApiServiceProvider.getSupplierApiService();
        apiService.getProductsBySupplier(supplierID).enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    products.clear();  // Clear existing data
                    products.addAll(response.body());
                    itemAdapter.notifyDataSetChanged();
                    hideLoading();
                } else {
                    hideLoading();
                    // Handle the error
                    Toast.makeText(SupplierProductActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                hideLoading();
                // Handle the failure
                Toast.makeText(SupplierProductActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading() {
        if (loadingFragment == null) {
            loadingFragment = new BlankFragment();
            loadingFragment.show(getSupportFragmentManager(), "loading");
        }
    }

    private void hideLoading() {
        if (loadingFragment != null) {
            loadingFragment.dismiss();
            loadingFragment = null;
        }
    }

    private void saveToSharedPreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void onCardClick(View view) {
        Intent intent = new Intent(SupplierProductActivity.this, SupplierInfoActivity.class);
        int supplierID = getIntent().getIntExtra("SupplierID", -1); // Ensure supplierID is retrieved
        intent.putExtra("SupplierID", supplierID);
        startActivity(intent);
    }
}
