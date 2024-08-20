package fascon.vovinam.vn.Model;

import com.google.gson.annotations.SerializedName;

public class OptionSupplier {
    private int SupplierID;
    private String SupplierName;

    @SerializedName("tenen")
    private String SupplierNameEn;

    private boolean isChecked;

    public OptionSupplier(boolean isChecked, int supplierID, String supplierName, String supplierNameEn) {
        this.isChecked = isChecked;
        SupplierID = supplierID;
        SupplierName = supplierName;
        SupplierNameEn = supplierNameEn;
    }

    public OptionSupplier() {
    }

    public String getSupplierNameEn() {
        return SupplierNameEn;
    }

    public void setSupplierNameEn(String supplierNameEn) {
        SupplierNameEn = supplierNameEn;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(int supplierID) {
        SupplierID = supplierID;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }
}
