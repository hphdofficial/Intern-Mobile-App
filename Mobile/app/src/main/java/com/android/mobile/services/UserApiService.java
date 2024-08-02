package com.android.mobile.services;

import com.android.mobile.models.ForgotPasswordModel;
import com.android.mobile.models.LoginModel;
import com.android.mobile.models.ProfileModel;
import com.android.mobile.models.RegisterModel;
import com.android.mobile.models.ReponseModel;
import com.android.mobile.models.ResetPasswordModel;
import com.android.mobile.models.SupplierModel;
import com.android.mobile.models.TokenModel;
import com.android.mobile.models.UpdateInfoModel;
import com.android.mobile.models.UpdatePasswordModel;
import com.android.mobile.models.VerifyOtpModel;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApiService {
    @POST("api/auth/register")
    Call<ReponseModel> registerUser(@Body RegisterModel request);

    @POST("api/auth/login")
    Call<TokenModel> loginUser(@Body LoginModel request);

    @GET("api/auth/profile")
    Call<ProfileModel> getProfile(@Header("Authorization") String token);

    @GET("api/auth/profile/viaId")
    Call<ProfileModel> getProfileViaId(@Header("Authorization") String token, @Query("id_atg_members") int memberId);

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

    @POST("api/forgotpassword/verify")
    Call<ReponseModel> verifyOtp(@Body VerifyOtpModel request);


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
