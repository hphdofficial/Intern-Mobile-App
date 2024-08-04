package com.android.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.models.SupplierModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.SupplierApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplierInfoActivity extends BaseActivity {

    private int supplierID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // chèn fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();

        Intent intent = getIntent();
        supplierID = intent.getIntExtra("SupplierID", -1);

        fetchSupplierInfo(supplierID);
    }

    private void fetchSupplierInfo(int supplierID) {
        SupplierApiService apiService = ApiServiceProvider.getSupplierApiService();
        apiService.getSupplier(supplierID).enqueue(new Callback<SupplierModel>() {
            @Override
            public void onResponse(Call<SupplierModel> call, Response<SupplierModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SupplierModel supplier = response.body();
                    displaySupplierInfo(supplier);
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<SupplierModel> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void displaySupplierInfo(SupplierModel supplier) {
        TextView supplierNameTextView = findViewById(R.id.supplierName);
        TextView addressTextView = findViewById(R.id.supplierAddress);
        TextView phoneTextView = findViewById(R.id.supplierPhone);
        TextView emailTextView = findViewById(R.id.supplierEmail);
        Button btnViewAllProducts = findViewById(R.id.viewAllProductsButton);

        supplierNameTextView.setText(supplier.getSupplierName());
        addressTextView.setText(supplier.getAddress());
        phoneTextView.setText(supplier.getPhone());
        emailTextView.setText(supplier.getEmail());

        btnViewAllProducts.setOnClickListener(view -> {
            Intent productIntent = new Intent(SupplierInfoActivity.this, SupplierProductActivity.class);
            productIntent.putExtra("SupplierID", supplierID); // Pass the SupplierID to the next activity
            startActivity(productIntent);
        });
    }
}
