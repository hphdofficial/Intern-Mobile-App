package com.android.mobile.services;

import com.android.mobile.models.ProductModel;
import com.android.mobile.models.TheoryModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TheoryApiService {
    @GET("api/lythuyet")
    Call<List<TheoryModel>> getMartialArtsTheory();

    @GET("api/lythuyet/{id}")
    Call<TheoryModel> getMartialArtsTheoryDetail(@Path("id") int id);

    @GET("api/lythuyet/search/{tenvi}")
    Call<List<TheoryModel>> searchMartialArtsTheory(@Path("tenvi") String tenvi);
}
