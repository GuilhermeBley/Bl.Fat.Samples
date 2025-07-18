package com.example.blfatsamples.model;

public class UserLoginResultModel {
    private  String name;
    private  String lastName;
    private  String email;

    public UserLoginResultModel(String name, String lastName, String email) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
