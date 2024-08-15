package fascon.vovinam.vn.Model;

import com.google.gson.annotations.SerializedName;

public class ClassModelT {
    @SerializedName("id")
    private String id;
    @SerializedName("ten")
    private String ten;
    @SerializedName("thoigian")
    private String thoigian;
    @SerializedName("giatien")
    private String giatien;@SerializedName("dienthoai")
    private String dienthoai;
    @SerializedName("club")
    private String club;
    @SerializedName("giangvien")
    private String giangvien;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getThoigian() {
        return thoigian;
    }

    public void setThoigian(String thoigian) {
        this.thoigian = thoigian;
    }

    public String getGiatien() {
        return giatien;
    }

    public void setGiatien(String giatien) {
        this.giatien = giatien;
    }

    public String getDienthoai() {
        return dienthoai;
    }

    public void setDienthoai(String dienthoai) {
        this.dienthoai = dienthoai;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getGiangvien() {
        return giangvien;
    }

    public void setGiangvien(String giangvien) {
        this.giangvien = giangvien;
    }
}
