package fascon.vovinam.vn.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class ProductModel implements Parcelable {
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
    @SerializedName("tenenglish")
    private String en;

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    @SerializedName("link_image")
    private String image_link;

    private int id_product;
    private int quantity;
    private String CategoryName;
    private String SupplierName;

    private String sale;
    private int noibat;

    public ProductModel(int id_product, int quantity) {
        this.id_product = id_product;
        this.quantity = quantity;
    }

    public ProductModel(String categoryID, String image_link, int productID, String productName, int supplierID, String unitPrice, int unitsInStock) {
        this.categoryID = categoryID;
        this.image_link = image_link;
        this.productID = productID;
        this.productName = productName;
        this.supplierID = supplierID;
        this.unitPrice = unitPrice;
        this.unitsInStock = unitsInStock;
    }

    public ProductModel(int productID, String productName, String unitPrice, String image_link, String categoryName, String supplierName, String sale, int quantity) {
        this.productID = productID;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.image_link = image_link;
        this.quantity = quantity;
        this.CategoryName = categoryName;
        this.SupplierName = supplierName;
        this.sale = sale;
    }
    public ProductModel(String en,int productID, String productName, String unitPrice, String image_link, String categoryName, String supplierName, String sale, int quantity) {
        this.productID = productID;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.image_link = image_link;
        this.quantity = quantity;
        this.CategoryName = categoryName;
        this.SupplierName = supplierName;
        this.sale = sale;
    }

    public ProductModel(String categoryID, String categoryName, String image_link, int noibat, int productID, String productName, int quantity, String sale, int supplierID, String supplierName, String unitPrice, int unitsInStock) {
        this.categoryID = categoryID;
        CategoryName = categoryName;
        this.image_link = image_link;
        this.noibat = noibat;
        this.productID = productID;
        this.productName = productName;
        this.quantity = quantity;
        this.sale = sale;
        this.supplierID = supplierID;
        SupplierName = supplierName;
        this.unitPrice = unitPrice;
        this.unitsInStock = unitsInStock;
    }

    protected ProductModel(Parcel in) {
        productID = in.readInt();
        productName = in.readString();
        supplierID = in.readInt();
        unitPrice = in.readString();
        unitsInStock = in.readInt();
        categoryID = in.readString();
        image_link = in.readString();
        quantity = in.readInt();
        CategoryName = in.readString();
        SupplierName = in.readString();
        sale = in.readString();
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public int getNoibat() {
        return noibat;
    }

    public void setNoibat(int noibat) {
        this.noibat = noibat;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(productID);
        dest.writeString(productName);
        dest.writeInt(supplierID);
        dest.writeString(unitPrice);
        dest.writeInt(unitsInStock);
        dest.writeString(categoryID);
        dest.writeString(image_link);
        dest.writeInt(quantity);
        dest.writeString(CategoryName);
        dest.writeString(SupplierName);
        dest.writeString(sale);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductModel productModel = (ProductModel) o;
        return productID == productModel.productID && Objects.equals(productName, productModel.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productID, productName);
    }
}