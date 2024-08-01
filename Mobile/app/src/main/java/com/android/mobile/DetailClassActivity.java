package com.android.mobile;

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
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.ClassApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailClassActivity extends AppCompatActivity {
    private TextView txtNameClass;
    private TextView txtTeacherClass;
    private TextView txtTimeLearn;
    private TextView txtAddressClass;
    private TextView txtInfoClass;
    private Button btnRegisterClass;

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

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        txtNameClass = findViewById(R.id.nameClass);
        txtTeacherClass = findViewById(R.id.nameTeacher);
        txtAddressClass = findViewById(R.id.addressLearn);
        txtTimeLearn = findViewById(R.id.timeLearn);
        txtInfoClass = findViewById(R.id.infoClass);
        btnRegisterClass = findViewById(R.id.btn_registerclass);

        String idClass = null;
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                idClass = bundle.getString("id_class");
            }
        }

        String finalIdClass = idClass;
        btnRegisterClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailClassActivity.this, RegisterClass.class);
                Bundle bundle = new Bundle();
                bundle.putString("id_class", finalIdClass);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
//                String token = sharedPreferences.getString("access_token", null);

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vdm92aW5hbW1vaS00YmVkYjZkZDFjMDUuaGVyb2t1YXBwLmNvbS9hcGkvYXV0aC9sb2dpbiIsImlhdCI6MTcyMjQ1MDczOSwiZXhwIjoxNzIyNTM3MTM5LCJuYmYiOjE3MjI0NTA3MzksImp0aSI6ImR0NnhJem9WRXgyOG96UG8iLCJzdWIiOiIyNTciLCJwcnYiOiIxMDY2NmI2ZDAzNThiMTA4YmY2MzIyYTg1OWJkZjk0MmFmYjg4ZjAyIiwibWVtYmVyX2lkIjoyNTcsInJvbGUiOjB9.Thyr4ure0t6UQiGvKh5N4DrQVJiD51m6Ah8kWbHZQWE";

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
}