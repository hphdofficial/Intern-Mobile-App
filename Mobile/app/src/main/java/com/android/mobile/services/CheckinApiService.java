package com.android.mobile.services;

import com.android.mobile.models.AttendanceRequest;
import com.android.mobile.models.CheckinMemberModel;
import com.android.mobile.models.CheckinTeacherModel;
import com.android.mobile.models.Class;
import com.android.mobile.models.ClassModel;
import com.android.mobile.models.TheoryModel;
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

public interface CheckinApiService {
    @GET("api/user/classes")
    Call<ClassModel[]> memberViewClass(@Header("Authorization") String token);

    @GET("api/member/view-checkin")
    Call<JsonObject> memberViewCheckin(@Header("Authorization") String token,
                                               @Query("start_date") String start,
                                               @Query("end_date") String end);

    @GET("api/teacher/view-checkin")
    Call<JsonObject> teacherViewCheckin(@Header("Authorization") String token,
                                                @Query("start_date") String start,
                                                @Query("end_date") String end,
                                        @Query("id_class") int id);

    @POST("api/teacher/checkin")
    Call<Void> teacherCheckin(@Header("Authorization") String token,
                              @Body AttendanceRequest request);

    @GET("api/teacher/classes/getall")
    Call<JsonObject> getTeacherClasses(@Header("Authorization") String token);

    @GET("api/teacher/classes/getdetail")
    Call<JsonObject> getClassMembers(@Header("Authorization") String token,
                                     @Query("id_class") int idClass);
}
