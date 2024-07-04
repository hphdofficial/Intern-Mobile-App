package com.android.mobile.models;

public class Member {
    private String ten;
    private String capDai;

    public Member(String capDai, String ten) {
        this.capDai = capDai;
        this.ten = ten;
    }

    public Member() {
    }

    public String getCapDai() {
        return capDai;
    }

    public void setCapDai(String capDai) {
        this.capDai = capDai;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
}
