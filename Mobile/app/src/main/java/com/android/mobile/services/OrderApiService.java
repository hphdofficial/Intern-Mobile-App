package com.android.mobile.services;

import com.android.mobile.models.OrderModel;
import com.android.mobile.models.ProductModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface OrderApiService {
    @GET("/api/orders/All")
    Call<List<OrderModel>> getHistoryOrder(
            @Header("Authorization") String token
    );

    @GET("/api/chitiethoadon")
    Call<List<ProductModel>> getListProductOrder(
            @Query("id_order") int orderId
    );
}