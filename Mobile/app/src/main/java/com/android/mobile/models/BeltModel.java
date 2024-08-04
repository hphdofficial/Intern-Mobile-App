package com.android.mobile.models;

import com.google.gson.annotations.SerializedName;

public class BeltModel {
    @SerializedName("id_capdai")
    private String id;
    @SerializedName("ten")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BeltModel(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
