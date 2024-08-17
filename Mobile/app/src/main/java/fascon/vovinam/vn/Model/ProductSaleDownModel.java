package fascon.vovinam.vn.Model;

import com.google.gson.annotations.SerializedName;

public class ProductSaleDownModel {
    @SerializedName("ProductName")
    private  String name;
    @SerializedName("UnitsInStock")
    private int quantity;
    @SerializedName("UnitPrice")
    private Double price;
    @SerializedName("ProductID")
    private int id;
    @SerializedName("link_image")
    private String linkImage;
    @SerializedName("CategoryName")
    private String categoryName;
    @SerializedName("SupplierName")
    private String nameSup;
    @SerializedName("sale")
    private String sale;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getNameSup() {
        return nameSup;
    }

    public void setNameSup(String nameSup) {
        this.nameSup = nameSup;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }
}
