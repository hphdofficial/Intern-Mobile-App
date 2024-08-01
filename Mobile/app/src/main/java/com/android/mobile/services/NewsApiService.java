package com.android.mobile.services;

import com.android.mobile.models.NewsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NewsApiService {
    @GET("api/thongbao")
    Call<List<NewsModel>> getAnouncements();

    @GET("api/thongbao/search/{tenvi}")
    Call<List<NewsModel>> searchAnouncements(@Path("tenvi") String tenvi);
}
