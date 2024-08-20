package fascon.vovinam.vn.Model.services;

import com.google.gson.JsonObject;

import fascon.vovinam.vn.Model.ApproveModel;
import fascon.vovinam.vn.Model.Class;
import fascon.vovinam.vn.Model.ReponseModel;

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
    Call<Class> getDetailClass(
            @Header("Authorization") String token,
            @Query("id_class") int classId
    );

    @GET("/api/classes/by-id")
    Call<Class> getDetailClassofClub(
            @Query("id_class") int classId,
            @Query("lang") String lang
    );

    @GET("/api/classes/by-club")
    Call<List<Class>> getListClassofClub(
            @Query("id_club") int clubId,
            @Query("lang") String lang
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

    @GET("/api/clubs/classes/pending")
    Call<List<Class>> getListClassPending(
            @Header("Authorization") String token
    );

    @POST("/api/clubs/classes/join-classpending")
    Call<ReponseModel> joinClassPending(
            @Header("Authorization") String token,
            @Query("id_class") int classId
    );

    @POST("/api/clubs/classes/out-pending")
    Call<ReponseModel> cancelClassPending(
            @Header("Authorization") String token,
            @Query("id_class") int classId
    );

    @POST("/api/member/leave-class-request")
    Call<ReponseModel> leaveClassPending(
            @Header("Authorization") String token
    );

    @GET("/api/coach/classes/getMemberpending")
    Call<List<ApproveModel>> getListJoinClassPending(
            @Header("Authorization") String token,
            @Query("lang") String lang
    );

    @GET("/api/coach/leave-class-requests")
    Call<List<ApproveModel>> getListLeaveClassPending(
            @Header("Authorization") String token,
            @Query("lang") String lang
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

    @POST("/api/coach/classes/disapprove-join")
    Call<ReponseModel> denyJoinClass(
            @Header("Authorization") String token,
            @Query("id_member") int memberId,
            @Query("id_class") int classId
    );

    @POST("/api/coach/classes/update-pending")
    Call<ReponseModel> changeJoinClass(
            @Header("Authorization") String token,
            @Query("id_member") int memberId,
            @Query("old_class_id") int oldClassId,
            @Query("new_class_id") int newClassId
    );

    @GET("/api/teacher/classes/getall")
    Call<JsonObject> getCoachClass(
            @Header("Authorization") String token,
            @Query("lang") String lang
    );
}