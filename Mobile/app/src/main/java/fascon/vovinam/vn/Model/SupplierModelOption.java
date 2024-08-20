package fascon.vovinam.vn.Model;

import com.google.gson.annotations.SerializedName;

public class SupplierModelOption {
    private int SupplierID;
    private String SupplierName;

    @SerializedName("tenen")
    private String SupplierNameEng;

    public SupplierModelOption(int supplierID, String supplierName, String supplierNameEng) {
        SupplierID = supplierID;
        SupplierName = supplierName;
        SupplierNameEng = supplierNameEng;
    }

    public String getSupplierNameEn() {
        return SupplierNameEng;
    }

    public void setSupplierNameEng(String supplierNameEn) {
        SupplierNameEng = supplierNameEn;
    }

    public SupplierModelOption() {
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
