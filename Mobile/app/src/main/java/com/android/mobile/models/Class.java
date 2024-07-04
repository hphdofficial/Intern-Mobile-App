package com.android.mobile.models;

import java.util.Date;

public class Class {
    private String ten;
    private int hocPhi;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private int thoiGian;
    private String teacherName;

    public Class(int hocPhi, Date ngayBatDau, Date ngayKetThuc, String ten, int thoiGian, String teacherName) {
        this.hocPhi = hocPhi;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.ten = ten;
        this.thoiGian = thoiGian;
        this.teacherName = teacherName;
    }

    public Class() {
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getHocPhi() {
        return hocPhi;
    }

    public void setHocPhi(int hocPhi) {
        this.hocPhi = hocPhi;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(int thoiGian) {
        this.thoiGian = thoiGian;
    }
}
