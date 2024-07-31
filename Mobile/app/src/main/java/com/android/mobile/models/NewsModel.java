package com.android.mobile.models;

import com.google.gson.annotations.SerializedName;

public class NewsModel {

    @SerializedName("id")
    private int id;

    @SerializedName("photo")
    private String photo;

    @SerializedName("tenvi")
    private String tenvi;

    @SerializedName("noidungvi")
    private String noidungvi;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTenvi() {
        return tenvi;
    }

    public void setTenvi(String tenvi) {
        this.tenvi = tenvi;
    }

    public String getNoidungvi() {
        return noidungvi;
    }

    public void setNoidungvi(String noidungvi) {
        this.noidungvi = noidungvi;
    }

    @Override
    public String toString() {
        return "NewsModel{" +
                "id=" + id +
                ", photo='" + photo + '\'' +
                ", tenvi='" + tenvi + '\'' +
                ", noidungvi='" + noidungvi + '\'' +
                '}';
    }
}
