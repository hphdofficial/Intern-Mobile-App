package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import fascon.vovinam.vn.ViewModel.ProductAdapter;
import fascon.vovinam.vn.Model.Product;
import fascon.vovinam.vn.Model.ProductModel;
import fascon.vovinam.vn.Model.StatusRegister;
import fascon.vovinam.vn.Model.addressModel;
import fascon.vovinam.vn.Model.network.APIServicePayment;
import fascon.vovinam.vn.Model.services.PaymentAPI;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Purchase extends BaseActivity {

    private RecyclerView recyclerView;
    private ProductAdapter itemAdapter;
    private List<Product> itemList;
    private Spinner voucherSpinner;
    private Spinner paymentSpinner;
    private List<String> voucherList;
    private List<String> paymentList;
    private SharedPreferences sharedPreferences;
    private Button btn_payment;
    private int positionVoucher;
    private String link = null;
    private TextView sum_money;
    private TextView discount;
    private TextView transport;
    private TextView total;
    private String textPayment;
    private TextView address;
    private List<ProductModel> productList;
    private BlankFragment loadingFragment;
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
        setContentView(R.layout.activity_purchase);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        showLoading();
        btn_payment = findViewById(R.id.btn_payment);
        sum_money = findViewById(R.id.sum_money);
        discount = findViewById(R.id.discount);
        transport = findViewById(R.id.transport);
        total = findViewById(R.id.total);
        layout1 = findViewById(R.id.layout1);
        address = findViewById(R.id.address);

        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Thanh toán");
        myContentE.apply();
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language",null);
        if(languageS!= null){
            if(languageS.contains("en")){
                myContentE.putString("title", "Payment");
                myContentE.apply();
            }
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

// Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
//        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();

        Intent intent = getIntent();
        productList = intent.getParcelableArrayListExtra("product_list");

        // tạo recyclerview
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // tạo voucher
        CreateVoucher();
        CreatePayment();

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddressP.class));
            }
        });

        // địa chỉ
        listPa = findViewById(R.id.listPa);
        addessP = findViewById(R.id.addessP);
        chooseV = findViewById(R.id.chooseV);
        pp = findViewById(R.id.pp);
        detail = findViewById(R.id.detail);
        suma = findViewById(R.id.suma);
        sumD = findViewById(R.id.sumD);
        sumT = findViewById(R.id.sumT);
        sumPa = findViewById(R.id.sumPa);

        if(languageS!= null ){
            if(languageS.contains("en")){
                addessP.setText("Delivery address");
                listPa.setText("List product");
                chooseV.setText("Choose code voucher");
                pp.setText("Payment method");
                detail.setText("Payment details");
                suma.setText("Total cost of product");
                sumD.setText("Money discount");
                sumT.setText("Money transport");
                sumPa.setText("Sum payment");
                btn_payment.setText("Payment");
            }
        }
    }
