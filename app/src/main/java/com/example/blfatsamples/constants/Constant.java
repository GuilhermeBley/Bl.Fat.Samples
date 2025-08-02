package com.example.blfatsamples.constants;

import android.content.SharedPreferences;

import com.example.blfatsamples.model.UserLoginResultModel;

public class Constant {
    private  static UserLoginResultModel UserInfo;

    public  static  String Name = "NAME";
    public  static  String Email = "EMAIL";
    public  static  String Token = "TOKEN";

    public  static  String UserId = "ID";
    public static String SharedPreferenceLogin = "Authorization";

    public static void setUserInfo(UserLoginResultModel userInfo) {
        UserInfo = userInfo;
    }

    public static UserLoginResultModel getUserInfo() {
        return UserInfo;
    }

    public static void setUserInfo(UserLoginResultModel userInfo, SharedPreferences preference) {
        UserInfo = userInfo;
        SharedPreferences.Editor editor = preference.edit();
        if (UserInfo == null){
            editor.clear();
            editor.apply();
            return;
        }

        editor.putString(Name, userInfo.getName());
        editor.putString(Email, userInfo.getEmail());
        editor.putString(Token, userInfo.getToken());
        editor.putInt(UserId, userInfo.getId());
        editor.apply();
    }

    public static UserLoginResultModel getUserInfo(SharedPreferences preference) {

        if (UserInfo == null){
            UserInfo = getByCacheOrDefault(preference);
        }

        if (UserInfo != null && UserInfo.IsTokenExpired())
            return  null;

        return UserInfo;
    }

    private static UserLoginResultModel getByCacheOrDefault(SharedPreferences preference){

        String email = preference.getString(Email, null);
        String name = preference.getString(Name, null);
        String token = preference.getString(Token, null);
        int id = preference.getInt(UserId, 0);

        if (email == null || name == null || token == null || id == 0)
        {
            return null;
        }

        return new UserLoginResultModel(id, name, token, email);
    }
}
