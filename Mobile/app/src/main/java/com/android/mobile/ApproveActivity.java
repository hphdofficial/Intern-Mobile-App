package com.android.mobile;

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

import com.android.mobile.adapter.ApproveAdapter;
import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.models.ApproveModel;
import com.android.mobile.models.OrderListModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.ClassApiService;
import com.android.mobile.services.ClubApiService;
import com.android.mobile.services.OrderApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApproveActivity extends BaseActivity {
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
        myContentE.putString("title", "Duyệt yêu cầu");
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
                "Yêu cầu tham gia câu lạc bộ",
                "Yêu cầu đăng ký lớp học",
                "Yêu cầu rời câu lạc bộ",
                "Yêu cầu rời lớp học"
        };

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOptions.setAdapter(spinnerAdapter);
        selectSpinnerItem(0);
    }

    private void getListJoinClub() {
        txtNotify.setText("Loading...");
        approveAdapter = new ApproveAdapter(this, new ArrayList<>(), "joinclub");
        recyclerView.setAdapter(approveAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        ClubApiService service = ApiServiceProvider.getClubApiService();
        Call<List<ApproveModel>> call = service.getListJoinClubPending("Bearer " + accessToken);

        call.enqueue(new Callback<List<ApproveModel>>() {
            @Override
            public void onResponse(Call<List<ApproveModel>> call, Response<List<ApproveModel>> response) {
                if (response.isSuccessful()) {
                    List<ApproveModel> list = response.body();
                    if (list != null && !list.isEmpty()) {
//                        Collections.sort(list, new Comparator<ApproveModel>() {
//                            @Override
//                            public int compare(ApproveModel o1, ApproveModel o2) {
//                                return o2.getCreated_at().compareTo(o1.getCreated_at());
//                            }
//                        });
                        approveAdapter.setDataClubClass(list);
                        txtNotify.setText("");
                    } else {
                        txtNotify.setText("Không có yêu cầu nào");
                    }
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ApproveModel>> call, @NonNull Throwable t) {
                Log.e("Fail", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void getListJoinClass() {
        txtNotify.setText("Loading...");
        approveAdapter = new ApproveAdapter(this, new ArrayList<>(), "joinclass");
        recyclerView.setAdapter(approveAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<List<ApproveModel>> call = service.getListJoinClassPending("Bearer " + accessToken);

        call.enqueue(new Callback<List<ApproveModel>>() {
            @Override
            public void onResponse(Call<List<ApproveModel>> call, Response<List<ApproveModel>> response) {
                if (response.isSuccessful()) {
                    List<ApproveModel> list = response.body();
                    if (list != null && !list.isEmpty()) {
//                        Collections.sort(list, new Comparator<ApproveModel>() {
//                            @Override
//                            public int compare(ApproveModel o1, ApproveModel o2) {
//                                return o2.getCreated_at().compareTo(o1.getCreated_at());
//                            }
//                        });
                        approveAdapter.setDataClubClass(list);
                        txtNotify.setText("");
                    } else {
                        txtNotify.setText("Không có yêu cầu nào");
                    }
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ApproveModel>> call, @NonNull Throwable t) {
                Log.e("Fail", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void getListLeaveClub() {
        txtNotify.setText("Loading...");
        approveAdapter = new ApproveAdapter(this, new ArrayList<>(), "leaveclub");
        recyclerView.setAdapter(approveAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        ClubApiService service = ApiServiceProvider.getClubApiService();
        Call<List<ApproveModel>> call = service.getListLeaveClubPending("Bearer " + accessToken);

        call.enqueue(new Callback<List<ApproveModel>>() {
            @Override
            public void onResponse(Call<List<ApproveModel>> call, Response<List<ApproveModel>> response) {
                if (response.isSuccessful()) {
                    List<ApproveModel> list = response.body();
                    if (list != null && !list.isEmpty()) {
//                        Collections.sort(list, new Comparator<ApproveModel>() {
//                            @Override
//                            public int compare(ApproveModel o1, ApproveModel o2) {
//                                return o2.getCreated_at().compareTo(o1.getCreated_at());
//                            }
//                        });
                        approveAdapter.setDataClubClass(list);
                        txtNotify.setText("");
                    } else {
                        txtNotify.setText("Không có yêu cầu nào");
                    }
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ApproveModel>> call, @NonNull Throwable t) {
                Log.e("Fail", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void getListLeaveClass() {
        txtNotify.setText("Loading...");
        approveAdapter = new ApproveAdapter(this, new ArrayList<>(), "leaveclass");
        recyclerView.setAdapter(approveAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<List<ApproveModel>> call = service.getListLeaveClassPending("Bearer " + accessToken);

        call.enqueue(new Callback<List<ApproveModel>>() {
            @Override
            public void onResponse(Call<List<ApproveModel>> call, Response<List<ApproveModel>> response) {
                if (response.isSuccessful()) {
                    List<ApproveModel> list = response.body();
                    if (list != null && !list.isEmpty()) {
//                        Collections.sort(list, new Comparator<ApproveModel>() {
//                            @Override
//                            public int compare(ApproveModel o1, ApproveModel o2) {
//                                return o2.getCreated_at().compareTo(o1.getCreated_at());
//                            }
//                        });
                        approveAdapter.setDataClubClass(list);
                        txtNotify.setText("");
                    } else {
                        txtNotify.setText("Không có yêu cầu nào");
                    }
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ApproveModel>> call, @NonNull Throwable t) {
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
                    getListJoinClub();
                    break;
                case 1:
                    getListJoinClass();
                    break;
                case 2:
                    getListLeaveClub();
                    break;
                case 3:
                    getListLeaveClass();
                    break;
                default:
                    break;
            }
            spinnerOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            getListJoinClub();
                            break;
                        case 1:
                            getListJoinClass();
                            break;
                        case 2:
                            getListLeaveClub();
                            break;
                        case 3:
                            getListLeaveClass();
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
            Log.e("ApproveActivity", "Invalid position: " + position);
        }
    }

    public void setEmptyList(){
        txtNotify.setText("Không có yêu cầu nào");
    }
}