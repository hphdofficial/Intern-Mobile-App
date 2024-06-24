package com.android.mobile.models;

public class Chapter {
    private String title;
    private int view;

    private String description;

    private String link;

    public Chapter() {
    }

    public Chapter(String title, int view, String description, String link) {
        this.title = title;
        this.view = view;
        this.description = description;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
