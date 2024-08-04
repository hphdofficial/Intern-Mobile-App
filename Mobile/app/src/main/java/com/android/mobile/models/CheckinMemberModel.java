package com.android.mobile.models;

import java.util.Date;

public class CheckinMemberModel {
    private int id;
    private String ten;
    private String begin_date;
    private boolean isChecked;

    public CheckinMemberModel(String begin_date, int id, boolean isChecked, String ten) {
        this.begin_date = begin_date;
        this.id = id;
        this.isChecked = false;
        this.ten = ten;
    }

    public CheckinMemberModel() {
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
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
}
