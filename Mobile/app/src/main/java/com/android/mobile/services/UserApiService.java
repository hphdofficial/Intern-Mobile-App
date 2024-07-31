package com.android.mobile.services;

import com.android.mobile.models.ForgotPasswordModel;
import com.android.mobile.models.LoginModel;
import com.android.mobile.models.ProfileModel;
import com.android.mobile.models.RegisterModel;
import com.android.mobile.models.ReponseModel;
import com.android.mobile.models.ResetPasswordModel;
import com.android.mobile.models.TokenModel;
import com.android.mobile.models.UpdateInfoModel;
import com.android.mobile.models.UpdatePasswordModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserApiService {
    @POST("api/auth/register")
    Call<ReponseModel> registerUser(@Body RegisterModel request);

    @POST("api/auth/login")
    Call<TokenModel> loginUser(@Body LoginModel request);

    @GET("api/auth/profile")
    Call<ProfileModel> getProfile(@Header("Authorization") String token);

    @Multipart
    @POST("api/upload/avatar")
    Call<ReponseModel> uploadAvatar(
            @Header("Authorization") String token,
            @Part MultipartBody.Part avatar
    );

    @POST("api/forgotpassword/request")
    Call<ReponseModel> sendOtp(@Body ForgotPasswordModel request);

    @POST("api/forgotpassword/reset")
    Call<ReponseModel> resetPassword(@Body ResetPasswordModel request);

    @PUT("api/auth/updateInfo")
    Call<ReponseModel> updateInfo(
            @Header("Authorization") String token,
            @Body UpdateInfoModel request
    );


    @PUT("api/auth/updatePass")
    Call<ReponseModel> updatePassword(
            @Header("Authorization") String token,
            @Body UpdatePasswordModel request
    );

}
