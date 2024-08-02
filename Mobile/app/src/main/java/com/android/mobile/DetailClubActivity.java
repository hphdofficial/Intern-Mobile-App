package com.android.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.mobile.models.Club;
import com.android.mobile.models.ReponseModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.ClubApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailClubActivity extends AppCompatActivity {
    private TextView txtNameClub;
    private TextView txtDesClub;
    private TextView txtAddressClub;
    private TextView txtPhoneClub;
    private TextView txtManagerClub;
    private Button btnJoinClub;

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

        String idClub = null;
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                idClub = bundle.getString("id_club");
            }
        }

        String finalIdClub = idClub;
        btnJoinClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vdm92aW5hbW1vaS00YmVkYjZkZDFjMDUuaGVyb2t1YXBwLmNvbS9hcGkvYXV0aC9sb2dpbiIsImlhdCI6MTcyMjQ1MDczOSwiZXhwIjoxNzIyNTM3MTM5LCJuYmYiOjE3MjI0NTA3MzksImp0aSI6ImR0NnhJem9WRXgyOG96UG8iLCJzdWIiOiIyNTciLCJwcnYiOiIxMDY2NmI2ZDAzNThiMTA4YmY2MzIyYTg1OWJkZjk0MmFmYjg4ZjAyIiwibWVtYmVyX2lkIjoyNTcsInJvbGUiOjB9.Thyr4ure0t6UQiGvKh5N4DrQVJiD51m6Ah8kWbHZQWE";

                SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
//                String token = sharedPreferences.getString("access_token", null);

                ClubApiService service = ApiServiceProvider.getClubApiService();
                Club data = new Club(finalIdClub);
                Call<ReponseModel> call = service.joinClub("Bearer" + token, data);

                call.enqueue(new Callback<ReponseModel>() {
                    @Override
                    public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                        if (response.isSuccessful()) {
                            ReponseModel clb = response.body();

                            Toast.makeText(DetailClubActivity.this, "Tham gia CLB thành công", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(DetailClubActivity.this, ClassActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("id_club", finalIdClub);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            Toast.makeText(DetailClubActivity.this, "Bạn đã là thành viên của CLB này rồi", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReponseModel> call, Throwable t) {
                        Toast.makeText(DetailClubActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

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
}