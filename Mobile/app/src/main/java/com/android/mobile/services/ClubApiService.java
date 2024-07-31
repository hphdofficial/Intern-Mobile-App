package com.android.mobile.services;

import com.android.mobile.models.Club;
import com.android.mobile.models.ReponseModel;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ClubApiService {
    @GET("/api/map1")
    Call<JsonObject> getListClubMap1(
            @Query("id_country") int countryId
    );

    @GET("/api/map2")
    Call<JsonObject> getListClubMap2(
            @Query("id_country") int countryId,
            @Query("id_city") int cityId
    );

    @GET("/api/map3")
    Call<JsonObject> getListClubMap3(
            @Query("id_country") int countryId,
            @Query("id_city") int cityId,
            @Query("address") int address
    );

    @GET("/api/clubs/getdetail")
    Call<Club> getDetailClub(@Query("id_club") int clubId);

    @PUT("/api/clubs/joinclub")
    Call<ReponseModel> joinClub(
            @Header("Authorization") String token,
            @Body Club club
    );
}