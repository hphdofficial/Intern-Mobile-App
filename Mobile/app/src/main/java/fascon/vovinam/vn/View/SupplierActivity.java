package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.ViewModel.SupplierAdapter;
import fascon.vovinam.vn.Model.SupplierModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.SupplierApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplierActivity extends BaseActivity implements SupplierAdapter.OnSupplierClickListener {
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
    private RecyclerView recyclerView;
    private SupplierAdapter adapter;
    private List<SupplierModel> supplierList;
    private BlankFragment loadingFragment;
    private String languageS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);

        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Danh sách nhà cung cấp");
        myContentE.apply();
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language",null);
        if(languageS!= null){
            if(languageS.contains("en")){
                myContentE.putString("title", "List Supplier");
                myContentE.apply();
            }
        }
        // chèn fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        supplierList = new ArrayList<>();
        adapter = new SupplierAdapter(supplierList, this);
        recyclerView.setAdapter(adapter);

        fetchSuppliers();
        NamePage();
        title = findViewById(R.id.title);
        if(languageS != null){
            if(languageS.contains("en")){
                title.setText("List of our suppliers");
            }
        }
    }
    private TextView title;

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



    private void fetchSuppliers() {
        showLoading();
        SupplierApiService apiService = ApiServiceProvider.getSupplierApiService();
        Call<List<SupplierModel>> call = apiService.getSuppliers();
        call.enqueue(new Callback<List<SupplierModel>>() {
            @Override
            public void onResponse(Call<List<SupplierModel>> call, Response<List<SupplierModel>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    supplierList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SupplierActivity.this, "Không thể lấy danh sách nhà cung cấp", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SupplierModel>> call, Throwable t) {
                hideLoading();
                Toast.makeText(SupplierActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSupplierClick(SupplierModel supplier) {
        Intent intent = new Intent(SupplierActivity.this, SupplierInfoActivity.class);
        intent.putExtra("SupplierID", supplier.getSupplierID());
        intent.putExtra("SupplierName", supplier.getSupplierName());
        intent.putExtra("Address", supplier.getAddress());
        intent.putExtra("Phone", supplier.getPhone());
        intent.putExtra("Email", supplier.getEmail());
        startActivity(intent);
    }

    public void NamePage(){
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Nhà cung cấp");
        myContentE.apply();
    }
}
