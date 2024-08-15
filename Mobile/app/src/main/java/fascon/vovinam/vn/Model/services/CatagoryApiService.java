package fascon.vovinam.vn.Model.services;

import fascon.vovinam.vn.Model.CatagoryModel;
import fascon.vovinam.vn.Model.SupplierModelOption;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CatagoryApiService {
    @GET("api/categories")
    Call<List<CatagoryModel>> index();

    @GET("api/suppliers")
    Call<List<SupplierModelOption>> getAllSupplier();

    @GET("api/suppliers/{id}")
    Call<SupplierModelOption> getSupplier(@Path("id") int SupplierID);
}
