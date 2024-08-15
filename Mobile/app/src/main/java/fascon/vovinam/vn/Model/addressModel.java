package fascon.vovinam.vn.Model;

public class addressModel {
    private String address;
    private String phone;
    private int selection;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    public addressModel(String address, String phone, int selection) {
        this.address = address;
        this.phone = phone;
        this.selection = selection;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
