package com.example.blfatsamples.constants;

import com.example.blfatsamples.model.UserLoginResultModel;

public class Constant {
    private  static UserLoginResultModel UserInfo;
    public  static  String UserName = "USERNAME";
    public  static  String Name = "NAME";
    public  static  String Email = "EMAIL";

    public static void setUserInfo(UserLoginResultModel userInfo) {
        UserInfo = userInfo;
    }

    public static UserLoginResultModel getUserInfo() {
        return UserInfo;
    }
}
