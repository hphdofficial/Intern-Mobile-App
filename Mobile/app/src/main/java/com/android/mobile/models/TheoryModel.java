package com.android.mobile.models;

import java.util.Date;

public class TheoryModel {
    private int id;
    private String tenvi;
    private String photo;
    private String noidungvi;
    private String link_video;

    public TheoryModel(int id, String link_video, String noidungvi, String photo, String tenvi) {
        this.id = id;
        this.link_video = link_video;
        this.noidungvi = noidungvi;
        this.photo = photo;
        this.tenvi = tenvi;
    }

    public TheoryModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink_video() {
        return link_video;
    }

    public void setLink_video(String link_video) {
        this.link_video = link_video;
    }

    public String getNoidungvi() {
        return noidungvi;
    }

    public void setNoidungvi(String noidungvi) {
        this.noidungvi = noidungvi;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTenvi() {
        return tenvi;
    }

    public void setTenvi(String tenvi) {
        this.tenvi = tenvi;
    }
}
