package com.android.mobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class PaymentQR extends AppCompatActivity {


    private ImageView imageViewQRCode;
    private Button buttonGenerateQRCode;
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

        buttonGenerateQRCode.setOnClickListener(v -> generateQRCode("Hello, QR Code!"));
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            // Lấy dữ liệu từ Bundle
            String title = extras.getString("title");
            if(title.contains("registerclass")){
                Toast.makeText(getApplicationContext(),extras.getDouble("money")+"",Toast.LENGTH_SHORT).show();
            }
        }

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
}