package fascon.vovinam.vn.Model;

public class Member {
    private String ten;
    private String capDai;
    private String maHocVien;

    public Member(String capDai, String maHocVien, String ten) {
        this.capDai = capDai;
        this.maHocVien = maHocVien;
        this.ten = ten;
    }

    public Member() {
    }

    public String getCapDai() {
        return capDai;
    }

    public void setCapDai(String capDai) {
        this.capDai = capDai;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getMaHocVien() {
        return maHocVien;
    }

    public void setMaHocVien(String maHocVien) {
        this.maHocVien = maHocVien;
    }
}
