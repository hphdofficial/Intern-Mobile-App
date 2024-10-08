package fascon.vovinam.vn.Model.services;

import fascon.vovinam.vn.Model.CartItem;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CartApiService {
    @GET("/api/cart")
    Call<JsonObject> getCart(
            @Query("member_id") int member_id,
            @Query("lang") String lang
    );

    @GET("/api/cart/total-price")
    Call<JsonObject> getTotalPrice(
            @Query("member_id") int member_id
    );

    @POST("/api/cart/remove")
    Call<JsonObject> removeProduct(
            @Body CartItem cartItem
    );

    @POST("/api/cart/increase-quantity")
    Call<JsonObject> increaseQuantity(
            @Body CartItem cartItem
    );

    @POST("/api/cart/decrease-quantity")
    Call<JsonObject> decreaseQuantity(
            @Body CartItem cartItem
    );
}