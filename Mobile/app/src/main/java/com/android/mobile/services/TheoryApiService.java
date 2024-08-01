package com.android.mobile.services;

import com.android.mobile.models.ProductModel;
import com.android.mobile.models.TheoryModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface TheoryApiService {
    @GET("api/lythuyet")
    Call<List<TheoryModel>> getMartialArtsTheory();

    @GET("api/lythuyet/{id}")
    Call<TheoryModel> getMartialArtsTheoryDetail(@Body TheoryModel request);

    @GET("api/lythuyet/search/{tenvi}")
    Call<TheoryModel> searchMartialArtsTheory(@Body TheoryModel request);
}
