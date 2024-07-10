package com.android.mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.adapter.Checkin_adapter;
import com.android.mobile.adapter.Item_adapter;
import com.android.mobile.models.Item;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class activity_items extends AppCompatActivity {
    Item item = new Item("9/7/2024", "Torem", "Vovinam Club", 100000, "Găng đấu tập", "Vải", "", "Găng");
    Item item1 = new Item("9/7/2024", "Torem1", "Vovinam Club1", 110000, "Găng đấu tập", "Vải", "", "Đai");
    Item item2 = new Item("9/7/2024", "Torem2", "Vovinam Club2", 1220000, "Găng đấu tập", "Vải", "", "Quần áo");
    Item item3 = new Item("9/7/2024", "Torem3", "Vovinam Club3", 166000, "Găng đấu tập", "Vải", "", "Quả đấm");
    Item item4 = new Item("9/7/2024", "Torem4", "Vovinam Club4", 888000, "Găng đấu tập", "Vải", "", "Găng");
    Item item5 = new Item("9/7/2024", "Torem5", "Vovinam Club5", 190000, "Găng đấu tập", "Vải", "", "Giáp");
    ArrayList<Item> items = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_items);
        items.add(item);
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        items.add(item5);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton btnFilter = findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View filterDialogView = LayoutInflater.from(activity_items.this).inflate(R.layout.bottom_sheet_dialog_filter, null);
                BottomSheetDialog filterDialog = new BottomSheetDialog(activity_items.this);
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

        Item_adapter itemAdapter = new Item_adapter(this, items);
        RecyclerView recyclerView = findViewById(R.id.recycler_item);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(itemAdapter);
    }
}