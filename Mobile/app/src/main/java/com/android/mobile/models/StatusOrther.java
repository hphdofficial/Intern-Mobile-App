package com.android.mobile.models;

import com.google.gson.annotations.SerializedName;

public class StatusOrther {

    @SerializedName("status")
    private String status;

    @SerializedName("id_order")
    private String id_order;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }
}
