package com.android.mobile.models;

public class Item {
    private String name;
    private String supplier;
    private int price;
    private String material;
    private String dateProduct;
    private String use;
    private String img;
    private String type;

    public Item() {
    }

    public Item(String dateProduct, String use, String supplier, int price, String name, String material, String img, String type) {
        this.dateProduct = dateProduct;
        this.use = use;
        this.supplier = supplier;
        this.price = price;
        this.name = name;
        this.material = material;
        this.img = img;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getDateProduct() {
        return dateProduct;
    }

    public void setDateProduct(String dateProduct) {
        this.dateProduct = dateProduct;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}
