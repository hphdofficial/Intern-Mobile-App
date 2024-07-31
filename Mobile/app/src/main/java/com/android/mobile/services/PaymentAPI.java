package com.android.mobile.services;

import com.android.mobile.models.CartModel;
import com.android.mobile.models.CartResponse;
import com.android.mobile.models.ProfileModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface PaymentAPI {

    @GET("api/cart")
    Call<CartResponse> getCart(@Query("member_id") String member_id);


    @GET("paymentss/getlink")
    Call<ResponseBody> getLink(@Query("member_id") String member_id);
}
