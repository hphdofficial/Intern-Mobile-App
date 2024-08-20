package fascon.vovinam.vn.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fascon.vovinam.vn.Model.OrderListModel;
import fascon.vovinam.vn.Model.ProductModel;
import fascon.vovinam.vn.Model.UpdateOrderRequest;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.OrderApiService;
import fascon.vovinam.vn.R;
import fascon.vovinam.vn.ViewModel.CartAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditOrderFragment extends DialogFragment {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<ProductModel> productList = new ArrayList<>();
    private Button btnUpdate;
    private Button btnAdd;
    private Button btnClose;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    private String languageS;

    private static final String ARG_ORDER = "order";
    private OrderListModel order;

    public static EditOrderFragment newInstance(OrderListModel order) {
        EditOrderFragment fragment = new EditOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ORDER, new Gson().toJson(order));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String orderJson = getArguments().getString(ARG_ORDER);
            order = new Gson().fromJson(orderJson, OrderListModel.class);
        }
        if (order != null && order.getDetail_carts() != null) {
            for (OrderListModel.DetailCart cart : order.getDetail_carts()) {
                productList.add(new ProductModel(
                        cart.getProduct().getProductID(),
                        cart.getProduct().getProductName(),
                        String.valueOf(cart.getProduct().getUnitPrice()),
                        cart.getProduct().getLink_image(),
                        cart.getProduct().getCategoryName(),
                        cart.getProduct().getSupplierName(),
                        cart.getProduct().getSale(),
                        cart.getQuantity()
                ));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_order, container, false);

        setCancelable(false);

        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new CartAdapter(getContext(), productList, true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        SharedPreferences shared = getActivity().getSharedPreferences("login_prefs", getContext().MODE_PRIVATE);
        languageS = shared.getString("language", null);

        btnClose = view.findViewById(R.id.button_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnUpdate = view.findViewById(R.id.button_update);
        btnUpdate.setOnClickListener(v -> fetchProductList());

        btnAdd = view.findViewById(R.id.button_add);
        btnAdd.setOnClickListener(v -> openAddItemFragment());

        sharedPreferences = getContext().getSharedPreferences("product_prefs", Context.MODE_PRIVATE);

        preferenceChangeListener = (sharedPreferences, key) -> {
            if (key.equals("saved_product_list")) {
                updateCurrentProductList();
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        if (languageS != null) {
            if (languageS.contains("en")) {
                btnAdd.setText("Add other product");
                btnUpdate.setText("Update");
                btnClose.setText("Close");
            }
        }

        return view;
    }

    private void fetchProductList() {
        btnAdd.setEnabled(false);
        btnUpdate.setEnabled(false);
        btnClose.setEnabled(false);

        List<ProductModel> currentProductList = adapter.getProductList();
        List<ProductModel> newProductList = new ArrayList<>();

        for (ProductModel product : currentProductList) {
            newProductList.add(new ProductModel(product.getProductID(), product.getQuantity()));
        }
        UpdateOrderRequest request = new UpdateOrderRequest(order.getTxn_ref(), newProductList);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_prefs", getContext().MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        OrderApiService service = ApiServiceProvider.getOrderApiService();
        Call<JsonObject> call = service.updateOrder("Bearer " + accessToken, request);

        if (languageS != null) {
            if (languageS.contains("en")) {
                Toast.makeText(getContext(), "Updating...", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Đang cập nhật...", Toast.LENGTH_LONG).show();
            }
        }

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    btnAdd.setEnabled(true);
                    btnUpdate.setEnabled(true);
                    btnClose.setEnabled(true);
                    if (languageS != null) {
                        if (languageS.contains("en")) {
                            Toast.makeText(getContext(), "Order updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Cập nhật đơn hàng thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (getContext() instanceof ApproveOrderActivity) {
                        ((ApproveOrderActivity) getContext()).selectSpinnerItem(0);
                    }
                    if (getContext() instanceof OrderActivity) {
                        ((OrderActivity) getContext()).switchToTab(0);
                    }
                    dismiss();
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.e("Fail", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void updateCurrentProductList() {
        List<ProductModel> currentProductList = adapter.getProductList();

        List<ProductModel> updatedProductList = getProductListFromSharedPreferences(getContext());

        if (updatedProductList != null && !updatedProductList.isEmpty()) {
            for (ProductModel newProduct : updatedProductList) {
                boolean alreadyExists = false;

                for (ProductModel existingProduct : currentProductList) {
                    if (existingProduct.getProductID() == newProduct.getProductID()) {
                        alreadyExists = true;
                        break;
                    }
                }

                if (!alreadyExists) {
                    currentProductList.add(newProduct);
                }
            }

            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    private void openAddItemFragment() {
        List<ProductModel> currentProductList = adapter.getProductList();
        AddItemFragment addItemFragment = new AddItemFragment(currentProductList);
        addItemFragment.show(getParentFragmentManager(), "AddItemFragment");
    }

    public List<ProductModel> getProductListFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("product_prefs", Context.MODE_PRIVATE);
        String jsonProductList = sharedPreferences.getString("saved_product_list", null);
        if (jsonProductList != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<ProductModel>>() {
            }.getType();
            return gson.fromJson(jsonProductList, type);
        }
        return new ArrayList<>();
    }
}