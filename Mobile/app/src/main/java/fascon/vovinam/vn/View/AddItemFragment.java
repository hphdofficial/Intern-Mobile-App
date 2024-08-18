package fascon.vovinam.vn.View;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fascon.vovinam.vn.Model.ProductModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.OrderApiService;
import fascon.vovinam.vn.R;
import fascon.vovinam.vn.ViewModel.CartAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItemFragment extends DialogFragment {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<ProductModel> currentProductList = new ArrayList<>();
    private List<ProductModel> productList = new ArrayList<>();
    private List<ProductModel> filteredProductList = new ArrayList<>();
    private SearchView searchView;
    private Button btnClose;
    private TextView txtNotify;

    public AddItemFragment(List<ProductModel> list) {
        this.currentProductList = list;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        setCancelable(false);

        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new CartAdapter(getContext(), productList, true, true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        btnClose = view.findViewById(R.id.button_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        txtNotify = view.findViewById(R.id.txt_notify);

        getAllProduct();

        searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterRecyclerView(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterRecyclerView(newText);
                return false;
            }
        });

        return view;
    }

    public void getAllProduct() {
        OrderApiService service = ApiServiceProvider.getOrderApiService();
        Call<List<ProductModel>> call = service.getAllProduct();

        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if (response.isSuccessful()) {
                    txtNotify.setText("");
                    productList = response.body();

                    if (productList != null && currentProductList != null) {
                        for (ProductModel currentProduct : currentProductList) {
                            for (Iterator<ProductModel> iterator = productList.iterator(); iterator.hasNext();) {
                                ProductModel product = iterator.next();

                                if (product.getProductID() == currentProduct.getProductID()) {
                                    iterator.remove();
                                }
                            }
                        }
                    }

                    adapter.setData(productList);
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {
                Log.e("Fail", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void filterRecyclerView(String query) {
        filteredProductList.clear();
        if (query.isEmpty()) {
            filteredProductList.addAll(productList);
        } else {
            for (ProductModel product : productList) {
                if (product.getProductName().toLowerCase().contains(query.toLowerCase())) {
                    filteredProductList.add(product);
                }
            }
        }
        adapter.setData(filteredProductList);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }
}