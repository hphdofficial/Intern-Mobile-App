package com.android.mobile.models;

public class NewsModel {
    private String title;
    private int imageResource;

    public NewsModel(String title, int imageResource) {
        this.title = title;
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResource() {
        return imageResource;
    }
}
