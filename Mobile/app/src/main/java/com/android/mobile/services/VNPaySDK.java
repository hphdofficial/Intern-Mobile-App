package com.android.mobile.services;

import android.content.Context;

public class VNPaySDK {
    private static VNPaySDK instance;
    private Context context;
    private String merchantId;
    private String secretKey;
    private String terminalId;

    private VNPaySDK() {
    }

    public static VNPaySDK getInstance() {
        if (instance == null) {
            instance = new VNPaySDK();
        }
        return instance;
    }

    public void init(Context context, String merchantId, String secretKey, String terminalId) {
        this.context = context;
        this.merchantId = merchantId;
        this.secretKey = secretKey;
        this.terminalId = terminalId;
    }

    public void createPayment(VNPayRequest request, VNPayCallback callback) {
        // Giả lập việc tạo mã QR và trả về URL mã QR
        String qrCodeUrl = " https://sandbox.vnpayment.vn/paymentv2/vpcpay.html" + request.getOrderId();
        callback.onSuccess(qrCodeUrl);
    }

    public void checkPaymentStatus(String orderId, VNPayCallback callback) {
        // Giả lập việc kiểm tra trạng thái thanh toán và trả về kết quả
        String paymentStatus = "SUCCESS"; // hoặc "PENDING", "FAILED", v.v.
        callback.onSuccess(paymentStatus);
    }

    public interface VNPayCallback {
        void onSuccess(String result);
        void onFailure(String errorCode, String errorMessage);
    }
}

