package com.android.mobile.services;

import com.android.mobile.models.Belt;
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

    @GET("api/all-belts")
    Call<List<Belt>> getAllBelt();

    @GET("api/martial-arts-theory-by-club-and-belt/{id_club}/{beltId}")
    Call<List<TheoryModel>> getMartialArtsTheoryByClubByBelt(@Path("id_club") int id_club,
                                                                @Path("beltId") int beltId);
}
