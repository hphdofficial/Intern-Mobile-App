package com.android.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.adapter.ProductAdapter;
import com.android.mobile.models.Product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Purchase extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter itemAdapter;
    private List<Product> itemList;
    private Spinner voucherSpinner;
    private Spinner paymentSpinner;
    private List<String> voucherList;
    private List<String > paymentList;
    private Button btn_payment;
    private int positionVoucher;
    private TextView sum_money;
    private TextView discount;
    private TextView transport;
    private TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_purchase);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_payment = findViewById(R.id.btn_payment);
        sum_money = findViewById(R.id.sum_money);
        discount = findViewById(R.id.discount);
        transport = findViewById(R.id.transport);
        total = findViewById(R.id.total);



        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

// Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();


        // tạo recyclerview
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // tạo voucher
        CreateVoucher();
        CreatePayment();

    }

    public void Summoney(){
        float sum = 0;
        for (Product value : itemList){
            sum += value.getPrice();
        }


        sum_money.setText(formatMoney(sum)+"đ");
    }
    public void DiscountMoney(){
        // lấy id voucher check voucher sao đó ghi tiền giảm giá vô
        discount.setText(formatMoney(10000)+"đ");
    }
    public String formatMoney(float money){
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedAmount = numberFormat.format(money);
        return  formattedAmount;
    }
    public void Transport(){
        //kiểm tra theo gg map lấy kích thước đường đi quy ra tiền 1km = 10k


        transport.setText(formatMoney(4000)+"đ");
    }
    public void TotalMoney(){
        float sum = 0;
        for (Product value : itemList){
            sum += value.getPrice();
        }
        float dis = Float.parseFloat(discount.getText().toString().replace("đ","").replace(".",""));

        float tran = Float.parseFloat(transport.getText().toString().replace("đ","").replace(".",""));

        float to = sum - dis + tran;

        total.setText(formatMoney(to)+"đ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        CreateItem();
      //  EventVoucher();
        EventPayment();
        Summoney();
        DiscountMoney();
        Transport();
        TotalMoney();

    }
    public void EventPayment(){
        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PaymentQR.class);
                startActivity(intent);
            }
        });
    }

    public  void CreateItem(){
        itemList = new ArrayList<>();
        Product p1 = new Product("Sản phẩm 1",10000f,"Nhà cung cấp: không xác định","Mặt hàng chưa rõ ",1,"null");
        Product p2 = new Product("Sản phẩm 2",10000f,"Nhà cung cấp: không xác định","Mặt hàng chưa rõ ",2,"null");
        Product p3 = new Product("Sản phẩm 3",10000f,"Nhà cung cấp: không xác định","Mặt hàng chưa rõ ",3,"null");
        Product p4 = new Product("Sản phẩm 4",10000f,"Nhà cung cấp: không xác định","Mặt hàng chưa rõ ",4,"null");

        itemList.add(p1);
        itemList.add(p2);
        itemList.add(p3);
        itemList.add(p4);

        itemAdapter = new ProductAdapter(itemList);
        recyclerView.setAdapter(itemAdapter);

    }
    public void CreatePayment(){
        paymentSpinner = findViewById(R.id.paymentww);
        paymentList = new ArrayList<>();
        // Thêm dữ liệu mẫu vào danh sách voucher
        paymentList.add("Thanh toán trực tiếp");
        paymentList.add("Thanh toán Momo");
        paymentList.add("Thay toán ...");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.layout_textviewsize, paymentList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentSpinner.setAdapter(adapter);
    }
    public void CreateVoucher(){
        voucherSpinner = findViewById(R.id.voucherSpinner);
        voucherList = new ArrayList<>();
        // Thêm dữ liệu mẫu vào danh sách voucher
        voucherList.add("Voucher 1");
        voucherList.add("Voucher 2");
        voucherList.add("Voucher 3");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.layout_textviewsize, voucherList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        voucherSpinner.setAdapter(adapter);
    }
    public void EventVoucher(){
        voucherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedVoucher = voucherList.get(position);

                positionVoucher = position;
                Toast.makeText(getApplicationContext(), "Selected: " + selectedVoucher, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không có gì xảy ra khi không chọn gì
            }
        });
    }
}