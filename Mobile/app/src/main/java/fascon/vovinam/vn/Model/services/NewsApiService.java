package fascon.vovinam.vn.Model.services;

import fascon.vovinam.vn.Model.ClubModel;
import fascon.vovinam.vn.Model.NewsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NewsApiService {
    @GET("api/news/latest")
    Call<List<NewsModel>> getAnouncements();

    @GET("api/news")
    Call<List<NewsModel>> getAllNews();

    @GET("api/thongbao/search/{tenvi}")
    Call<List<NewsModel>> searchAnouncements(@Path("tenvi") String tenvi);

    @GET("api/clubs/getall")
    Call<List<ClubModel>> getAllClubs();

    @GET("api/news/filter-by-club/{id_club}")
    Call<List<NewsModel>> filterAnnouncementsByClub(@Path("id_club") int idClub);

    @GET("api/clubs/getall?lang=en")
    Call<List<ClubModel>> getAllClubsInEnglish();

//    @GET("api/news/filter-announcements-by-club/{id_club}")
//    Call<List<NewsModel>> filterAnnouncementsByClub(@Path("id_club") int idClub);
}
