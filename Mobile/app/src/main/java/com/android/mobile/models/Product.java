package com.android.mobile.models;

public class Product {

    private String linkImage;
    private String name;
    private Float price;
    private String supplier;
    private String type;
    private int quantity;

    public Product(String name, Float price, String supplier, String type,int quantity, String linkImage) {
        this.name = name;
        this.price = price;
        this.supplier = supplier;
        this.type = type;
        this.quantity = quantity;
        this.linkImage = linkImage;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public Product() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Float getPrice() {
        return price;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getType() {
        return type;
    }
}
