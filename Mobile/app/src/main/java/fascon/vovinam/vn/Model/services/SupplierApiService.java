    package fascon.vovinam.vn.Model.services;

    import fascon.vovinam.vn.Model.ProductModel;
    import fascon.vovinam.vn.Model.SupplierModel;

    import java.util.List;

    import retrofit2.Call;
    import retrofit2.http.GET;
    import retrofit2.http.POST;
    import retrofit2.http.Path;
    import retrofit2.http.Query;

    public interface SupplierApiService {
        @GET("api/suppliers")
        Call<List<SupplierModel>> getSuppliers();

        @GET("api/suppliers/{id}")
        Call<SupplierModel> getSupplier(@Path("id") int id);

        @POST("api/products/filter-by-supplier")
        Call<List<ProductModel>> getProductsBySupplier(@Query("SupplierID") int supplierID);
    }
