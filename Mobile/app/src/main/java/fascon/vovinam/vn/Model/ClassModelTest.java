package fascon.vovinam.vn.Model;

public class ClassModelTest {
    private int id;
    private String ten;
    private String thoigian;
    private int giatien;
    private String dienthoai;
    private String club;
    private String giangvien;

    public ClassModelTest() {
    }

    public ClassModelTest(int id, String ten, String thoigian, int giatien, String dienthoai, String club, String giangvien) {
        this.id = id;
        this.ten = ten;
        this.thoigian = thoigian;
        this.giatien = giatien;
        this.dienthoai = dienthoai;
        this.club = club;
        this.giangvien = giangvien;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getGiatien() {
        return giatien;
    }

    public void setGiatien(int giatien) {
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
