package com.android.mobile.services;

import com.android.mobile.models.ProductModel;
import com.android.mobile.models.ReviewModel;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductApiService {

    @GET("api/products")
    Call<List<ProductModel>> getProducts();

    @GET("api/products/{id}")
    Call<ProductModel> show(@Path("id") int id);

    @GET("api/reviews/{ProductID}")
    Call<List<ReviewModel>> getProductReviews(@Path("ProductID") int productId);

    @GET("api/products/category/{Categoryname}")
    Call<List<ProductModel>> getByCategory(@Path("Categoryname") String Categoryname);

    @POST("api/reviews")
    Call<Void> addReview(@Header("Authorization") String token, @Body ReviewModel review);

    @GET("api/products/search/{name}")
    Call<List<ProductModel>> search(@Path("name") String name);

    @POST("api/products/filter-by-supplier")
    Call<List<ProductModel>> getFilterBySupplier(@Query("SupplierID") int SupplierID);

    @POST("api/products/filter-by-price")
    Call<List<ProductModel>> getFilterByPrice(@Query("min_price") int min_price,
                                          @Query("max_price") int max_price);

    @POST("api/cart")
    Call<Void> addToCart(@Header("Authorization") String token,
                         @Query("member_id") int id,
                         @Query("product_id") int ProductID,
                         @Query("quantity") int quantity);
}
