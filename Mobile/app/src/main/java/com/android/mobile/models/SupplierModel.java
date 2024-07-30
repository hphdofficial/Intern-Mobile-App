package com.android.mobile.models;

public class SupplierModel {
    private int SupplierID;
    private String SupplierName;
    private String Address;
    private String Phone;
    private String Email;

    // Constructor for mock data
    public SupplierModel(int supplierID, String supplierName, String address, String phone, String email) {
        this.SupplierID = supplierID;
        this.SupplierName = supplierName;
        this.Address = address;
        this.Phone = phone;
        this.Email = email;
    }

    // Getters and Setters
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
