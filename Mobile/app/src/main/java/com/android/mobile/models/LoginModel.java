package com.android.mobile.models;

public class LoginModel {
    private String login;  // Có thể là username, email hoặc số điện thoại
    private String password;  // Mật khẩu của người dùng

    // Constructor
    public LoginModel(String login, String password) {
        this.login = login;
        this.password = password;
    }

    // Getter cho login
    public String getLogin() {
        return login;
    }

    // Setter cho login
    public void setLogin(String login) {
        this.login = login;
    }

    // Getter cho password
    public String getPassword() {
        return password;
    }

    // Setter cho password
    public void setPassword(String password) {
        this.password = password;
    }
}
