package com.android.mobile.services;

import com.android.mobile.models.CartItem;
import com.android.mobile.models.Club;
import com.android.mobile.models.ReponseModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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

    @POST("/api/cart/remove")
    Call<JsonObject> removeProduct(
            @Body CartItem cartItem
    );

    @POST("/api/cart/increase-quantity")
    Call<JsonObject> increaseQuantity(
            @Body CartItem cartItem
    );

    @POST("/api/cart/decrease-quantity")
    Call<JsonObject> decreaseQuantity(
            @Body CartItem cartItem
    );
}