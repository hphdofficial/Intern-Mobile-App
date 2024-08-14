package com.android.mobile.models;

public class ApproveModel {
    private int id_club;
    private int id_class;
    private int id_member;
    private String ten_club;
    private String class_name;
    private String ten;
    private String created_at;
    private String ten_class;
    private String ten_member;

    public ApproveModel(int id_member, int id_club) {
        this.id_member = id_member;
        this.id_club = id_club;
    }

    public int getId_club() {
        return id_club;
    }

    public void setId_club(int id_club) {
        this.id_club = id_club;
    }

    public int getId_class() {
        return id_class;
    }

    public void setId_class(int id_class) {
        this.id_class = id_class;
    }

    public int getId_member() {
        return id_member;
    }

    public void setId_member(int id_member) {
        this.id_member = id_member;
    }

    public String getTen_club() {
        return ten_club;
    }

    public void setTen_club(String ten_club) {
        this.ten_club = ten_club;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTen_member() {
        return ten_member;
    }

    public void setTen_member(String ten_member) {
        this.ten_member = ten_member;
    }

    public String getTen_class() {
        return ten_class;
    }

    public void setTen_class(String ten_class) {
        this.ten_class = ten_class;
    }
}