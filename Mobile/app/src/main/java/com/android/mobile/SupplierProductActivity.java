package com.android.mobile;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.adapter.ItemSupplierProductAdapter;
import com.android.mobile.models.Item;
import java.util.ArrayList;

public class SupplierProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ItemSupplierProductAdapter adapter;
    private ArrayList<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_product);

        recyclerView = findViewById(R.id.recycler_supplier_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //chèn fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

// Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();

        // Tạo dữ liệu mẫu cho nhà cung cấp
        String supplierName = "Nhà cung cấp A"; // Bạn có thể thay đổi giá trị này hoặc nhận từ Intent
        itemList = getItemsBySupplier(supplierName);

        adapter = new ItemSupplierProductAdapter(this, itemList);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<Item> getItemsBySupplier(String supplierName) {
        ArrayList<Item> allItems = new ArrayList<>();
        // Thêm các item mẫu
        allItems.add(new Item("9/7/2024", "Sử dụng cho tập luyện", "Nhà cung cấp A", 100000, "Găng đấu tập", "Vải", "", "Găng"));
        allItems.add(new Item("9/7/2024", "Sử dụng cho thi đấu", "Nhà cung cấp A", 150000, "Giáp thi đấu", "Vải", "", "Giáp"));
        allItems.add(new Item("9/7/2024", "Sử dụng cho thi đấu", "Nhà cung cấp A", 150000, "Giáp thi đấu", "Vải", "", "Giáp"));
        allItems.add(new Item("9/7/2024", "Sử dụng cho thi đấu", "Nhà cung cấp A", 150000, "Giáp thi đấu", "Vải", "", "Giáp"));

        ArrayList<Item> filteredItems = new ArrayList<>();
        for (Item item : allItems) {
            if (item.getSupplier().equals(supplierName)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }
}
