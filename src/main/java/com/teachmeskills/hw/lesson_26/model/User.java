package com.teachmeskills.hw.lesson_26.model;

public class User {

    String login;
    String password;
    String role;

    public User(String login, String password, String role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public String getLogin() {
        return login;
    }


    public String getPassword() {
        return password;
    }


    public String getRole() {
        return role;
    }
}
