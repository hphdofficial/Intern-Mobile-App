package com.android.mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

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
import com.android.mobile.adapter.TheoryAdapter;
import com.android.mobile.models.CatagoryModel;
import com.android.mobile.models.Item;
import com.android.mobile.models.OptionCategory;
import com.android.mobile.models.OptionSupplier;
import com.android.mobile.models.ProductModel;
import com.android.mobile.models.SupplierModelOption;
import com.android.mobile.models.TheoryModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.CatagoryApiService;
import com.android.mobile.services.ProductApiService;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_items extends BaseActivity {
    private EditText searchView;
    private EditText editMinPrice;
    private EditText editMaxPrice;
    private Button btnFilterActive;
    private OptionCheckBoxAdapter optionAdapter;
    private OptionCheckBoxAdapter2 optionAdapter2;
    private List<ProductModel> productList = new ArrayList<>();
    private List<ProductModel> filteredProductList = new ArrayList<>();
    private Item_adapter itemAdapter;
    private Item_adapter itemAdapterFilter;
    private int min_price;
    private int max_price;
    private BlankFragment loadingFragment;

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

        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Danh sách dụng cụ");
        myContentE.apply();

        ImageButton btnFilter = findViewById(R.id.btnFilter);

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading();
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
                            hideLoading();
                            System.out.println("Active: Call onResponse");
                            Log.e("PostData", "Error: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CatagoryModel>> call, Throwable throwable) {
                        hideLoading();
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

                            hideLoading();
                        }else {
                            System.out.println("Active: Call onResponse");
                            Log.e("PostData", "Error: " + response.message());
                            hideLoading();
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

        searchView = findViewById(R.id.search_edit_text);
        ImageButton btnSearch = findViewById(R.id.search_button);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FetchProducts();
            }
        });

        searchView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                SearchProductsByName(searchView.getText().toString());
                return true;
            }
            return false;
        });

        itemAdapter = new Item_adapter(getApplicationContext(), filteredProductList);
        RecyclerView recyclerView = findViewById(R.id.recycler_item);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(itemAdapter);

        FetchProducts();

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterProduct(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

// Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.commit();


    }

    private void performSearch() {
        showLoading();

        String min_price_str = editMinPrice.getText().toString().trim();
        String max_price_str = editMaxPrice.getText().toString().trim();
        List<ProductModel> productList = new ArrayList<>();
        List<OptionCategory> selectedOptions = optionAdapter.getOptionList().stream()
                .filter(OptionCategory::isChecked)
                .collect(Collectors.toList());

        List<OptionSupplier> selectedOptions2 = optionAdapter2.getOptionList().stream()
                .filter(OptionSupplier::isChecked)
                .collect(Collectors.toList());
        System.out.println(selectedOptions);


        String CategoryName = "";
        for (OptionCategory option : selectedOptions) {
            CategoryName = option.getCategoryName();
        }
        ProductApiService apiService = ApiServiceProvider.getProductApiService();
        apiService.getByCategory(CategoryName).enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if(response.isSuccessful()){
                    List<ProductModel> productListByCategory = response.body();
                    for (ProductModel product : productListByCategory){
                        Log.e("PostData", "Success: " + product.getProductName());
                        productList.add(product);
                    }
                    itemAdapterFilter = new Item_adapter(getApplicationContext(), productList);
                    RecyclerView recyclerView = findViewById(R.id.recycler_item);
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    recyclerView.setAdapter(itemAdapterFilter);
                }else {
                    hideLoading();
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable throwable) {
                hideLoading();
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + throwable.getMessage());
            }
        });






        if(!min_price_str.isEmpty() && !max_price_str.isEmpty()){
            min_price = Integer.parseInt(min_price_str);
            max_price = Integer.parseInt(max_price_str);
            apiService.getFilterByPrice(min_price, max_price).enqueue(new Callback<List<ProductModel>>() {
                @Override
                public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                    if(response.isSuccessful()){
                        List<ProductModel> productListByPrice = response.body();
                        for (ProductModel product : productListByPrice){
                            Log.e("PostData", "Success: " + product.getProductName());
                            productList.add(product);

                        }
                        itemAdapterFilter = new Item_adapter(getApplicationContext(), productList);
                        RecyclerView recyclerView = findViewById(R.id.recycler_item);
                        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                        recyclerView.setAdapter(itemAdapterFilter);

                    }else {
                        hideLoading();
                        System.out.println("Active: Call onResponse");
                        Log.e("PostData", "Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<ProductModel>> call, Throwable throwable) {
                    hideLoading();
                    System.out.println("Active: Call Onfail");
                    Log.e("PostData", "Failure: " + throwable.getMessage());
                }
            });
        }

        int SupplierID = 0;
        for (OptionSupplier option : selectedOptions2) {
            SupplierID = option.getSupplierID();
        }

        apiService.getFilterBySupplier(SupplierID).enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if(response.isSuccessful()){
                    List<ProductModel> productListBySupplier = response.body();
                    for (ProductModel product : productListBySupplier){
                        Log.e("PostData", "Success: " + product.getProductName());
                        productList.add(product);

                    }
                    itemAdapterFilter = new Item_adapter(getApplicationContext(), productList);
                    RecyclerView recyclerView = findViewById(R.id.recycler_item);
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    recyclerView.setAdapter(itemAdapterFilter);
                    hideLoading();
                }else {
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());
                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable throwable) {
                hideLoading();
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + throwable.getMessage());
            }
        });

    }

    private void FetchProducts(){
        showLoading();
        ProductApiService apiService = ApiServiceProvider.getProductApiService();
        apiService.getProducts().enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if(response.isSuccessful()){
                    productList.clear(); // Clear existing data
                    productList.addAll(response.body());
                    filterProduct(searchView.getText().toString());

                    hideLoading();
                }else {
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());
                    Toast.makeText(activity_items.this, "Lấy dữ liệu thất bại, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable throwable) {
                hideLoading();
                Toast.makeText(activity_items.this, "Lỗi kết nối mạng, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + throwable.getMessage());
            }
        });
    }

    private void SearchProductsByName(String name){
        if(name.isEmpty()){
            FetchProducts();
        }else{
            ProductApiService apiService = ApiServiceProvider.getProductApiService();
            apiService.search(name).enqueue(new Callback<List<ProductModel>>() {
                @Override
                public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                    if(response.isSuccessful()){
                        List<ProductModel> productList = response.body();
                        productList.clear(); // Clear existing data
                        productList.addAll(response.body());
                        filterProduct(name);

                    }else {
                        System.out.println("Active: Call onResponse");
                        Log.e("PostData", "Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<ProductModel>> call, Throwable throwable) {
                    System.out.println("Active: Call Onfail");
                    Log.e("PostData", "Failure: " + throwable.getMessage());
                }
            });
        }

    }

    private void filterProduct(String query) {
        filteredProductList.clear();
        String normalizedQuery = removeDiacritics(query.toLowerCase());
        if (normalizedQuery.isEmpty()) {
            filteredProductList.addAll(productList);
        } else {
            for (ProductModel product : productList) {
                if (removeDiacritics(product.getProductName().toLowerCase()).contains(normalizedQuery) ||
                        removeDiacritics(product.getUnitPrice().toLowerCase()).contains(normalizedQuery)) {
                    filteredProductList.add(product);
                }
            }
        }
        itemAdapter.notifyDataSetChanged();
    }

    private String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
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
}