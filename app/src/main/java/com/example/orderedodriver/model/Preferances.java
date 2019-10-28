package com.example.orderedodriver.model;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Preferances {
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public Preferances(Activity activity) {
        sharedPref = activity.getSharedPreferences("ANYTHING", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }
    public void setUId(String id){
        editor.putString(KEYS.UID, id);
        editor.commit();
    }
    public String getUId(){
        return sharedPref.getString(KEYS.UID,"");
    } public void setBarcode(String barcode){
        editor.putString(KEYS.BARCODE, barcode);
        editor.commit();
    }
    public String getBarcode(){
        return sharedPref.getString(KEYS.BARCODE,"");
    }
    public void setKeepLogin(boolean keepLogin){
        editor.putBoolean(KEYS.KEEPLOGIN, keepLogin);
        editor.commit();
    }
    public boolean getKeepLogin(){
        return sharedPref.getBoolean(KEYS.KEEPLOGIN,false);
    }
    public void setCurrentOrder(String current){
        editor.putString(KEYS.CURRENTORDER, current);
        editor.commit();
    }
    public String getCurrentOrder(){
        return sharedPref.getString(KEYS.CURRENTORDER,"");
    }

}