package fascon.vovinam.vn.Model;

public class SupplierModel {
    private int SupplierID;
    private String SupplierName;
    private String Address;
    private String Phone;
    private String Email;
    private String tenen;
    private String diachien;


    // Getters and Setters
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTenen() {
        return tenen;
    }

    public void setTenen(String tenen) {
        this.tenen = tenen;
    }

    public String getDiachien() {
        return diachien;
    }

    public void setDiachien(String diachien) {
        this.diachien = diachien;
    }
}
