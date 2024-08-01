package com.android.mobile.services;

import com.android.mobile.models.Club;
import com.android.mobile.models.ReponseModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;

public interface CartApiService {
    @GET("/api/cart")
    Call<JsonObject> getCart(
            @Header("Authorization") String token
    );
}