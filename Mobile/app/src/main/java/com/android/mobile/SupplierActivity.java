package com.android.mobile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.adapter.SupplierAdapter;
import com.android.mobile.models.SupplierModel;

import java.util.ArrayList;
import java.util.List;

public class SupplierActivity extends AppCompatActivity {

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
        // Adding mock data
        supplierList.add(new SupplierModel(1, "Supplier 1", "Address 1", "1234567890", "email1@example.com"));
        supplierList.add(new SupplierModel(2, "Supplier 2", "Address 2", "0987654321", "email2@example.com"));
        supplierList.add(new SupplierModel(3, "Supplier 3", "Address 3", "1122334455", "email3@example.com"));

        adapter = new SupplierAdapter(supplierList);
        recyclerView.setAdapter(adapter);
    }
}
