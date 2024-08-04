package com.android.mobile;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.adapter.ClubAdapter;
import com.android.mobile.adapter.HistoryOrderAdapter;
import com.android.mobile.models.Club;
import com.android.mobile.models.OrderModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.ClubApiService;
import com.android.mobile.services.OrderApiService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryOrderActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private HistoryOrderAdapter adapter;
    private List<OrderModel> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        adapter = new HistoryOrderAdapter(this, orderList);
        recyclerView = findViewById(R.id.recycler_purchase_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        OrderApiService service = ApiServiceProvider.getOrderApiService();
        Call<List<OrderModel>> call = service.getHistoryOrder(101);

        call.enqueue(new Callback<List<OrderModel>>() {
            @Override
            public void onResponse(Call<List<OrderModel>> call, Response<List<OrderModel>> response) {
                if (response.isSuccessful()) {
                    List<OrderModel> orders = response.body();
                    adapter.setData(orders);
                    Toast.makeText(HistoryOrderActivity.this, "Success " + response.message(), Toast.LENGTH_SHORT).show();
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<OrderModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}