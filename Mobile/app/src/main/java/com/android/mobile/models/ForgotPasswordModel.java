package com.android.mobile.models;

public class ForgotPasswordModel {
    private String email;

    public ForgotPasswordModel(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
