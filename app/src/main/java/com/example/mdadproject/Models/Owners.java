package com.example.mdadproject.Models;

public class Owners {

    public Owners() {

    }

    String nric;
    String name;
    String mobilenumber;
    String email;
    String address;
    String zipcode;
    String username;
    String password;
    String security;

    public Owners(String nric, String name, String mobilenumber, String email, String address, String zipcode, String username, String password, String security) {
        this.nric = nric;
        this.name = name;
        this.mobilenumber = mobilenumber;
        this.email = email;
        this.address = address;
        this.zipcode = zipcode;
        this.username = username;
        this.password = password;
        this.security = security;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }
}
