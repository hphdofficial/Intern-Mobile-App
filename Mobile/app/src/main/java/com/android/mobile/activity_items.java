package com.android.mobile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;

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

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.adapter.Checkin_adapter;
import com.android.mobile.adapter.Item_adapter;
import com.android.mobile.adapter.OptionCheckBoxAdapter;
import com.android.mobile.adapter.OptionCheckBoxAdapter2;
import com.android.mobile.models.CatagoryModel;
import com.android.mobile.models.Item;
import com.android.mobile.models.OptionCategory;
import com.android.mobile.models.OptionSupplier;
import com.android.mobile.models.ProductModel;
import com.android.mobile.models.SupplierModelOption;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.CatagoryApiService;
import com.android.mobile.services.ProductApiService;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_items extends BaseActivity {
    private SearchView searchView;
    private EditText editMinPrice;
    private EditText editMaxPrice;
    private Button btnFilterActive;
    private OptionCheckBoxAdapter optionAdapter;
    private OptionCheckBoxAdapter2 optionAdapter2;
    private int min_price;
    private int max_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_items);

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

                btnFilterActive = filterDialogView.findViewById(R.id.btnFilter);

                BottomSheetDialog filterDialog = new BottomSheetDialog(activity_items.this);
                filterDialog.setContentView(filterDialogView);
                filterDialog.setCanceledOnTouchOutside(true);
                filterDialog.setDismissWithAnimation(true);

                editMinPrice = filterDialog.findViewById(R.id.editMinPrice);
                editMaxPrice = filterDialog.findViewById(R.id.editMaxPrice);


                //Fetch Category

                CatagoryApiService apiService = ApiServiceProvider.getCatagoryApiService();
                apiService.index().enqueue(new Callback<List<CatagoryModel>>() {
                    @Override
                    public void onResponse(Call<List<CatagoryModel>> call, Response<List<CatagoryModel>> response) {
                        if(response.isSuccessful()){
                            List<CatagoryModel> catagoryList = response.body();
                            List<OptionCategory> catagoryOptionList = new ArrayList<>();
                            for (CatagoryModel catagory : catagoryList){
                                OptionCategory optionCategory = new OptionCategory(catagory.getCategoryID(), catagory.getCategoryName(), false);
                                catagoryOptionList.add(optionCategory);
                            }
                            optionAdapter = new OptionCheckBoxAdapter(getApplicationContext(), catagoryOptionList);
                            RecyclerView recyclerView = filterDialogView.findViewById(R.id.recycle_option);
                            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                            recyclerView.setAdapter(optionAdapter);
                        }else {
                            System.out.println("Active: Call onResponse");
                            Log.e("PostData", "Error: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CatagoryModel>> call, Throwable throwable) {
                        System.out.println("Active: Call Onfail");
                        Log.e("PostData", "Failure: " + throwable.getMessage());
                    }
                });

                //Fetch Supplier
                CatagoryApiService apiService2 = ApiServiceProvider.getCatagoryApiService();
                apiService2.getAllSupplier().enqueue(new Callback<List<SupplierModelOption>>() {
                    @Override
                    public void onResponse(Call<List<SupplierModelOption>> call, Response<List<SupplierModelOption>> response) {
                        if(response.isSuccessful()){
                            List<SupplierModelOption> supplierList = response.body();
                            List<OptionSupplier> supplierOptionList = new ArrayList<>();
                            for (SupplierModelOption supplier : supplierList){
                                OptionSupplier optionSupplier = new OptionSupplier(false, supplier.getSupplierID(), supplier.getSupplierName());
                                supplierOptionList.add(optionSupplier);
                            }
                            optionAdapter2 = new OptionCheckBoxAdapter2(getApplicationContext(), supplierOptionList);
                            RecyclerView recyclerView = filterDialogView.findViewById(R.id.recycle_option_supplier);
                            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                            recyclerView.setAdapter(optionAdapter2);
                        }else {
                            System.out.println("Active: Call onResponse");
                            Log.e("PostData", "Error: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<SupplierModelOption>> call, Throwable throwable) {
                        System.out.println("Active: Call Onfail");
                        Log.e("PostData", "Failure: " + throwable.getMessage());
                    }
                });

                btnFilterActive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        performSearch();
                    }
                });

                filterDialog.show();

            }
        });

        searchView = findViewById(R.id.search_item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                SearchProductsByName(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
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

        FetchProducts();
    }

    private void performSearch() {

        String min_price_str = editMinPrice.getText().toString().trim();
        String max_price_str = editMaxPrice.getText().toString().trim();
        List<OptionCategory> selectedOptions = optionAdapter.getOptionList().stream()
                .filter(OptionCategory::isChecked)
                .collect(Collectors.toList());

        List<OptionSupplier> selectedOptions2 = optionAdapter2.getOptionList().stream()
                .filter(OptionSupplier::isChecked)
                .collect(Collectors.toList());
        System.out.println(selectedOptions);
        if(selectedOptions != null){
            String CategoryID = "";
            for (OptionCategory option : selectedOptions) {
                CategoryID = option.getCategoryID();
            }
            ProductApiService apiService = ApiServiceProvider.getProductApiService();
            apiService.getByCategory(CategoryID).enqueue(new Callback<ProductModel[]>() {
                @Override
                public void onResponse(Call<ProductModel[]> call, Response<ProductModel[]> response) {
                    if(response.isSuccessful()){
                        ProductModel[] productList = response.body();
                        for (ProductModel product : productList){
                            Log.e("PostData", "Success: " + product.getProductName());
                        }
                        Item_adapter itemAdapter = new Item_adapter(getApplicationContext(), productList);
                        RecyclerView recyclerView = findViewById(R.id.recycler_item);
                        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                        recyclerView.setAdapter(itemAdapter);
                    }else {
                        System.out.println("Active: Call onResponse");
                        Log.e("PostData", "Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ProductModel[]> call, Throwable throwable) {
                    System.out.println("Active: Call Onfail");
                    Log.e("PostData", "Failure: " + throwable.getMessage());
                }
            });
        }
        if(selectedOptions2 != null && !min_price_str.isEmpty() && !max_price_str.isEmpty()){

            min_price = Integer.parseInt(min_price_str);
            max_price = Integer.parseInt(max_price_str);
            int SupplierID = 0;
            for (OptionSupplier option : selectedOptions2) {
                SupplierID = option.getSupplierID();
            }
            ProductApiService apiService = ApiServiceProvider.getProductApiService();
            apiService.getFilter(SupplierID, min_price, max_price).enqueue(new Callback<ProductModel[]>() {
                @Override
                public void onResponse(Call<ProductModel[]> call, Response<ProductModel[]> response) {
                    if(response.isSuccessful()){
                        ProductModel[] productList = response.body();
                        for (ProductModel product : productList){
                            Log.e("PostData", "Success: " + product.getProductName());
                        }
                        Item_adapter itemAdapter = new Item_adapter(getApplicationContext(), productList);
                        RecyclerView recyclerView = findViewById(R.id.recycler_item);
                        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                        recyclerView.setAdapter(itemAdapter);
                    }else {
                        System.out.println("Active: Call onResponse");
                        Log.e("PostData", "Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ProductModel[]> call, Throwable throwable) {
                    System.out.println("Active: Call Onfail");
                    Log.e("PostData", "Failure: " + throwable.getMessage());
                }
            });
        }
    }

    private void FetchProducts(){
        ProductApiService apiService = ApiServiceProvider.getProductApiService();
        apiService.getProducts().enqueue(new Callback<ProductModel[]>() {
            @Override
            public void onResponse(Call<ProductModel[]> call, Response<ProductModel[]> response) {
                if(response.isSuccessful()){
                    ProductModel[] productList = response.body();
                    for (ProductModel product : productList){
                        Log.e("PostData", "Success: " + product.getProductName());
                    }
                    Item_adapter itemAdapter = new Item_adapter(getApplicationContext(), productList);
                    RecyclerView recyclerView = findViewById(R.id.recycler_item);
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    recyclerView.setAdapter(itemAdapter);
                }else {
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ProductModel[]> call, Throwable throwable) {
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + throwable.getMessage());
            }
        });
    }

    private void SearchProductsByName(String name){
        ProductApiService apiService = ApiServiceProvider.getProductApiService();
        apiService.search(name).enqueue(new Callback<ProductModel[]>() {
            @Override
            public void onResponse(Call<ProductModel[]> call, Response<ProductModel[]> response) {
                if(response.isSuccessful()){
                    ProductModel[] productList = response.body();
                    for (ProductModel product : productList){
                        Log.e("PostData", "Success: " + product.getProductName());
                    }
                    Item_adapter itemAdapter = new Item_adapter(getApplicationContext(), productList);
                    RecyclerView recyclerView = findViewById(R.id.recycler_item);
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    recyclerView.setAdapter(itemAdapter);
                }else {
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ProductModel[]> call, Throwable throwable) {
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + throwable.getMessage());
            }
        });
    }
}