    package com.android.mobile.services;

    import com.android.mobile.models.ProductModel;
    import com.android.mobile.models.SupplierModel;

    import java.util.List;

    import retrofit2.Call;
    import retrofit2.http.Body;
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
