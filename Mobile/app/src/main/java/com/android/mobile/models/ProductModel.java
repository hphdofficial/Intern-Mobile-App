package com.android.mobile.models;

import com.google.gson.annotations.SerializedName;

public class ProductModel {
    @SerializedName("ProductID")
    private int productID;

    @SerializedName("ProductName")
    private String productName;

    @SerializedName("SupplierID")
    private int supplierID;

    @SerializedName("UnitPrice")
    private String unitPrice;

    @SerializedName("UnitsInStock")

    private int unitsInStock;

    @SerializedName("CategoryID")
    private String categoryID;

    public ProductModel(int productID, String productName, int supplierID, String unitPrice, int unitsInStock, String categoryID) {
        this.productID = productID;
        this.productName = productName;
        this.supplierID = supplierID;
        this.unitPrice = unitPrice;
        this.unitsInStock = unitsInStock;
        this.categoryID = categoryID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getUnitsInStock() {
        return unitsInStock;
    }

    public void setUnitsInStock(int unitsInStock) {
        this.unitsInStock = unitsInStock;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }
}
