package fascon.vovinam.vn.Model;

import com.google.gson.annotations.SerializedName;

public class ProductSaleModel {
    @SerializedName("ProductName")
    private  String name;

    @SerializedName("total_quantity_sold")
    private int quantity;
    @SerializedName("product_price")
    private Double price;
    @SerializedName("id_product")
    private int id;
    @SerializedName("link_image")
    private String linkImage;
    @SerializedName("SupplierID")
    private int categoryId;
    @SerializedName("SupplierName")
    private String nameSup;

    public String getNameSup() {
        return nameSup;
    }

    public void setNameSup(String nameSup) {
        this.nameSup = nameSup;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

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
}
