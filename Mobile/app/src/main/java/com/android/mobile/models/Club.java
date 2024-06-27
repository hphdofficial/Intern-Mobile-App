package com.android.mobile.models;

public class Club {
    private String ten;
    private String diaChi;

    public Club(String diaChi, String ten) {
        this.diaChi = diaChi;
        this.ten = ten;
    }

    public Club() {
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
}
