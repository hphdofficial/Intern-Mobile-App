package com.android.mobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.models.PaymentRequest;
import com.android.mobile.models.PaymentResponse;
import com.android.mobile.network.ApiClient;
import com.android.mobile.services.VNPayRequest;
import com.android.mobile.services.VNPaySDK;
import com.android.mobile.services.VietQRService;
import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PaymentQR extends BaseActivity {


    private ImageView imageViewQRCode;
    private Button buttonGenerateQRCode;
    private TextView author;
    private TextView bank;
    private Float total = 0f;
    private String textPayment;
    private BlankFragment loadingFragment;
    private VietQRService vietQRService;
    private String baseUrl = "https://api.vietqr.vn/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_qr);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });





        imageViewQRCode = findViewById(R.id.imageViewQRCode);
        buttonGenerateQRCode = findViewById(R.id.buttonGenerateQRCode);
        author = findViewById(R.id.author);
        bank = findViewById(R.id.bank);
        bank.setText(bank.getText() + "Techcombank");
        author.setText(author.getText() + "Huỳnh Hữu Lợi");
        buttonGenerateQRCode.setVisibility(View.GONE);

        buttonGenerateQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MenuActivity.class));
            }
        });

   /*     Retrofit retrofit = ApiClient.getClient(baseUrl);
        vietQRService = retrofit.create(VietQRService.class);*/

        // Tạo yêu cầu thanh toán khi Activity được tạo
       // createPaymentRequest();
        displayQRCode("https://api.vietqr.io/image/970425-0937759311-cG4PADy.jpg&amount=" + total+

                "&addInfo=" + textPayment);


    }
    private void createPaymentRequest() {
        String amount = "10000"; // Số tiền thanh toán
        String orderId = "1"; // Mã đơn hàng của bạn
        String orderInfo = "Thanh toán đơn hàng 1";
        String ipAddr = getDeviceIpAddress();

        PaymentRequest paymentRequest = new PaymentRequest(amount, orderId, orderInfo, ipAddr);

        Call<PaymentResponse> call = vietQRService.createPayment(paymentRequest);
        call.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                if (response.isSuccessful()) {
                    PaymentResponse paymentResponse = response.body();
                    if (paymentResponse != null) {
                        displayQRCode(paymentResponse.getQrCodeUrl());
                    }
                } else {
                    Log.e("VietQR", "Error: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                Log.e("VietQR", "Failure: " + t.getMessage());
            }
        });
    }
    private void generateQRCode(String content) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 300, 300);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
        Bitmap bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.RGB_565);

        for (int x = 0; x < 300; x++) {
            for (int y = 0; y < 300; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
            }
        }

        imageViewQRCode.setImageBitmap(bitmap);

    }
    private String getDeviceIpAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : interfaces) {
                List<InetAddress> addresses = Collections.list(networkInterface.getInetAddresses());
                for (InetAddress address : addresses) {
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "127.0.0.1"; // Trả về IP mặc định nếu không thể lấy địa chỉ IP
    }

    public void displayQRCode(String qrCodeUrl) {
        ImageView qrCodeImageView = findViewById(R.id.imageViewQRCode);
        Glide.with(this)
                .load(qrCodeUrl)
                .into(qrCodeImageView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPaymentStatus("1");
    }

    public void checkPaymentStatus(String orderId) {
        VNPaySDK vnPaySDK = VNPaySDK.getInstance();

        vnPaySDK.checkPaymentStatus(orderId, new VNPaySDK.VNPayCallback() {
            @Override
            public void onSuccess(String paymentStatus) {
                // Xử lý trạng thái thanh toán
                Log.d("VNPay", "Payment Status: " + paymentStatus);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                // Xử lý lỗi
                Log.e("VNPay", "Error: " + errorMessage);
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