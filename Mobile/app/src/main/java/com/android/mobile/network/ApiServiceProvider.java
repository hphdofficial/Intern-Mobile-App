package com.android.mobile.network;
import com.android.mobile.ClubActivity;
import com.android.mobile.services.ClassApiService;
import com.android.mobile.services.ClubApiService;
import com.android.mobile.services.UserApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiServiceProvider {
    private static final String BASE_URL = "https://vovinammoi-4bedb6dd1c05.herokuapp.com/";
    private static Retrofit retrofit = null;

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            Gson gson = new GsonBuilder().create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static UserApiService getUserApiService() {
        return getRetrofitInstance().create(UserApiService.class);
    }

    public static ClubApiService getClubApiService() {
        return getRetrofitInstance().create(ClubApiService.class);
    }

    public static ClassApiService getClassApiService() {
        return getRetrofitInstance().create(ClassApiService.class);
    }
}