private TextView addessP;
    private TextView listPa;
    private TextView chooseV;
    private TextView pp;
    private TextView detail;
    private TextView suma;
    private TextView sumD;
    private TextView sumT;
    private TextView sumPa;

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

    public void Summoney() {
        float sum = 0;
        for (ProductModel value : productList) {
            sum += Integer.parseInt(value.getUnitPrice()) * value.getQuantity();
        }


        sum_money.setText(formatMoney(sum) + " đ");
    }

    public void DiscountMoney() {
        // lấy id voucher check voucher sao đó ghi tiền giảm giá vô

        int totalDiscount = 0;

        for (ProductModel product : productList) {
            float percent = Float.parseFloat(product.getSale().toString());
            int intPercent = (int) (percent * 100);
            int unitPrice = Integer.parseInt(product.getUnitPrice());
            int discountPerProduct = (unitPrice * intPercent * product.getQuantity()) / 100;
            totalDiscount += discountPerProduct;
        }

        discount.setText(formatMoney(totalDiscount) + " đ");
    }

    public String formatMoney(float money) {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedAmount = numberFormat.format(money);
        return formattedAmount;
    }

    public void Transport() {
        //kiểm tra theo gg map lấy kích thước đường đi quy ra tiền 1km = 10k


        transport.setText(formatMoney(10000) + " đ");
    }

    public void TotalMoney() {
        float sum = 0;
//        for (Product value : itemList){
//            textPayment += value.getName() + "va";
//            sum += value.getPrice()*value.getQuantity();
//        }
        for (ProductModel value : productList) {

            if (languageS!= null){
                if(languageS.contains("en")){
                    textPayment += value.getEn() + "va";
                }else {
                    textPayment += value.getProductName() + "va";
                }
            }else {
                textPayment += value.getProductName() + "va";
            }
            sum += Integer.parseInt(value.getUnitPrice()) * value.getQuantity();
        }
        float dis = Float.parseFloat(discount.getText().toString().replace("đ", "").replace(".", ""));

        float tran = Float.parseFloat(transport.getText().toString().replace("đ", "").replace(".", ""));

        float to = sum - dis + tran;

        total.setText(formatMoney(to) + " đ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        CreateItem();
        //  EventVoucher();
        EventPayment();
        GetStatus();
        changeAddress();


    }

    public void changeAddress() {
        SharedPreferences sharedPreferences = getSharedPreferences("myAddress", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list", null);

        if (json != null) {
            Type type = new TypeToken<ArrayList<addressModel>>() {
            }.getType();
            List<addressModel> list = gson.fromJson(json, type);

            for (addressModel value : list) {
                if (value.getSelection() == 1) {
                    address.setText(value.getAddress());

                }
            }
        }
    }

    public void GetStatus() {
        if (link != null) {
            String s = link.replace("https://vovinammoi-4bedb6dd1c05.herokuapp.com/orders/show?id=", "");
            String arr[] = s.split("&");
            PaymentAPI apiService = APIServicePayment.getPaymentApiService();
            Call<StatusRegister> call = apiService.GetStatusRegister(Integer.parseInt(arr[0]));
            call.enqueue(new Callback<StatusRegister>() {
                @Override
                public void onResponse(Call<StatusRegister> call, Response<StatusRegister> response) {
                    if (response.isSuccessful()) {
                        StatusRegister status = response.body();
                        if (status.getStatus().contains("thành công")) {
                            if(languageS!= null){
                                if (languageS.contains("en")){
                                    Toast.makeText(getApplicationContext(), "Payment Ok, bill code: "+ arr[0], Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(), "Thanh toán thành công, mã hóa đơn " + arr[0], Toast.LENGTH_SHORT).show();

                                }
                            }else {
                                Toast.makeText(getApplicationContext(), "Thanh toán thành công, mã hóa đơn " + arr[0], Toast.LENGTH_SHORT).show();

                            }
                            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "Thanh toán thát bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(languageS!= null){
                        if (languageS.contains("en")){
                            Toast.makeText(getApplicationContext(), "Payment fail", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "Thanh toán thất bại ", Toast.LENGTH_SHORT).show();

                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Thanh toán thất bại ", Toast.LENGTH_SHORT).show();

                    }


                }

                @Override
                public void onFailure(Call<StatusRegister> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "error 404", Toast.LENGTH_SHORT).show();
                }
            });
            Log.e("stats", link);
        }
    }

    private LinearLayout layout1;

    public void EventPayment() {
        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentAPI apiService = APIServicePayment.getPaymentApiService();
                sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("access_token", null);
                Call<ResponseBody> call = apiService.OrderBill("Bearer " + token);


                if (productList.size() > 0) {
                    String selectedValue = paymentSpinner.getSelectedItem().toString();
                    if (selectedValue.contains("Qr")) {


                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {

                                 if(languageS!= null){
                                     if (languageS.contains("en")){
                                         Toast.makeText(getApplicationContext(), "order successfully", Toast.LENGTH_SHORT).show();
                                     }else {
                                         Toast.makeText(getApplicationContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                     }
                                 }else {
                                     Toast.makeText(getApplicationContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                 }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Đặt hàng thất bại vì k có hàng", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });

                        float sum = 0;
                        for (ProductModel value : productList) {
                            sum += Integer.parseInt(value.getUnitPrice()) * value.getQuantity();
                        }
                        float dis = Float.parseFloat(discount.getText().toString().replace("đ", "").replace(".", ""));

                        float tran = Float.parseFloat(transport.getText().toString().replace("đ", "").replace(".", ""));

                        float to = sum - dis + tran;
                        Intent inten = new Intent(getApplicationContext(), PaymentQR.class);
                        inten.putExtra("amount", to);
                        inten.putExtra("textPayment", textPayment);
                        startActivity(inten);
                    } else if (selectedValue.contains("thẻ")|| selectedValue.contains("Card"))
                        getLink();
                    else {
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {

                                    if(languageS!= null){
                                        if (languageS.contains("en")){
                                            Toast.makeText(getApplicationContext(), "order successfully", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(getApplicationContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Toast.makeText(getApplicationContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                    }                                } else {
//                                    Toast.makeText(getApplicationContext(), "Đặt hàng thất bại vì k có hàng", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
//                        Toast.makeText(getApplicationContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                        if (languageS != null) {
                            if (languageS.contains("en")) {
                                Toast.makeText(getApplicationContext(), "order successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                    }

                }
            }
        });
    }

    public void CreateItem() {

        itemAdapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(itemAdapter);
        Summoney();
        DiscountMoney();
        Transport();
        TotalMoney();
        hideLoading();

//        itemList = new ArrayList<>();
//
//        sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
//        int memberId = sharedPreferences.getInt("member_id", -1);
//
//        PaymentAPI apiService = APIServicePayment.getPaymentApiService();
//        Call<CartResponse> call = apiService.getCart(memberId+"");
//        call.enqueue(new Callback<CartResponse>() {
//            @Override
//            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
//                if(response.isSuccessful()){
//                    CartResponse cartResponse = response.body();
//                    List<CartModel> cartItems = cartResponse.getCart();
//                    Double count = 0.0;
//                    if(cartItems.size()>0){
//                        itemList.clear();
//                        for (CartModel value : cartItems){
//
//                            count+= Double.parseDouble(value.getTotalPrice());
//                            ProductModel p = value.getProduct();
//                            Product pr = new Product();
//                            pr.setName(p.getProductName());
//                            pr.setPrice(Float.parseFloat(p.getUnitPrice().toString()));
//                            pr.setQuantity(value.getQuantity());
//                            pr.setSupplier(p.getSupplierID()+"");
//                            pr.setLinkImage("null");
//                            itemList.add(pr);
//                        }
//                        itemAdapter = new ProductAdapter(itemList);
//                        recyclerView.setAdapter(itemAdapter);
//                        Summoney();
//                        DiscountMoney();
//                        Transport();
//                        TotalMoney();
//                        hideLoading();
//
//                    }
//                }else {
//                    hideLoading();
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CartResponse> call, Throwable t) {
//
//            }
//        });


    }

    public void getLink() {
        sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", null);
        int memberId = sharedPreferences.getInt("member_id", -1);
        PaymentAPI apiService = APIServicePayment.getPaymentApiService();
        Call<ResponseBody> call = apiService.getLink("Bearer " + token, memberId + "");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;

                    try {
                        link = response.body().string();
                        String url = link;

                        // Tạo một Intent với action ACTION_VIEW và URL dưới dạng Uri
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));

                        // Bắt đầu Activity với Intent
                        startActivity(intent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(languageS!= null){
                    if (languageS.contains("en")){
                        Toast.makeText(getApplicationContext(), "payment error", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Thanh toán thất bại", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Thanh toán thất bại", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void CreatePayment() {
        paymentSpinner = findViewById(R.id.paymentww);
        paymentList = new ArrayList<>();

        // Thêm dữ liệu mẫu vào danh sách voucher
        paymentList.add("Thanh toán trực tiếp");
        paymentList.add("Thanh toán thẻ");
        paymentList.add("Thanh toán Qr");
        if(languageS != null){
            if(languageS.contains("en")){
                paymentList.clear();
                paymentList.add("Direct payment");
                paymentList.add("Card payment");
                paymentList.add("Payment Qr");
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.layout_textviewsize, paymentList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentSpinner.setAdapter(adapter);
    }

    public void CreateVoucher() {
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

    public void EventVoucher() {
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