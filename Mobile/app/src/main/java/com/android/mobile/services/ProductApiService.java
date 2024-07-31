package com.android.mobile.services;

import com.android.mobile.models.ProductModel;
import com.android.mobile.models.ReviewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductApiService {

    @GET("api/products")
    Call<List<ProductModel>> getAllProducts();

    @GET("api/reviews/{ProductID}")
    Call<List<ReviewModel>> getProductReviews(@Path("ProductID") int productId);

    @POST("api/reviews")
    Call<Void> addReview(@Header("Authorization") String token, @Body ReviewModel review);
}
