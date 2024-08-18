package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fascon.vovinam.vn.ViewModel.ApproveAdapter;
import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.Model.OrderListModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.OrderApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApproveOrderActivity extends BaseActivity {
    private Spinner spinnerOptions;
    private RecyclerView recyclerView;
    private ApproveAdapter approveAdapter;
    private TextView txtNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_approve);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Duyệt đơn hàng");
        myContentE.apply();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new titleFragment());
        fragmentTransaction.commit();

        txtNotify = findViewById(R.id.txt_notify);
        spinnerOptions = findViewById(R.id.spinner_options);
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String[] options = {
                "Đơn hàng chờ xác nhận",
                "Đơn hàng chờ lấy hàng",
                "Đơn hàng đang giao"
        };

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOptions.setAdapter(spinnerAdapter);
        selectSpinnerItem(0);
    }

    private void getListConfirmOrder() {
        txtNotify.setText("Loading...");
        approveAdapter = new ApproveAdapter(this, new ArrayList<>(), "confirmorder");
        recyclerView.setAdapter(approveAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        OrderApiService service = ApiServiceProvider.getOrderApiService();
        Call<List<OrderListModel>> call = service.getOrderCoach("Bearer " + accessToken);

        call.enqueue(new Callback<List<OrderListModel>>() {
            @Override
            public void onResponse(Call<List<OrderListModel>> call, Response<List<OrderListModel>> response) {
                if (response.isSuccessful()) {
                    List<OrderListModel> orders = response.body();
                    List<OrderListModel> orderList = new ArrayList<>();
                    if (orders != null && !orders.isEmpty()) {
                        for (OrderListModel order : orders) {
                            if ("chờ xác nhận".equalsIgnoreCase(order.getGiao_hang())) {
                                orderList.add(order);
                            }
                        }
                        if (!orderList.isEmpty()) {
                            Collections.sort(orderList, new Comparator<OrderListModel>() {
                                @Override
                                public int compare(OrderListModel o1, OrderListModel o2) {
                                    return o2.getPay_date().compareTo(o1.getPay_date());
                                }
                            });
                            approveAdapter.setDataOrder(orderList);
                            txtNotify.setText("");
                        } else {
                            txtNotify.setText("Không có đơn hàng nào");
                        }
                    }
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<OrderListModel>> call, @NonNull Throwable t) {
                Log.e("Fail", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void getListGetOrder() {
        txtNotify.setText("Loading...");
        approveAdapter = new ApproveAdapter(this, new ArrayList<>(), "getorder");
        recyclerView.setAdapter(approveAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        OrderApiService service = ApiServiceProvider.getOrderApiService();
        Call<List<OrderListModel>> call = service.getOrderCoach("Bearer " + accessToken);

        call.enqueue(new Callback<List<OrderListModel>>() {
            @Override
            public void onResponse(Call<List<OrderListModel>> call, Response<List<OrderListModel>> response) {
                if (response.isSuccessful()) {
                    List<OrderListModel> orders = response.body();
                    List<OrderListModel> orderList = new ArrayList<>();
                    if (orders != null && !orders.isEmpty()) {
                        for (OrderListModel order : orders) {
                            if ("chờ lấy hàng".equalsIgnoreCase(order.getGiao_hang())) {
                                orderList.add(order);
                            }
                        }
                        if (!orderList.isEmpty()) {
                            Collections.sort(orderList, new Comparator<OrderListModel>() {
                                @Override
                                public int compare(OrderListModel o1, OrderListModel o2) {
                                    return o2.getPay_date().compareTo(o1.getPay_date());
                                }
                            });
                            approveAdapter.setDataOrder(orderList);
                            txtNotify.setText("");
                        } else {
                            txtNotify.setText("Không có đơn hàng nào");
                        }
                    }
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<OrderListModel>> call, @NonNull Throwable t) {
                Log.e("Fail", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void getListShipOrder() {
        txtNotify.setText("Loading...");
        approveAdapter = new ApproveAdapter(this, new ArrayList<>(), "shiporder");
        recyclerView.setAdapter(approveAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        OrderApiService service = ApiServiceProvider.getOrderApiService();
        Call<List<OrderListModel>> call = service.getOrderCoach("Bearer " + accessToken);

        call.enqueue(new Callback<List<OrderListModel>>() {
            @Override
            public void onResponse(Call<List<OrderListModel>> call, Response<List<OrderListModel>> response) {
                if (response.isSuccessful()) {
                    List<OrderListModel> orders = response.body();
                    List<OrderListModel> orderList = new ArrayList<>();
                    if (orders != null && !orders.isEmpty()) {
                        for (OrderListModel order : orders) {
                            if ("đang giao hàng".equalsIgnoreCase(order.getGiao_hang())) {
                                orderList.add(order);
                            }
                        }
                        if (!orderList.isEmpty()) {
                            Collections.sort(orderList, new Comparator<OrderListModel>() {
                                @Override
                                public int compare(OrderListModel o1, OrderListModel o2) {
                                    return o2.getPay_date().compareTo(o1.getPay_date());
                                }
                            });
                            approveAdapter.setDataOrder(orderList);
                            txtNotify.setText("");
                        } else {
                            txtNotify.setText("Không có đơn hàng nào");
                        }
                    }
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<OrderListModel>> call, @NonNull Throwable t) {
                Log.e("Fail", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    public void selectSpinnerItem(int position) {
        if (position >= 0 && position < spinnerOptions.getCount()) {
            spinnerOptions.setSelection(position);
            spinnerOptions.setOnItemSelectedListener(null);
            switch (position) {
                case 0:
                    getListConfirmOrder();
                    break;
                case 1:
                    getListGetOrder();
                    break;
                case 2:
                    getListShipOrder();
                    break;
                default:
                    break;
            }
            spinnerOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            getListConfirmOrder();
                            break;
                        case 1:
                            getListGetOrder();
                            break;
                        case 2:
                            getListShipOrder();
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else {
            Log.e("ApproveOrderActivity", "Invalid position: " + position);
        }
    }

    public void setEmptyList(){
        txtNotify.setText("Không có đơn hàng nào");
    }
}