package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.Model.Club;
import fascon.vovinam.vn.Model.ReponseModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.ClubApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailClubActivity extends BaseActivity {
    private SharedPreferences sharedPreferences;
    private TextView txtNameClub;
    private TextView txtDesClub;
    private TextView txtAddressClub;
    private TextView txtPhoneClub;
    private TextView txtManagerClub;
    private Button btnJoinClubPending;
    private Button btnCancelClubPending;
    private Button btnLeaveClubPending;
    private Button btnDirectClub;
    private String idClub = null;
    private List<Club> listClubPending = new ArrayList<>();
    private boolean isPending = false;
    private BlankFragment loadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_club);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);

        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Chi tiết câu lạc bộ");
        myContentE.apply();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new titleFragment());
        fragmentTransaction.commit();

        txtNameClub = findViewById(R.id.txtNameDetailClub);
        txtDesClub = findViewById(R.id.txtDesDetailClub);
        txtAddressClub = findViewById(R.id.txtAddressDetailClub);
        txtPhoneClub = findViewById(R.id.txtPhoneDetailClub);
        txtManagerClub = findViewById(R.id.txtManagerDetailClub);
        btnJoinClubPending = findViewById(R.id.btn_join_club_pending);
        btnCancelClubPending = findViewById(R.id.btn_cancel_club_pending);
        btnLeaveClubPending = findViewById(R.id.btn_leave_club_pending);
        btnDirectClub = findViewById(R.id.btn_direct_club);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                idClub = bundle.getString("id_club");
            }
        }

        btnJoinClubPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinClubPending();
            }
        });

        btnCancelClubPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelClubPending();
            }
        });

        btnLeaveClubPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveClubPending();
            }
        });

        btnDirectClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directJoinedClub();
            }
        });

        getDetailClub();
    }

    public void getDetailClub() {
        showLoading();

        getListClubPending();

        ClubApiService service = ApiServiceProvider.getClubApiService();
        Call<Club> call = service.getDetailClub(Integer.parseInt(idClub));

        call.enqueue(new Callback<Club>() {
            @Override
            public void onResponse(Call<Club> call, Response<Club> response) {
                if (response.isSuccessful()) {
                    Club clb = response.body();
                    txtNameClub.setText(clb.getTen());
                    txtDesClub.setText(clb.getMota());
                    txtAddressClub.setText(clb.getDiachi());
                    txtPhoneClub.setText(clb.getDienthoai());
                    txtManagerClub.setText(clb.getNguoiquanly());
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<Club> call, Throwable t) {
                hideLoading();

                Log.e("Fail", t.getMessage());
            }
        });
    }

    public void getListClubPending() {
        String token = sharedPreferences.getString("access_token", null);

        ClubApiService service = ApiServiceProvider.getClubApiService();
        Call<List<Club>> call = service.getListClubPending("Bearer" + token);

        call.enqueue(new Callback<List<Club>>() {
            @Override
            public void onResponse(Call<List<Club>> call, Response<List<Club>> response) {
                hideLoading();

                if (response.isSuccessful()) {
                    listClubPending = response.body();
                    if (!listClubPending.isEmpty()) {
                        for (Club clb : listClubPending) {
                            if (clb.getId_club().equals(idClub)) {
                                isPending = true;
                                break;
                            }
                            isPending = false;
                        }
                    }
                    setupButton();
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Club>> call, Throwable t) {
                hideLoading();

                Log.e("Fail", t.getMessage());
            }
        });
    }

    public void setupButton() {
        String idClubShared = sharedPreferences.getString("id_club_shared", null);

        if (idClubShared == null) {
            btnJoinClubPending.setVisibility(isPending ? View.GONE : View.VISIBLE);
            btnCancelClubPending.setVisibility(isPending ? View.VISIBLE : View.GONE);
        } else if (!idClub.equals(idClubShared)) {
            btnDirectClub.setVisibility(View.VISIBLE);
        } else {
            btnLeaveClubPending.setVisibility(View.VISIBLE);
        }
    }

    public void joinClubPending() {
        btnJoinClubPending.setEnabled(false);
        Toast.makeText(DetailClubActivity.this, "Đang xử lý...", Toast.LENGTH_SHORT).show();

        String token = sharedPreferences.getString("access_token", null);

        ClubApiService service = ApiServiceProvider.getClubApiService();
        Call<ReponseModel> call = service.joinClubPending("Bearer" + token, Integer.parseInt(idClub));

        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                if (response.isSuccessful()) {
                    isPending = true;
                    setupButton();
                    btnJoinClubPending.setEnabled(true);
                    Toast.makeText(DetailClubActivity.this, "Đã gửi yêu cầu tham gia", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                Log.e("Fail", t.getMessage());
            }
        });
    }

    public void cancelClubPending() {
        btnCancelClubPending.setEnabled(false);
        Toast.makeText(DetailClubActivity.this, "Đang xử lý...", Toast.LENGTH_SHORT).show();

        String token = sharedPreferences.getString("access_token", null);

        ClubApiService service = ApiServiceProvider.getClubApiService();
        Call<ReponseModel> call = service.cancelClubPending("Bearer" + token, Integer.parseInt(idClub));

        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                if (response.isSuccessful()) {
                    isPending = false;
                    setupButton();
                    btnCancelClubPending.setEnabled(true);
                    Toast.makeText(DetailClubActivity.this, "Đã hủy yêu cầu tham gia", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                Log.e("Fail", t.getMessage());
            }
        });
    }

    private void leaveClubPending() {
        btnLeaveClubPending.setEnabled(false);
        Toast.makeText(DetailClubActivity.this, "Đang xử lý...", Toast.LENGTH_SHORT).show();

        String token = sharedPreferences.getString("access_token", null);

        ClubApiService service = ApiServiceProvider.getClubApiService();
        Call<ReponseModel> call = service.leaveClubPending("Bearer" + token);

        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                if (response.isSuccessful()) {
                    isPending = false;
                    setupButton();
                    btnLeaveClubPending.setEnabled(true);
                    btnLeaveClubPending.setVisibility(View.GONE);
                    Toast.makeText(DetailClubActivity.this, "Đã gửi yêu cầu rời câu lạc bộ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailClubActivity.this, "Yêu cầu của bạn đang chờ duyệt", Toast.LENGTH_SHORT).show();
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                Toast.makeText(DetailClubActivity.this, "Yêu cầu của bạn đang chờ duyệt", Toast.LENGTH_SHORT).show();
                Log.e("Fail", t.getMessage());
            }
        });
    }

    public void directJoinedClub() {
        Intent intent = new Intent(DetailClubActivity.this, DetailClubActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id_club", sharedPreferences.getString("id_club_shared", null));
        intent.putExtras(bundle);
        startActivity(intent);
    }

//    public void getMyClub() {
//        String token = sharedPreferences.getString("access_token", null);
//
//        ClubApiService service = ApiServiceProvider.getClubApiService();
//        Call<Club> call = service.getDetailClubMember("Bearer" + token);
//
//        call.enqueue(new Callback<Club>() {
//            @Override
//            public void onResponse(Call<Club> call, Response<Club> response) {
//                if (response.isSuccessful()) {
//                    Club clb = response.body();
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("id_club_shared", clb != null ? clb.getId() : null);
//                    editor.apply();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Club> call, Throwable t) {
//                Toast.makeText(DetailClubActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    public void joinClub() {
//        showLoading();
//
//        String token = sharedPreferences.getString("access_token", null);
//
//        ClubApiService service = ApiServiceProvider.getClubApiService();
//        Club data = new Club(idClub);
//        Call<ReponseModel> call = service.joinClub("Bearer" + token, data);
//
//        call.enqueue(new Callback<ReponseModel>() {
//            @Override
//            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
//                hideLoading();
//
//                if (response.isSuccessful()) {
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("id_club_shared", idClub);
//                    editor.apply();
//
//                    btnJoinClubPending.setVisibility(View.GONE);
//                    btnCancelClubPending.setVisibility(View.VISIBLE);
//
//                    Toast.makeText(DetailClubActivity.this, "Tham gia câu lạc bộ thành công", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ReponseModel> call, Throwable t) {
//                hideLoading();
//
//                Toast.makeText(DetailClubActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    public void leaveClub() {
//        showLoading();
//
//        String token = sharedPreferences.getString("access_token", null);
//
//        ClubApiService service = ApiServiceProvider.getClubApiService();
//        Call<ReponseModel> call = service.leaveClub("Bearer" + token);
//
//        call.enqueue(new Callback<ReponseModel>() {
//            @Override
//            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
//                hideLoading();
//                if (response.isSuccessful()) {
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("id_club_shared", null);
//                    editor.apply();
//
//                    btnCancelClubPending.setVisibility(View.GONE);
//                    btnJoinClubPending.setVisibility(View.VISIBLE);
//
//                    Toast.makeText(DetailClubActivity.this, "Rời câu lạc bộ thành công", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ReponseModel> call, Throwable t) {
//                hideLoading();
//
//                Toast.makeText(DetailClubActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

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