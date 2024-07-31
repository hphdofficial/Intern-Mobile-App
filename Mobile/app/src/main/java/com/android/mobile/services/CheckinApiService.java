package com.android.mobile.services;

import com.android.mobile.models.Class;
import com.android.mobile.models.TheoryModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CheckinApiService {
    @GET("api/member/view-checkin")
    Call<TheoryModel> memberViewCheckin (@Body TheoryModel request);

    @GET("api/teacher/view-checkin")
    Call<TheoryModel> getMartialArtsTheoryDetail (@Body TheoryModel request);

    @POST("api/teacher/checkin")
    Call<TheoryModel> teacherCheckin (@Body TheoryModel request);

    @GET("api/teacher/classes/getall")
    Call<List<Class>> getTeacherClasses();

    @GET("api/teacher/classes/getdetail")
    Call<TheoryModel> getClassMembers (@Body TheoryModel request);
}
