package com.android.mobile.services;

import com.android.mobile.models.PaymentRequest;
import com.android.mobile.models.PaymentResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface VietQRService {

    @Headers("Content-Type: application/json")
    @POST("v2/paymentRequests")
    Call<PaymentResponse> createPayment(@Body PaymentRequest paymentRequest);
}
