package com.android.mobile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

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
import com.android.mobile.models.Item;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class SupplierProductActivity extends BaseActivity {
    private SharedPreferences sharedPreferences;
    private static final String NAME_SHARED = "myContent";
    private static final String KEY_TITLE = "title";
    private static final String VALUE_INFO = "SupplierProduct";

    Item item = new Item("9/7/2024", "Torem", "Nhà cung cấp A", 100000, "Găng đấu tập", "Vải", "", "Găng");
    Item item1 = new Item("9/7/2024", "Torem1", "Nhà cung cấp A", 110000, "Găng đấu tập", "Vải", "", "Đai");
    Item item2 = new Item("9/7/2024", "Torem2", "Nhà cung cấp A", 1220000, "Găng đấu tập", "Vải", "", "Quần áo");
    Item item3 = new Item("9/7/2024", "Torem3", "Nhà cung cấp A", 166000, "Găng đấu tập", "Vải", "", "Quả đấm");
    Item item4 = new Item("9/7/2024", "Torem4", "Nhà cung cấp A", 888000, "Găng đấu tập", "Vải", "", "Găng");
    Item item5 = new Item("9/7/2024", "Torem5", "Nhà cung cấp A", 190000, "Găng đấu tập", "Vải", "", "Giáp");
    ArrayList<Item> items = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supplier_product);
        items.add(item);
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        items.add(item5);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(NAME_SHARED, MODE_PRIVATE);
        saveToSharedPreferences(KEY_TITLE, VALUE_INFO);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton btnFilter = findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View filterDialogView = LayoutInflater.from(SupplierProductActivity.this).inflate(R.layout.bottom_sheet_dialog_filter, null);
                BottomSheetDialog filterDialog = new BottomSheetDialog(SupplierProductActivity.this);
                filterDialog.setContentView(filterDialogView);
                filterDialog.setCanceledOnTouchOutside(true);
                filterDialog.setDismissWithAnimation(true);
                filterDialog.show();
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

        ItemSupplierProductAdapter itemAdapter = new ItemSupplierProductAdapter(this, items);
        RecyclerView recyclerView = findViewById(R.id.item_supplier_product);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(itemAdapter);
    }

    private void saveToSharedPreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
