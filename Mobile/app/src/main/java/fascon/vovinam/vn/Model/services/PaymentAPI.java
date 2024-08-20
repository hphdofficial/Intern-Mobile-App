package fascon.vovinam.vn.Model.services;

import fascon.vovinam.vn.Model.Belt;
import fascon.vovinam.vn.Model.BeltModel;
import fascon.vovinam.vn.Model.CartResponse;
import fascon.vovinam.vn.Model.ClassModelT;
import fascon.vovinam.vn.Model.DetailsBelt;
import fascon.vovinam.vn.Model.HistoryClassModel;
import fascon.vovinam.vn.Model.ProductSaleDownModel;
import fascon.vovinam.vn.Model.ProductSaleModel;
import fascon.vovinam.vn.Model.StatusOrther;
import fascon.vovinam.vn.Model.StatusRegister;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface PaymentAPI {

    @GET("api/cart")
    Call<CartResponse> getCart(@Query("member_id") String member_id);


    @GET("api/all-belts")
    Call<List<Belt>> getAllBelt(@Query("lang") String language);

    @GET("paymentss/getlink")
    Call<ResponseBody> getLink(
            @Header("Authorization") String token,
            @Query("member_id") String member_id);

    @GET("pay_clb/getlink")
    Call<ResponseBody> RegisterClass(
            @Header("Authorization") String token,
            @Query("id_class") String id_class, @Query("amount") Double amount);

    @GET("getlink_nopay")
    Call<ResponseBody> OrderBill(
            @Header("Authorization") String token
            );

    @GET("api/members/belt-level")
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
    Call<List<DetailsBelt>> getBeltInfo(@Query ("id") int id,@Query ("lang") String language);

    @GET("status_order")
    Call<StatusOrther> GetStatusOrder(@Query ("id") int id);

    @GET("status_payingclass")
    Call<StatusRegister> GetStatusRegister(@Query ("id") int id);

    @GET("api/getTopSellingProducts")
    Call<List<ProductSaleModel>> GetSaleProduct(@Query ("lang") String language);

    @GET("api/sale-products")
    Call<List<ProductSaleDownModel>> GetSaleDownProduct();


}
