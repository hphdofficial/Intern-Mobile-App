package com.android.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.models.Club;
import com.android.mobile.models.ReponseModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.ClubApiService;

import java.io.IOException;

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
    private Button btnJoinClub;
    private Button btnLeaveClub;
    private Button btnDirectClub;
    private String idClub = null;

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
        btnJoinClub = findViewById(R.id.btnJoinClub);
        btnLeaveClub = findViewById(R.id.btnLeaveClub);
        btnDirectClub = findViewById(R.id.btnDirectClub);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                idClub = bundle.getString("id_club");
            }
        }

        if (sharedPreferences.getString("id_club_shared", null) == null) {
            btnJoinClub.setVisibility(View.VISIBLE);
        } else if (idClub.equals(String.valueOf(sharedPreferences.getString("id_club_shared", null)))) {
            btnLeaveClub.setVisibility(View.VISIBLE);
            Toast.makeText(DetailClubActivity.this, "Bạn là thành viên của câu lạc bộ này", Toast.LENGTH_SHORT).show();
        } else {
            btnDirectClub.setVisibility(View.VISIBLE);
            Toast.makeText(DetailClubActivity.this, "Bạn đã tham gia câu lạc bộ khác", Toast.LENGTH_SHORT).show();
        }

        btnJoinClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinClub();
            }
        });

        btnLeaveClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.getString("id_class_shared", null) == null) {
                    leaveClub();
                } else {
                    Intent intent = new Intent(DetailClubActivity.this, DetailClassActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id_class", sharedPreferences.getString("id_class_shared", null));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Toast.makeText(DetailClubActivity.this, "Bạn đang tham gia lớp học của câu lạc bộ này nên chưa thể rời câu lạc bộ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDirectClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directClub();
            }
        });

        getDetailClub();
//        getMyClub();
    }

    public void getDetailClub() {
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
                    Toast.makeText(DetailClubActivity.this, "That bai " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Club> call, Throwable t) {
                Toast.makeText(DetailClubActivity.this, "Loi " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

    public void joinClub() {
        String token = sharedPreferences.getString("access_token", null);

        ClubApiService service = ApiServiceProvider.getClubApiService();
        Club data = new Club(idClub);
        Call<ReponseModel> call = service.joinClub("Bearer" + token, data);

        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                if (response.isSuccessful()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("id_club_shared", idClub);
                    editor.apply();

                    btnJoinClub.setVisibility(View.GONE);
                    btnLeaveClub.setVisibility(View.VISIBLE);

                    Toast.makeText(DetailClubActivity.this, "Tham gia câu lạc bộ thành công", Toast.LENGTH_SHORT).show();

//                    Intent intent = new Intent(DetailClubActivity.this, ClassActivity.class);
//                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                Toast.makeText(DetailClubActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void leaveClub() {
        String token = sharedPreferences.getString("access_token", null);

        ClubApiService service = ApiServiceProvider.getClubApiService();
        Call<ReponseModel> call = service.leaveClub("Bearer" + token);

        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                if (response.isSuccessful()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("id_club_shared", null);
                    editor.apply();

                    btnLeaveClub.setVisibility(View.GONE);
                    btnJoinClub.setVisibility(View.VISIBLE);

                    Toast.makeText(DetailClubActivity.this, "Rời câu lạc bộ thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                Toast.makeText(DetailClubActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void directClub() {
        if (sharedPreferences.getString("id_club_shared", null) != null) {
            Intent intent = new Intent(DetailClubActivity.this, DetailClubActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id_club", sharedPreferences.getString("id_club_shared", null));
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            btnDirectClub.setVisibility(View.GONE);
            btnJoinClub.setVisibility(View.VISIBLE);
            Toast.makeText(DetailClubActivity.this, "Bạn chưa tham gia câu lạc bộ nào", Toast.LENGTH_SHORT).show();
        }
    }
}