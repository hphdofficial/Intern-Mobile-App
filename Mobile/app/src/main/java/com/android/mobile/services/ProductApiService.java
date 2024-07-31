package com.android.mobile.services;

import com.android.mobile.models.ProductModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductApiService {
    @GET("api/products")
    Call<ProductModel[]> getProducts();

    @GET("api/products/{id}")
    Call<ProductModel> show(@Path("id") int id);

    @GET("api/products/category/{CategoryID}")
    Call<ProductModel[]> getByCategory(@Path("CategoryID") String CategoryID);

    @GET("api/products/search/{name}")
    Call<ProductModel[]> search(@Path("name") String name);

    @GET("api/products/filter")
    Call<ProductModel[]> getFilter(@Query("SupplierID") int SupplierID);
}
