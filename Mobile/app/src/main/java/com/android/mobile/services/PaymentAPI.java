package com.android.mobile.services;

import com.android.mobile.HistoryRegisterClass;
import com.android.mobile.models.Belt;
import com.android.mobile.models.BeltModel;
import com.android.mobile.models.CartModel;
import com.android.mobile.models.CartResponse;
import com.android.mobile.models.ClassModelT;
import com.android.mobile.models.DetailsBelt;
import com.android.mobile.models.HistoryClassModel;
import com.android.mobile.models.ProfileModel;
import com.android.mobile.models.StatusOrther;
import com.android.mobile.models.StatusRegister;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PaymentAPI {

    @GET("api/cart")
    Call<CartResponse> getCart(@Query("member_id") String member_id);


    @GET("api/alldai")
    Call<List<Belt>> getAllBelt();

    @GET("paymentss/getlink")
    Call<ResponseBody> getLink(
            @Header("Authorization") String token,
            @Query("member_id") String member_id);

    @GET("pay_clb/getlink")
    Call<ResponseBody> RegisterClass(
            @Header("Authorization") String token,
            @Query("id_class") String id_class, @Query("amount") Double amount);

    @GET("api/members/capdai")
    Call<BeltModel> GetBelt(
            @Header("Authorization") String token
            );

    @GET("api/payment-history")
    Call<List<HistoryClassModel>> GetHistoryClass(
            @Header("Authorization") String token
    );
    @GET("api/user/classes")
    Call<List<ClassModelT>> GetClass(
            @Header("Authorization") String token
    );

    @GET("api/education-grades/belt-info")
    Call<List<DetailsBelt>> getBeltInfo(@Query ("id") int id);

    @GET("status_order")
    Call<StatusOrther> GetStatusOrder(@Query ("id") int id);

    @GET("status_payingclass")
    Call<StatusRegister> GetStatusRegister(@Query ("id") int id);

}
