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

    @SerializedName("link_image")
    private String image_link;

    private int quantity;
    private String CategoryName;
    private String SupplierName;

    public ProductModel(String categoryID, String image_link, int productID, String productName, int supplierID, String unitPrice, int unitsInStock) {
        this.categoryID = categoryID;
        this.image_link = image_link;
        this.productID = productID;
        this.productName = productName;
        this.supplierID = supplierID;
        this.unitPrice = unitPrice;
        this.unitsInStock = unitsInStock;
    }

    public ProductModel(int productID, String productName, String unitPrice, String image_link, String categoryName, String supplierName, int quantity) {
        this.productID = productID;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.image_link = image_link;
        this.quantity = quantity;
        this.CategoryName = categoryName;
        this.SupplierName = supplierName;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        this.CategoryName = categoryName;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }
}