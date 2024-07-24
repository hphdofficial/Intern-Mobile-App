package com.android.mobile.services;

import com.android.mobile.models.RegisterModel;
import com.android.mobile.models.ReponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApiService {
    @POST("api/auth/register")
    Call<ReponseModel> registerUser(@Body RegisterModel request);
}
