package com.android.mobile.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {
    @SerializedName("success")
    private String success;

    @SerializedName("data")
    private List<ClassData> data;

    public String getSuccess() {
        return success;
    }

    public List<ClassData> getData() {
        return data;
    }
}
