package com.android.mobile.models;

public class Club {
    private String ten, mota, diachi, dienthoai, nguoiquanly;
    private String id_club, name, address;
    private String id;
    private String map_lat;
    private String map_long;

    public Club() {
    }

    public Club(String id_club) {
        this.id_club = id_club;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getDienthoai() {
        return dienthoai;
    }

    public void setDienthoai(String dienthoai) {
        this.dienthoai = dienthoai;
    }

    public String getNguoiquanly() {
        return nguoiquanly;
    }

    public void setNguoiquanly(String nguoiquanly) {
        this.nguoiquanly = nguoiquanly;
    }

    public String getId_club() {
        return id_club;
    }

    public void setId_club(String id_club) {
        this.id_club = id_club;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMap_lat() {
        return map_lat;
    }

    public void setMap_lat(String map_lat) {
        this.map_lat = map_lat;
    }

    public String getMap_long() {
        return map_long;
    }

    public void setMap_long(String map_long) {
        this.map_long = map_long;
    }
}