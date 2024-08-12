package com.android.mobile.services;

import com.android.mobile.models.OrderListModel;
import com.android.mobile.models.OrderModel;
import com.android.mobile.models.ProductModel;
import com.android.mobile.models.ReponseModel;

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

    @GET("api/orders/All")
    Call<List<OrderListModel>> getListOrder(
            @Header("Authorization") String token
    );

    @GET("/update_delete_delivery")
    Call<ReponseModel> cancelOrder(
            @Query("id") String txnRef,
            @Query("action") String action
    );
}