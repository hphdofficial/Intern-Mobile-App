package com.android.mobile.models;

public class RegisterModel {
    private String username;
    private String password;
    private String email;
    private String ten;
    private String dienthoai;
    private String diachi;
    private String gioitinh;
    private String ngaysinh;
    private String hoten_giamho;
    private String dienthoai_giamho;
    private int chieucao; // Thêm trường chiều cao
    private int cannang; // Thêm trường cân nặng

    // Getters and setters...
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
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

    public String getHoten_giamho() {
        return hoten_giamho;
    }

    public void setHoten_giamho(String hoten_giamho) {
        this.hoten_giamho = hoten_giamho;
    }

    public String getDienthoai_giamho() {
        return dienthoai_giamho;
    }

    public void setDienthoai_giamho(String dienthoai_giamho) {
        this.dienthoai_giamho = dienthoai_giamho;
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
}
