package com.android.mobile.services;

import com.android.mobile.models.ApproveModel;
import com.android.mobile.models.Class;
import com.android.mobile.models.ReponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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

    @POST("/api/leave-class")
    Call<ReponseModel> leaveClass(
            @Header("Authorization") String token
    );

    @GET("/api/classes/search_name")
    Call<List<Class>> searchClass(
            @Query("keyword") String name
    );

    @GET("/api/coach/classes/getMemberpending")
    Call<List<ApproveModel>> getListJoinClassPending(
            @Header("Authorization") String token
    );

    @GET("/api/coach/leave-class-requests")
    Call<List<ApproveModel>> getListLeaveClassPending(
            @Header("Authorization") String token
    );

    @POST("/api/coach/classes/approve-join")
    Call<ReponseModel> approveJoinClass(
            @Header("Authorization") String token,
            @Query("id_member") int memberId,
            @Query("id_class") int classId
    );

    @POST("/api/coach/approve-leave-class-request")
    Call<ReponseModel> approveLeaveClass(
            @Header("Authorization") String token,
            @Query("id_member") int memberId,
            @Query("id_class") int classId
    );
}