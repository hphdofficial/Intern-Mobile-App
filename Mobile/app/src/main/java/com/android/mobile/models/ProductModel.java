package com.android.mobile.models;

public class ProductModel {
    private int ProductID;
    private String ProductName;
    private int SupplierID;
    private String UnitPrice;
    private int UnitsInStock;
    private String CategoryID;

    public ProductModel(String categoryID, int productID, String productName, int supplierID, String unitPrice, int unitsInStock) {
        CategoryID = categoryID;
        ProductID = productID;
        ProductName = productName;
        SupplierID = supplierID;
        UnitPrice = unitPrice;
        UnitsInStock = unitsInStock;
    }

    public ProductModel() {
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(int supplierID) {
        SupplierID = supplierID;
    }

    public String getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        UnitPrice = unitPrice;
    }

    public int getUnitsInStock() {
        return UnitsInStock;
    }

    public void setUnitsInStock(int unitsInStock) {
        UnitsInStock = unitsInStock;
    }
}
