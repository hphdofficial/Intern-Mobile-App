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

import com.android.mobile.models.Class;
import com.android.mobile.models.Club;
import com.android.mobile.models.ReponseModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.ClassApiService;
import com.android.mobile.services.ClubApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailClassActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private TextView txtNameClass;
    private TextView txtTeacherClass;
    private TextView txtTimeLearn;
    private TextView txtAddressClass;
    private TextView txtInfoClass;
    private Button btnRegisterClass;
    private Button btnLeaveClass;
    private Button btnDirectClass;
    private String idClass = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);

        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Chi tiết lớp học");
        myContentE.apply();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new titleFragment());
        fragmentTransaction.commit();

        txtNameClass = findViewById(R.id.nameClass);
        txtTeacherClass = findViewById(R.id.nameTeacher);
        txtAddressClass = findViewById(R.id.addressLearn);
        txtTimeLearn = findViewById(R.id.timeLearn);
        txtInfoClass = findViewById(R.id.infoClass);
        btnRegisterClass = findViewById(R.id.btn_register_class);
        btnLeaveClass = findViewById(R.id.btn_leave_class);
        btnDirectClass = findViewById(R.id.btn_direct_class);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                idClass = bundle.getString("id_class");
            }
        }

        if (sharedPreferences.getString("id_class_shared", null) == null) {
            btnRegisterClass.setVisibility(View.VISIBLE);
        }
        if (idClass.equals(sharedPreferences.getString("id_class_shared", null))) {
            btnLeaveClass.setVisibility(View.VISIBLE);
            Toast.makeText(DetailClassActivity.this, "Bạn là thành viên của lớp học này", Toast.LENGTH_SHORT).show();
        } else {
            btnDirectClass.setVisibility(View.VISIBLE);
            Toast.makeText(DetailClassActivity.this, "Bạn đã tham gia lớp học khác", Toast.LENGTH_SHORT).show();
        }

        btnRegisterClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.getString("id_club_shared", null) != null) {
                    registerClass();
                } else {
                    Toast.makeText(DetailClassActivity.this, "Bạn chưa tham gia câu lạc bộ nào nên không thể đăng ký lớp học", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLeaveClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveClass();
            }
        });

        btnDirectClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directClass();
            }
        });

        getDetailClass();
//        getMyClass();
    }

    public void getDetailClass() {
        String token = sharedPreferences.getString("access_token", null);

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<Class> call = service.getDetailClassofClub("Bearer" + token, Integer.parseInt(idClass));

        call.enqueue(new Callback<Class>() {
            @Override
            public void onResponse(Call<Class> call, Response<Class> response) {
                if (response.isSuccessful()) {
                    Class dataClass = response.body();
                    txtNameClass.setText(dataClass.getTen());
                    txtTeacherClass.setText(dataClass.getGiangvien());
                    txtAddressClass.setText(dataClass.getClub());
                    txtTimeLearn.setText(dataClass.getThoigian());
                    txtInfoClass.setText(dataClass.getGiangvien());
                } else {
                    Toast.makeText(DetailClassActivity.this, "That bai " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Class> call, Throwable t) {
                Toast.makeText(DetailClassActivity.this, "Loi " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void getMyClass() {
//        String token = sharedPreferences.getString("access_token", null);
//
//        ClassApiService service = ApiServiceProvider.getClassApiService();
//        Call<Class> call = service.getDetailClassMember("Bearer" + token);
//
//        call.enqueue(new Callback<Class>() {
//            @Override
//            public void onResponse(Call<Class> call, Response<Class> response) {
//                if (response.isSuccessful()) {
//                    Class classs = response.body();
//
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("id_class_shared", String.valueOf(classs != null ? classs.getId() : null));
//                    editor.apply();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Class> call, Throwable t) {
//                Toast.makeText(DetailClassActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    public void registerClass() {
        Intent intent = new Intent(DetailClassActivity.this, RegisterClass.class);
        Bundle bundle = new Bundle();
        bundle.putString("id_class", idClass);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void leaveClass() {
        String token = sharedPreferences.getString("access_token", null);

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<ReponseModel> call = service.leaveClass("Bearer" + token, new Class(Integer.parseInt(idClass)));

        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                if (response.isSuccessful()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("id_class_shared", null);
                    editor.apply();

                    btnLeaveClass.setVisibility(View.GONE);
                    btnRegisterClass.setVisibility(View.VISIBLE);

                    Toast.makeText(DetailClassActivity.this, "Rời lớp học thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                Toast.makeText(DetailClassActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void directClass() {
        if (sharedPreferences.getString("id_class_shared", null) != null) {
            Intent intent = new Intent(DetailClassActivity.this, DetailClassActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id_class", sharedPreferences.getString("id_class_shared", null));
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            btnDirectClass.setVisibility(View.GONE);
            btnRegisterClass.setVisibility(View.VISIBLE);
            Toast.makeText(DetailClassActivity.this, "Bạn chưa tham gia lớp học nào", Toast.LENGTH_SHORT).show();
        }
    }
}