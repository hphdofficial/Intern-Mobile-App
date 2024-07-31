package com.android.mobile.models;

public class UpdatePasswordModel {
    private String email;
    private String current_pass;
    private String new_pass;
    private String new_pass_confirmation;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrent_pass() {
        return current_pass;
    }

    public void setCurrent_pass(String current_pass) {
        this.current_pass = current_pass;
    }

    public String getNew_pass() {
        return new_pass;
    }

    public void setNew_pass(String new_pass) {
        this.new_pass = new_pass;
    }

    public String getNew_pass_confirmation() {
        return new_pass_confirmation;
    }

    public void setNew_pass_confirmation(String new_pass_confirmation) {
        this.new_pass_confirmation = new_pass_confirmation;
    }
}
