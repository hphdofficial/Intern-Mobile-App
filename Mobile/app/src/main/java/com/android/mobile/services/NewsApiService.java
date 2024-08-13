package com.android.mobile.services;

import com.android.mobile.models.ClubModel;
import com.android.mobile.models.NewsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NewsApiService {
    @GET("api/thongbao")
    Call<List<NewsModel>> getAnouncements();

    @GET("api/news")
    Call<List<NewsModel>> getAllNews();  // Sử dụng API này để lấy toàn bộ tin tức

    @GET("api/thongbao/search/{tenvi}")
    Call<List<NewsModel>> searchAnouncements(@Path("tenvi") String tenvi);

    @GET("api/clubs/getall")
    Call<List<ClubModel>> getAllClubs();

    @GET("api/news/filter-announcements-by-club/{id_club}")
    Call<List<NewsModel>> filterAnnouncementsByClub(@Path("id_club") int idClub);

}
