package com.android.mobile.services;

import com.android.mobile.models.Class;
import com.android.mobile.models.Club;
import com.android.mobile.models.ReponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ClassApiService {
    @GET("/api/clubs/classes/getall")
    Call<List<Class>> getClassofClub(
            @Header("Authorization") String token
    );

    @GET("/api/clubs/classes/getdetail")
    Call<Class> getDetailClassofClub(
            @Header("Authorization") String token,
            @Query("id_class") int classId
    );

    @GET("/api/user/classes")
    Call<List<Class>> getDetailClassMember(
            @Header("Authorization") String token
    );

    @POST("/api/roi-khoi-lop-hoc")
    Call<ReponseModel> leaveClass(
            @Header("Authorization") String token,
            @Body Class classs
    );

    @GET("/api/classes/search_name")
    Call<List<Class>> searchClass(
            @Query("keyword") String name
    );
}