package fascon.vovinam.vn.View;

import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import fascon.vovinam.vn.Model.ApproveModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.ClassApiService;
import fascon.vovinam.vn.Model.services.ClubApiService;

import java.util.ArrayList;
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
    private String languageS;

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
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language", null);
        if (languageS != null) {
            if (languageS.contains("en")) {
                myContentE.putString("title", "Browse requests");
                myContentE.apply();
            }
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new titleFragment());
        fragmentTransaction.commit();

        txtNotify = findViewById(R.id.txt_notify);
        spinnerOptions = findViewById(R.id.spinner_options);
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String[] options = {
                "Yêu cầu đăng ký lớp học",
                "Yêu cầu rời lớp học"
        };
        if (languageS != null) {
            if (languageS.contains("en")) {
                options[0] = "Class registration required";
                options[1] = "Request to leave class";
            }
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOptions.setAdapter(spinnerAdapter);
        selectSpinnerItem(0);
    }

    private TextView text;

    public void onMenuItemClick(View view) {
        text = findViewById(R.id.languageText);
        String language = text.getText() + "";
        if (view.getId() == R.id.btn_change) {
            SharedPreferences sga = getSharedPreferences("login_prefs", MODE_PRIVATE);
            SharedPreferences.Editor edit = sga.edit();

            if (language.contains("VN")) {
                edit.putString("language", "en");
                text.setText("ENG");
            } else {
                edit.putString("language", "vn");
                text.setText("VN");
            }
            edit.apply();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    private void getListJoinClub() {
        if (languageS.contains("en")) {
            txtNotify.setText("Loading...");
        } else {
            txtNotify.setText("Đang tải...");
        }
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
                        if (languageS.contains("en")) {
                            txtNotify.setText("No requirements");
                        } else {
                            txtNotify.setText("Không có yêu cầu nào");
                        }
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
        if (languageS.contains("en")) {
            txtNotify.setText("Loading...");
        } else {
            txtNotify.setText("Đang tải...");
        }
        approveAdapter = new ApproveAdapter(this, new ArrayList<>(), "joinclass");
        recyclerView.setAdapter(approveAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<List<ApproveModel>> call = service.getListJoinClassPending("Bearer " + accessToken, languageS.equals("vn") ? "vi" : "en");

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
                        if (languageS.contains("en")) {
                            txtNotify.setText("No requirements");
                        } else {
                            txtNotify.setText("Không có yêu cầu nào");
                        }
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
        if (languageS.contains("en")) {
            txtNotify.setText("Loading...");
        } else {
            txtNotify.setText("Đang tải...");
        }
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
                        if (languageS.contains("en")) {
                            txtNotify.setText("No requirements");
                        } else {
                            txtNotify.setText("Không có yêu cầu nào");
                        }
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
        if (languageS.contains("en")) {
            txtNotify.setText("Loading...");
        } else {
            txtNotify.setText("Đang tải...");
        }
        approveAdapter = new ApproveAdapter(this, new ArrayList<>(), "leaveclass");
        recyclerView.setAdapter(approveAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<List<ApproveModel>> call = service.getListLeaveClassPending("Bearer " + accessToken, languageS.equals("vn") ? "vi" : "en");

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
                        if (languageS.contains("en")) {
                            txtNotify.setText("No requirements");
                        } else {
                            txtNotify.setText("Không có yêu cầu nào");
                        }
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
                    getListJoinClass();
                    break;
                case 1:
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
                            getListJoinClass();
                            break;
                        case 1:
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

    public void setEmptyList() {
        if (languageS.contains("en")) {
            txtNotify.setText("No requirements");
        } else {
            txtNotify.setText("Không có yêu cầu nào");
        }
    }
}