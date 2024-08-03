package com.android.mobile.services;

import com.android.mobile.models.Club;
import com.android.mobile.models.ReponseModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CartApiService {
    @GET("/api/cart")
    Call<JsonObject> getCart(
            @Header("Authorization") String token,
            @Query("member_id") int member_id
    );

    @GET("/api/cart/total-price")
    Call<JsonObject> getTotalPrice(
            @Query("member_id") int member_id
    );
}