package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.ViewModel.MyClassAdapter;
import fascon.vovinam.vn.Model.ClassModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.CheckinApiService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyClassActivity extends BaseActivity {

    private BlankFragment loadingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_class);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Những lớp đang dạy");
        myContentE.apply();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

// Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
//        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();

        FetchClassesForTeacher();
    }

    private void FetchClassesForTeacher(){
        showLoading();
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", null);
        CheckinApiService apiService = ApiServiceProvider.getCheckinApiService();
        apiService.getTeacherClasses("Bearer "+token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject jsonObject = response.body();
                    Gson gson = new Gson();
                    Type classListType = new TypeToken<List<ClassModel>>() {}.getType();
                    List<ClassModel> classes = gson.fromJson(jsonObject.get("data"), classListType);
                    if(classes == null){
                        Toast.makeText(MyClassActivity.this, "Không có lớp học vào thời điểm hiện tại", Toast.LENGTH_SHORT).show();
                        hideLoading();
                    }else{
                        for (ClassModel classSample : classes){
                            Log.e("PostData", "Success: " + classSample.getTen());
                        }
                        MyClassAdapter classAdapter = new MyClassAdapter(getApplicationContext(), classes);
                        RecyclerView recyclerView = findViewById(R.id.recycler_class);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(classAdapter);

                        hideLoading();
                    }


                }else {
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());
                    if(response.code() == 403){
                        hideLoading();
                        Toast.makeText(MyClassActivity.this, "Bạn không phải giảng viên." +
                                " Đang chuyển qua lớp học cho học viên", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), activity_member_checkin.class));

                    }else{
                        Toast.makeText(MyClassActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        hideLoading();
                    }


                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                hideLoading();
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + throwable.getMessage());
                Toast.makeText(MyClassActivity.this, "Lỗi lấy dữ liệu lớp học", Toast.LENGTH_SHORT).show();
            }
        });
    }

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