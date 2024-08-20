package fascon.vovinam.vn.Model;

public class OptionCategory {
    private String CategoryName;
    private String CategoryID;
    private boolean isChecked;
    private String CategoryNameEn;

    public OptionCategory(String categoryID, String categoryName, String categoryNameEn, boolean isChecked) {
        CategoryID = categoryID;
        CategoryName = categoryName;
        CategoryNameEn = categoryNameEn;
        this.isChecked = isChecked;
    }

    public String getCategoryNameEn() {
        return CategoryNameEn;
    }

    public void setCategoryNameEn(String categoryNameEn) {
        CategoryNameEn = categoryNameEn;
    }

    public OptionCategory() {
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
