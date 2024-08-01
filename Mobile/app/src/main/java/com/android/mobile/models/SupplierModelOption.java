package com.android.mobile.models;

public class SupplierModelOption {
    private int SupplierID;
    private String SupplierName;

    public SupplierModelOption(int supplierID, String supplierName) {
        SupplierID = supplierID;
        SupplierName = supplierName;
    }

    public SupplierModelOption() {
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
