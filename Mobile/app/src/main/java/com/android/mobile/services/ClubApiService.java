package com.android.mobile.services;

import com.android.mobile.models.CityModel;
import com.android.mobile.models.Club;
import com.android.mobile.models.CountryModel;
import com.android.mobile.models.MapsResponse;
import com.android.mobile.models.ReponseModel;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ClubApiService {
    @GET("api/interpreter")
    Call<MapsResponse> getMapData(@Query("data") String data);

    @GET("/api/countries")
    Call<List<CountryModel>> getListCountry();

    @GET("/api/cities/country/{id_country}")
    Call<List<CityModel>> getListCity(
            @Path("id_country") int countryId
    );

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
            @Query("address") String address
    );

    @GET("/api/clubs/getdetail")
    Call<Club> getDetailClub(@Query("id_club") int clubId);

    @PUT("/api/clubs/joinclub")
    Call<ReponseModel> joinClub(
            @Header("Authorization") String token,
            @Body Club club
    );

    @PUT("/api/clubs/outclub")
    Call<ReponseModel> leaveClub(
            @Header("Authorization") String token
    );

    @GET("/api/auth/clubs/getdetail/member")
    Call<Club> getDetailClubMember(
            @Header("Authorization") String token
    );

    @GET("/api/clubs/search_name")
    Call<List<Club>> searchClub(
            @Query("keyword") String name
    );

    @GET("/api/clubs/pending")
    Call<List<Club>> getListClubPending(
            @Header("Authorization") String token
    );

    @GET("/api/clubs/join-clubpending")
    Call<ReponseModel> joinClubPending(
            @Header("Authorization") String token,
            @Query("id_club") int clubId
    );

    @POST("/api/clubs/out-pending")
    Call<ReponseModel> cancelClubPending(
            @Header("Authorization") String token,
            @Query("id_club") int clubId
    );

    @POST("/api/coach/approve-join")
    Call<ReponseModel> approveJoinClub(
            @Header("Authorization") String token,
            @Query("id_member") int memberId,
            @Query("id_club") int clubId
    );

    @GET("/api/coach/join-memberpending")
    Call<List<Club>> getListMemberPending(
            @Header("Authorization") String token
    );
}