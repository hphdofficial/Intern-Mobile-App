package com.android.mobile.models;

import com.google.gson.annotations.SerializedName;

public class Belt {
    @SerializedName("hinhanh")
    private String linkImage;
    @SerializedName("id")
    private int id;
    @SerializedName("ten")
    private String Name;
    @SerializedName("mo_ta_chi_tiet")
    private String Content;
    @SerializedName("mau_dai")
    private String color;

    @SerializedName("danh_xung")
    private String danhxung;

    public Belt(String linkImage, int id, String name, String content, String color, String danhxung) {
        this.linkImage = linkImage;
        this.id = id;
        Name = name;
        Content = content;
        this.color = color;
        this.danhxung = danhxung;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDanhxung() {
        return danhxung;
    }

    public void setDanhxung(String danhxung) {
        this.danhxung = danhxung;
    }
}
