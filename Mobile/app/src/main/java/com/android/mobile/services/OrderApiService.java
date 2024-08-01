package com.android.mobile.services;

import com.android.mobile.models.Class;
import com.android.mobile.models.OrderModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface OrderApiService {
    @GET("/api/hoadon")
    Call<List<OrderModel>> getHistoryOrder(
            @Query("member_id") int member_id
    );
}