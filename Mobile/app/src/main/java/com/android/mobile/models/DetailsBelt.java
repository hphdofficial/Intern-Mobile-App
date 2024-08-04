package com.android.mobile.models;

import com.google.gson.annotations.SerializedName;

public class DetailsBelt {
    @SerializedName("code")
    private String code;

    @SerializedName("ten")
    private String ten;

    @SerializedName("level")
    private String level;

    @SerializedName("mau_dai")
    private String coler;

    @SerializedName("danh_xung")
    private String DanhXung;

    @SerializedName("hinhanh")
    private String link;

    @SerializedName("mo_ta_chi_tiet")
    private String MoTa;


    public DetailsBelt(String code, String ten, String level, String coler, String danhXung, String link, String moTa) {
        this.code = code;
        this.ten = ten;
        this.level = level;
        this.coler = coler;
        DanhXung = danhXung;
        this.link = link;
        MoTa = moTa;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getColer() {
        return coler;
    }

    public void setColer(String coler) {
        this.coler = coler;
    }

    public String getDanhXung() {
        return DanhXung;
    }

    public void setDanhXung(String danhXung) {
        DanhXung = danhXung;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }
}
