package com.example.orderedodriver;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UtilsApp {

    private static String IS_LOGIN_KEY = "IS_LOGIN_KEY";
    private static String APP_SHARED_DATA_NAME = "APP_SHARED_DATA_NAME";
    public static boolean isLogin(Activity activity){

        SharedPreferences sharedPref = activity.getSharedPreferences(APP_SHARED_DATA_NAME,Context.MODE_PRIVATE);
        return  sharedPref.getBoolean(IS_LOGIN_KEY, false) ;
    }

    public static void setIsLogin(Activity activity ,boolean isLogin){
        SharedPreferences sharedPref = activity.getSharedPreferences(APP_SHARED_DATA_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(IS_LOGIN_KEY, isLogin);
        editor.apply();
    }
}
