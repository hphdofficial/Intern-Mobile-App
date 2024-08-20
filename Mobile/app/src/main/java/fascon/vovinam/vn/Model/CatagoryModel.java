package fascon.vovinam.vn.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class CatagoryModel {
    private String CategoryID;
    private String CategoryName;

    @SerializedName("tenen")
    private String CategoryNameEn;
    private Date created_at;
    private Date update_at;

    public CatagoryModel(String categoryID, String categoryName, String categoryNameEn, Date created_at, Date update_at) {
        CategoryID = categoryID;
        CategoryName = categoryName;
        CategoryNameEn = categoryNameEn;
        this.created_at = created_at;
        this.update_at = update_at;
    }

    public String getCategoryNameEn() {
        return CategoryNameEn;
    }

    public void setCategoryNameEn(String categoryNameEn) {
        CategoryNameEn = categoryNameEn;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(Date update_at) {
        this.update_at = update_at;
    }
}
