package com.android.mobile.services;

import com.android.mobile.models.NewsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsApiService {
    @GET("api/thongbao")
    Call<List<NewsModel>> getAnouncements();
}
