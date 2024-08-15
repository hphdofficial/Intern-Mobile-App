package fascon.vovinam.vn.Model.services;

import fascon.vovinam.vn.Model.OrderListModel;
import fascon.vovinam.vn.Model.OrderModel;
import fascon.vovinam.vn.Model.ProductModel;
import fascon.vovinam.vn.Model.ReponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface OrderApiService {
    @GET("/api/orders/All")
    Call<List<OrderModel>> getHistoryOrder(
            @Header("Authorization") String token
    );

    @GET("/api/chitiethoadon")
    Call<List<ProductModel>> getListProductOrder(
            @Query("id_order") int orderId
    );

    @GET("api/orders/All")
    Call<List<OrderListModel>> getListOrder(
            @Header("Authorization") String token
    );

    @GET("/admin_order")
    Call<List<OrderListModel>> getListAllOrder(
            @Header("Authorization") String token
    );

    @GET("/update_second_delivery")
    Call<ReponseModel> updateConfirmOrder(
            @Query("id") String txnRef
    );

    @GET("/update_third_delivery")
    Call<ReponseModel> updateGetOrder(
            @Query("id") String txnRef
    );

    @GET("/delivery_update")
    Call<ReponseModel> updateShipOrder(
            @Query("id") String txnRef
    );

    @GET("/update_delete_delivery")
    Call<ReponseModel> cancelOrder(
            @Query("id") String txnRef,
            @Query("action") String action
    );
}