package com.android.mobile.services;

import com.android.mobile.models.CatagoryModel;
import com.android.mobile.models.ProductModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface CatagoryApiService {
    @GET("api/categories")
    Call<CatagoryModel> index (@Body CatagoryModel request);
}
