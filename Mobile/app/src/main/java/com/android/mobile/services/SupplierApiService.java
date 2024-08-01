package com.android.mobile.services;

import com.android.mobile.models.SupplierModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SupplierApiService {
    @GET("api/suppliers")
    Call<List<SupplierModel>> getSuppliers();
}
