package com.example.blfatsamples.model;

public class UserLoginResultModel {
    private  int id;
    private  String name;
    private  String token;
    private  String email;

    public UserLoginResultModel(int id, String name, String token, String email) {
        this.id = id;
        this.name = name;
        this.token = token;
        this.email = email;
    }

    public int getId() {
        return id;
    }
    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
