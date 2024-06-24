package com.android.mobile.models;

public class Lesson {
    private String title;
    private String object;
    private String require;

    private String time;

    public Lesson() {
    }

    public Lesson(String title, String object, String require, String time) {
        this.title = title;
        this.object = object;
        this.require = require;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getRequire() {
        return require;
    }

    public void setRequire(String require) {
        this.require = require;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
