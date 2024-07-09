package com.android.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class activity_item_detail extends AppCompatActivity {

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

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

// Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        int price = intent.getIntExtra("price", -1);
        String supplier = intent.getStringExtra("supplier");
        String dateProduct = intent.getStringExtra("dateProduct");
        String material = intent.getStringExtra("material");
        String use = intent.getStringExtra("use");
        String type = intent.getStringExtra("type");

        TextView txtItemName = findViewById(R.id.txtItemName);
        TextView txtItemPrice = findViewById(R.id.txtItemPrice);
        TextView txtItemSupplier = findViewById(R.id.txtItemSupplier);
        TextView txtItemDateProduct = findViewById(R.id.txtItemDateProduct);
        TextView txtItemMaterial = findViewById(R.id.txtItemMaterial);
        TextView txtItemUse = findViewById(R.id.txtItemUse);
        TextView txtItemType = findViewById(R.id.txtItemType);

        txtItemName.setText(name);
        txtItemPrice.setText(price + " VND");
        txtItemSupplier.setText(supplier);
        txtItemDateProduct.setText(dateProduct);
        txtItemMaterial.setText(material);
        txtItemUse.setText(use);
        txtItemType.setText(type);
    }
}