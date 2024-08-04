package com.android.mobile.models;

public class ClassModel {
    private int id;
    private String ten;
    private String thoigian;

    public ClassModel(int id, String ten, String thoigian) {
        this.id = id;
        this.ten = ten;
        this.thoigian = thoigian;
    }

    public ClassModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getThoigian() {
        return thoigian;
    }

    public void setThoigian(String thoigian) {
        this.thoigian = thoigian;
    }
}
