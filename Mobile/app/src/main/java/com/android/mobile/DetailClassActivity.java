package com.android.mobile;

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

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.models.Class;
import com.android.mobile.models.Club;
import com.android.mobile.models.ReponseModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.ClassApiService;
import com.android.mobile.services.ClubApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailClassActivity extends BaseActivity {
    private SharedPreferences sharedPreferences;
    private TextView txtNameClass;
    private TextView txtTeacherClass;
    private TextView txtTimeLearn;
    private TextView txtAddressClass;
    private TextView txtInfoClass;
    private Button btnJoinClassPending;
    private Button btnCancelClassPending;
    private Button btnLeaveClassPending;
    private Button btnDirectClass;
    private String idClass = null;
    private List<Class> listClassPending = new ArrayList<>();
    private boolean isPending = false;
    private String name = "";
    private String nameClass = "";
    private BlankFragment loadingFragment;

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
        btnJoinClassPending = findViewById(R.id.btn_join_class_pending);
        btnCancelClassPending = findViewById(R.id.btn_cancel_class_pending);
        btnLeaveClassPending = findViewById(R.id.btn_leave_class_pending);
        btnDirectClass = findViewById(R.id.btn_direct_class);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                idClass = bundle.getString("id_class");
            }
        }

        btnJoinClassPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinClassPending();
            }
        });

        btnCancelClassPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelClassPending();
            }
        });

        btnLeaveClassPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveClassPending();
            }
        });

        btnDirectClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directJoinedClass();
            }
        });

        getDetailClass();
    }

    public void getDetailClass() {
        showLoading();

        getListClassPending();

        String token = sharedPreferences.getString("access_token", null);

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<Class> call = service.getDetailClassofClub("Bearer" + token, Integer.parseInt(idClass));

        call.enqueue(new Callback<Class>() {
            @Override
            public void onResponse(Call<Class> call, Response<Class> response) {
                if (response.isSuccessful()) {
                    Class dataClass = response.body();
                    nameClass = dataClass.getTen();
                    name = dataClass.getGiangvien();
                    txtNameClass.setText(dataClass.getTen());
                    txtTeacherClass.setText(dataClass.getGiangvien());
                    txtAddressClass.setText(dataClass.getClub());
                    txtTimeLearn.setText(dataClass.getThoigian());
                    txtInfoClass.setText(dataClass.getGiangvien());
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<Class> call, Throwable t) {
                hideLoading();

                Log.e("Fail", t.getMessage());
            }
        });
    }

    public void getListClassPending() {
        String token = sharedPreferences.getString("access_token", null);

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<List<Class>> call = service.getListClassPending("Bearer" + token);

        call.enqueue(new Callback<List<Class>>() {
            @Override
            public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                hideLoading();

                if (response.isSuccessful()) {
                    listClassPending = response.body();
                    if (!listClassPending.isEmpty()) {
                        for (Class clb : listClassPending) {
                            if (String.valueOf(clb.getId_class()).equals(idClass)) {
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
            public void onFailure(Call<List<Class>> call, Throwable t) {
                hideLoading();

                Log.e("Fail", t.getMessage());
            }
        });
    }

    public void setupButton() {
        String idClassShared = sharedPreferences.getString("id_class_shared", null);

        if (idClassShared == null) {
            btnJoinClassPending.setVisibility(isPending ? View.GONE : View.VISIBLE);
            btnCancelClassPending.setVisibility(isPending ? View.VISIBLE : View.GONE);
        } else if (!idClass.equals(idClassShared)) {
            btnDirectClass.setVisibility(View.VISIBLE);
        } else {
            btnLeaveClassPending.setVisibility(View.VISIBLE);
        }
    }

    public void joinClassPending() {
        btnJoinClassPending.setEnabled(false);
        Toast.makeText(DetailClassActivity.this, "Đang xử lý...", Toast.LENGTH_SHORT).show();

        String token = sharedPreferences.getString("access_token", null);

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<ReponseModel> call = service.joinClassPending("Bearer" + token, Integer.parseInt(idClass));

        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                if (response.isSuccessful()) {
                    isPending = true;
                    setupButton();
                    btnJoinClassPending.setEnabled(true);

                    Intent intent = new Intent(DetailClassActivity.this, RegisterClass.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id_class", idClass);
                    intent.putExtra("name", name);
                    intent.putExtra("nameClass", nameClass);
                    intent.putExtra("idClass", idClass);
                    intent.putExtras(bundle);
                    startActivity(intent);

                    Toast.makeText(DetailClassActivity.this, "Đã gửi yêu cầu tham gia lớp học", Toast.LENGTH_SHORT).show();
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

    public void cancelClassPending() {
        btnCancelClassPending.setEnabled(false);
        Toast.makeText(DetailClassActivity.this, "Đang xử lý...", Toast.LENGTH_SHORT).show();

        String token = sharedPreferences.getString("access_token", null);

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<ReponseModel> call = service.cancelClassPending("Bearer" + token, Integer.parseInt(idClass));

        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                if (response.isSuccessful()) {
                    isPending = false;
                    setupButton();
                    btnCancelClassPending.setEnabled(true);
                    Toast.makeText(DetailClassActivity.this, "Đã hủy yêu cầu tham gia lớp học", Toast.LENGTH_SHORT).show();
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

    private void leaveClassPending() {
        btnLeaveClassPending.setEnabled(false);
        Toast.makeText(DetailClassActivity.this, "Đang xử lý...", Toast.LENGTH_SHORT).show();

        String token = sharedPreferences.getString("access_token", null);

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<ReponseModel> call = service.leaveClassPending("Bearer" + token);

        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                if (response.isSuccessful()) {
                    isPending = false;
                    setupButton();
                    btnLeaveClassPending.setEnabled(true);
                    Toast.makeText(DetailClassActivity.this, "Đã gửi yêu cầu rời câu lạc bộ", Toast.LENGTH_SHORT).show();
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

    public void directJoinedClass() {
        Intent intent = new Intent(DetailClassActivity.this, DetailClassActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id_class", sharedPreferences.getString("id_class_shared", null));
        intent.putExtras(bundle);
        startActivity(intent);
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

//    public void registerClass() {
//        Intent intent = new Intent(DetailClassActivity.this, RegisterClass.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("id_class", idClass);
//        intent.putExtra("name", name);
//        intent.putExtra("nameClass", nameClass);
//        intent.putExtra("idClass", idClass);
//        intent.putExtras(bundle);
//        startActivity(intent);
//    }
//
//    public void leaveClass() {
//        showLoading();
//
//        String token = sharedPreferences.getString("access_token", null);
//
//        ClassApiService service = ApiServiceProvider.getClassApiService();
//        Call<ReponseModel> call = service.leaveClass("Bearer" + token);
//
//        call.enqueue(new Callback<ReponseModel>() {
//            @Override
//            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
//                hideLoading();
//
//                if (response.isSuccessful()) {
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("id_class_shared", null);
//                    editor.apply();
//
//                    btnCancelClassPending.setVisibility(View.GONE);
//                    btnJoinClassPending.setVisibility(View.VISIBLE);
//
//                    Toast.makeText(DetailClassActivity.this, "Rời lớp học thành công", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ReponseModel> call, Throwable t) {
//                hideLoading();
//
//                Toast.makeText(DetailClassActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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