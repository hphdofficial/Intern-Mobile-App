package com.android.mobile.services;

import com.android.mobile.models.ProductModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface ProductApiService {
    @GET("api/products")
    Call<ProductModel[]> getProducts();

    @GET("api/products/{id}")
    Call<ProductModel> show(@Body ProductModel request);

    @GET("api/products/category/{CategoryID}")
    Call<ProductModel> getByCategory(@Body ProductModel request);

    @GET("api/products/search/{name}")
    Call<ProductModel> search(@Body ProductModel request);
}
