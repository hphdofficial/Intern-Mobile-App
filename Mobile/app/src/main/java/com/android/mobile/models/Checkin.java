package com.android.mobile.models;

import java.util.Date;

public class Checkin {
    private Date ngayDiemDanh;
    private boolean hienDien;

    public Checkin(boolean hienDien, Date ngayDiemDanh) {
        this.hienDien = hienDien;
        this.ngayDiemDanh = ngayDiemDanh;
    }

    public Checkin() {
    }

    public boolean isHienDien() {
        return hienDien;
    }

    public void setHienDien(boolean hienDien) {
        this.hienDien = hienDien;
    }

    public Date getNgayDiemDanh() {
        return ngayDiemDanh;
    }

    public void setNgayDiemDanh(Date ngayDiemDanh) {
        this.ngayDiemDanh = ngayDiemDanh;
    }
}
