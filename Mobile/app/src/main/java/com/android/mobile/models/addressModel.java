package com.android.mobile.models;

public class addressModel {
    private String address;
    private int selection;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    public addressModel(String address, int selection) {
        this.address = address;
        this.selection = selection;
    }
}
