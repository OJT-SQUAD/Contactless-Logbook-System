package com.example.contactlessapp.DbHelpers;

public class CreateAccountShopHelperClass {

    String accountType, name, location, username, email, password;

    public CreateAccountShopHelperClass() {
    }

    public CreateAccountShopHelperClass(String accountType, String name, String location, String username, String email, String password) {
        this.accountType = accountType;
        this.name = name;
        this.location = location;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
