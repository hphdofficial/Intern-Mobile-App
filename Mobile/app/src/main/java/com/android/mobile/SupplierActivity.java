package com.android.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.adapter.SupplierAdapter;
import com.android.mobile.models.SupplierModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.SupplierApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplierActivity extends BaseActivity implements SupplierAdapter.OnSupplierClickListener {

    private RecyclerView recyclerView;
    private SupplierAdapter adapter;
    private List<SupplierModel> supplierList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);
        // chèn fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        supplierList = new ArrayList<>();
        adapter = new SupplierAdapter(supplierList, this);
        recyclerView.setAdapter(adapter);

        fetchSuppliers();
        NamePage();
    }

    private void fetchSuppliers() {
        SupplierApiService apiService = ApiServiceProvider.getSupplierApiService();
        Call<List<SupplierModel>> call = apiService.getSuppliers();
        call.enqueue(new Callback<List<SupplierModel>>() {
            @Override
            public void onResponse(Call<List<SupplierModel>> call, Response<List<SupplierModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    supplierList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SupplierActivity.this, "Không thể lấy danh sách nhà cung cấp", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SupplierModel>> call, Throwable t) {
                Toast.makeText(SupplierActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSupplierClick(SupplierModel supplier) {
        Intent intent = new Intent(SupplierActivity.this, SupplierInfoActivity.class);
        intent.putExtra("SupplierID", supplier.getSupplierID());
        intent.putExtra("SupplierName", supplier.getSupplierName());
        intent.putExtra("Address", supplier.getAddress());
        intent.putExtra("Phone", supplier.getPhone());
        intent.putExtra("Email", supplier.getEmail());
        startActivity(intent);
    }

    public void NamePage(){
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Nhà cung cấp");
        myContentE.apply();
    }
}
