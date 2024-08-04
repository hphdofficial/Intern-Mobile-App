package com.android.mobile.models;

import com.google.gson.annotations.SerializedName;

public class HistoryClassModel {

    @SerializedName("id")
    private String idBill;
    @SerializedName("created_at")
    private String date;
    @SerializedName("status")
    private String status;
    @SerializedName("ten_lop")
    private String nameClass;
    @SerializedName("hocphi")
    private Double price;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getIdBill() {
        return idBill;
    }

    public void setIdBill(String idBill) {
        this.idBill = idBill;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNameClass() {
        return nameClass;
    }

    public void setNameClass(String nameClass) {
        this.nameClass = nameClass;
    }
}
