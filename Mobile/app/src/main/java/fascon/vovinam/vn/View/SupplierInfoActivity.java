package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.Model.SupplierModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.SupplierApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplierInfoActivity extends BaseActivity {
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
    private int supplierID;
    private BlankFragment loadingFragment;
    private String languageS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Thông tin nhà cung cấp");
        myContentE.apply();
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language",null);
        if(languageS!= null){
            if(languageS.contains("en")){
                myContentE.putString("title", "Supplier Information");
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
//        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();

        Intent intent = getIntent();
        supplierID = intent.getIntExtra("SupplierID", -1);

        fetchSupplierInfo(supplierID);
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


    private void fetchSupplierInfo(int supplierID) {
        showLoading();
        SupplierApiService apiService = ApiServiceProvider.getSupplierApiService();
        apiService.getSupplier(supplierID).enqueue(new Callback<SupplierModel>() {
            @Override
            public void onResponse(Call<SupplierModel> call, Response<SupplierModel> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    SupplierModel supplier = response.body();
                    displaySupplierInfo(supplier);
                } else {
                    // Handle error
                    Toast.makeText(SupplierInfoActivity.this, "Có lỗi xảy ra, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SupplierModel> call, Throwable t) {
                hideLoading();
                // Handle failure
                Toast.makeText(SupplierInfoActivity.this, "Không thể kết nối, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void displaySupplierInfo(SupplierModel supplier) {
        TextView supplierNameTextView = findViewById(R.id.supplierName);
        TextView addressTextView = findViewById(R.id.supplierAddress);
        TextView phoneTextView = findViewById(R.id.supplierPhone);
        TextView emailTextView = findViewById(R.id.supplierEmail);
        Button btnViewAllProducts = findViewById(R.id.viewAllProductsButton);

        // Kiểm tra ngôn ngữ và hiển thị thông tin nhà cung cấp tương ứng
        if (languageS != null && languageS.contains("en")) {
            supplierNameTextView.setText(supplier.getTenen()); // Hiển thị tên nhà cung cấp bằng tiếng Anh
            addressTextView.setText(supplier.getDiachien());   // Hiển thị địa chỉ bằng tiếng Anh
            btnViewAllProducts.setText("Show all products from supplier");
        } else {
            supplierNameTextView.setText(supplier.getSupplierName()); // Hiển thị tên nhà cung cấp mặc định (tiếng Việt)
            addressTextView.setText(supplier.getAddress());           // Hiển thị địa chỉ mặc định (tiếng Việt)
        }

        phoneTextView.setText(supplier.getPhone()); // Số điện thoại hiển thị không thay đổi
        emailTextView.setText(supplier.getEmail()); // Email hiển thị không thay đổi

        btnViewAllProducts.setOnClickListener(view -> {
            Intent productIntent = new Intent(SupplierInfoActivity.this, SupplierProductActivity.class);
            productIntent.putExtra("SupplierID", supplierID); // Chuyển SupplierID sang Activity tiếp theo
            startActivity(productIntent);
        });
    }

}
