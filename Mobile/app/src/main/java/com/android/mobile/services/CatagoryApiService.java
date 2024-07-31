package com.android.mobile.services;

import com.android.mobile.models.CatagoryModel;
import com.android.mobile.models.ProductModel;
import com.android.mobile.models.SupplierModelOption;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
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
