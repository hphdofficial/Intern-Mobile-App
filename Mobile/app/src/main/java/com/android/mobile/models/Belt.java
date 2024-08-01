package com.android.mobile.models;

public class Belt {

    private String Name;
    private String Content;

    public Belt(String name, String content) {
        Name = name;
        Content = content;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
