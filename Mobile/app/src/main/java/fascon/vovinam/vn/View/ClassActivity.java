package fascon.vovinam.vn.View;

import fascon.vovinam.vn.Model.ReponseModel;
import fascon.vovinam.vn.Model.services.ClubApiService;
import fascon.vovinam.vn.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
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
import fascon.vovinam.vn.ViewModel.ClassAdapter;
import fascon.vovinam.vn.Model.Class;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.ClassApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private ClassAdapter adapter;
    private List<Class> classList = new ArrayList<>();
    private SearchView searchView;
    private BlankFragment loadingFragment;
    private String idClub = null;
    private String languageS;
    private TextView text;
    public void onMenuItemClick(View view) {
        text = findViewById(R.id.languageText);
        String language = text.getText()+"";
        if(view.getId() == R.id.btn_change){
            SharedPreferences sga = getSharedPreferences("login_prefs", MODE_PRIVATE);
            SharedPreferences.Editor edit =  sga.edit();

            if(language.contains("VN")){
                edit.putString("language","en");
                text.setText("ENG");
            }else {
                edit.putString("language","vn");
                text.setText("VN");
            }
            edit.apply();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language",null);
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Danh sách lớp học");
        myContentE.apply();
        if(languageS!= null){
            if(languageS.contains("en")){
                myContentE.putString("title", "List Class");
                myContentE.apply();
            }
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new titleFragment());
        fragmentTransaction.commit();

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                idClub = bundle.getString("id_club");
            }
        }

        adapter = new ClassAdapter(this, classList, idClub);
        recyclerView = findViewById(R.id.recycler_class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        searchView = findViewById(R.id.search_class);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchClass(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                searchClass(newText);
                return false;
            }
        });
        showLoading();

//        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
//        String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vdm92aW5hbW1vaS00YmVkYjZkZDFjMDUuaGVyb2t1YXBwLmNvbS9hcGkvYXV0aC9sb2dpbiIsImlhdCI6MTcyMzgxMDY4NywiZXhwIjoxNzIzODk3MDg3LCJuYmYiOjE3MjM4MTA2ODcsImp0aSI6IktldnN6dktQT1BCS0ttVU8iLCJzdWIiOiIyOTMiLCJwcnYiOiIxMDY2NmI2ZDAzNThiMTA4YmY2MzIyYTg1OWJkZjk0MmFmYjg4ZjAyIiwibWVtYmVyX2lkIjoyOTMsInJvbGUiOjF9.4meCsHv5Ma7wVdr7lpOGQuAmpBuSHJNAiejsIaB9SjQ";
//        String idMember = sharedPreferences.getString("member_id", null);
//
//        ClubApiService service = ApiServiceProvider.getClubApiService();
//        Call<ReponseModel> call = service.approveJoinClub("Bearer " + accessToken, Integer.parseInt(idMember), Integer.parseInt(idClub));
//        call.enqueue(new Callback<ReponseModel>() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
//            }
//
//            @Override
//            public void onFailure(Call<ReponseModel> call, Throwable t) {
//            }
//        });

        getListClass();
        if(languageS!= null){
            if(languageS.contains("en")){
                searchView.setQueryHint("Search...");
            }
        }
    }

    private void getListClass(){
        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<List<Class>> call = service.getListClassofClub(Integer.parseInt(idClub));

        call.enqueue(new Callback<List<Class>>() {
            @Override
            public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                hideLoading();

                if (response.isSuccessful()) {
                    List<Class> classes = response.body();
                    adapter.setData(classes);
                    if (classes.isEmpty()) {
                        Toast.makeText(ClassActivity.this, "Không có lớp học nào đang mở", Toast.LENGTH_SHORT).show();
                    }
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


//    private void getListClass(){
//        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
//        String token = sharedPreferences.getString("access_token", null);
//
//        ClassApiService service = ApiServiceProvider.getClassApiService();
//        Call<List<Class>> call = service.getClassofClub("Bearer" + token);
//
//        call.enqueue(new Callback<List<Class>>() {
//            @Override
//            public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
//                hideLoading();
//
//                if (response.isSuccessful()) {
//                    List<Class> classes = response.body();
//                    adapter.setData(classes);
//                    if (!classes.isEmpty()) {
//                        Toast.makeText(ClassActivity.this, "Tải dữ liệu thành công", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(ClassActivity.this, "Không có lớp học nào thuộc câu lạc bộ bạn tham gia", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(ClassActivity.this, "Không có lớp học nào thuộc câu lạc bộ bạn tham gia", Toast.LENGTH_SHORT).show();
//                    System.err.println("Response error: " + response.errorBody());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Class>> call, Throwable t) {
//                hideLoading();
//
//                t.printStackTrace();
//            }
//        });
//    }

    public void searchClass(String text) {
        showLoading();

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<List<Class>> call = service.searchClass(text);

        call.enqueue(new Callback<List<Class>>() {
            @Override
            public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                hideLoading();

                if (response.isSuccessful()) {
                    List<Class> classes = response.body();
                    adapter.setData(classes);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(ClassActivity.this, "Tìm kiếm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Class>> call, Throwable t) {
                hideLoading();

                t.printStackTrace();
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