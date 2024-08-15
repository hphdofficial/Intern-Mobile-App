package fascon.vovinam.vn.Model.network;
import fascon.vovinam.vn.Model.services.NewsApiService;
import fascon.vovinam.vn.Model.services.OrderApiService;
import fascon.vovinam.vn.Model.services.ProductApiService;
import fascon.vovinam.vn.Model.services.SupplierApiService;
import fascon.vovinam.vn.Model.services.CatagoryApiService;
import fascon.vovinam.vn.Model.services.CheckinApiService;
import fascon.vovinam.vn.Model.services.TheoryApiService;
import fascon.vovinam.vn.Model.services.CartApiService;
import fascon.vovinam.vn.Model.services.ClassApiService;
import fascon.vovinam.vn.Model.services.ClubApiService;
import fascon.vovinam.vn.Model.services.UserApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiServiceProvider {
    private static final String BASE_URL = "https://vovinammoi-4bedb6dd1c05.herokuapp.com/";
    private static final String MAPS_URL = "https://vovinammoi-4bedb6dd1c05.herokuapp.com/";
    private static Retrofit retrofit = null;
    private static Retrofit retrofitMaps = null;

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

    public static Retrofit getMapsInstance() {
        if (retrofitMaps == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            Gson gson = new GsonBuilder().create();

            retrofitMaps = new Retrofit.Builder()
                    .baseUrl(MAPS_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofitMaps;
    }

    public static UserApiService getUserApiService() {
        return getRetrofitInstance().create(UserApiService.class);
    }

    public static SupplierApiService getSupplierApiService() {
        return getRetrofitInstance().create(SupplierApiService.class);
    }
    public static NewsApiService getNewsApiService() {
        return getRetrofitInstance().create(NewsApiService.class);
    }
    public static ProductApiService getProductApiService() {
        return getRetrofitInstance().create(ProductApiService.class);
    }

    public static CatagoryApiService getCatagoryApiService() {
        return getRetrofitInstance().create(CatagoryApiService.class);
    }

    public static CheckinApiService getCheckinApiService() {
        return getRetrofitInstance().create(CheckinApiService.class);
    }

    public static TheoryApiService getTheoryApiService() {
        return getRetrofitInstance().create(TheoryApiService.class);
    }

    public static ClubApiService getClubApiService() {
        return getRetrofitInstance().create(ClubApiService.class);
    }

    public static ClassApiService getClassApiService() {
        return getRetrofitInstance().create(ClassApiService.class);
    }

    public static CartApiService getCartApiService() {
        return getRetrofitInstance().create(CartApiService.class);
    }

    public static OrderApiService getOrderApiService() {
        return getRetrofitInstance().create(OrderApiService.class);
    }
}