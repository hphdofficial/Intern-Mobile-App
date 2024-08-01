package com.android.mobile.models;

public class OptionSupplier {
    private int SupplierID;
    private String SupplierName;
    private boolean isChecked;

    public OptionSupplier(boolean isChecked, int supplierID, String supplierName) {
        this.isChecked = isChecked;
        SupplierID = supplierID;
        SupplierName = supplierName;
    }

    public OptionSupplier() {
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(int supplierID) {
        SupplierID = supplierID;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }
}
