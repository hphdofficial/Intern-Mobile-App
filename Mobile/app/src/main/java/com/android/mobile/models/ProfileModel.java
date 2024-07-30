package com.android.mobile.models;

public class ProfileModel {
    private String username;
    private String ten;
    private String email;
    private String dienthoai;
    private String diachi;
    private String gioitinh;
    private String ngaysinh;
    private String hotengiamho;
    private String dienthoai_giamho;
    private String lastlogin;
    private int chieucao; // Thêm trường chiều cao
    private int cannang; // Thêm trường cân nặng

    private AvatarModel avatar;


    // Getters and setters...

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDienthoai() {
        return dienthoai;
    }

    public void setDienthoai(String dienthoai) {
        this.dienthoai = dienthoai;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(String ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getHotengiamho() {
        return hotengiamho;
    }

    public void setHotengiamho(String hotengiamho) {
        this.hotengiamho = hotengiamho;
    }

    public String getDienthoai_giamho() {
        return dienthoai_giamho;
    }

    public void setDienthoai_giamho(String dienthoai_giamho) {
        this.dienthoai_giamho = dienthoai_giamho;
    }

    public String getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(String lastlogin) {
        this.lastlogin = lastlogin;
    }

    public AvatarModel getAvatar() {
        return avatar;
    }

    public int getChieucao() {
        return chieucao;
    }

    public void setChieucao(int chieucao) {
        this.chieucao = chieucao;
    }

    public int getCannang() {
        return cannang;
    }

    public void setCannang(int cannang) {
        this.cannang = cannang;
    }

    public void setAvatar(AvatarModel avatar) {
        this.avatar = avatar;
    }

    public String getAvatarUrl() {
        return avatar != null ? avatar.getAvatarUrl() : null;
    }

}
