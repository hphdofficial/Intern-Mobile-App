package com.android.mobile.models;

import java.util.List;

public class MapsResponse {
    private List<MapsElement> elements;

    public List<MapsElement> getElements() {
        return elements;
    }

    public void setElements(List<MapsElement> elements) {
        this.elements = elements;
    }
}