package com.domoffon.efremov.domoffon;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private SharedPreferences prefs;

    Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    void setusename(String usename) {
        prefs.edit().putString("number", usename).apply();
    }

    String getusename() {
        return prefs.getString("number","");
    }

    void setpass(String usename) {
        prefs.edit().putString("password", usename).apply();
    }

    void clean(){
        //TODO: remove password
        prefs.edit().remove("password").apply();
        prefs.edit().remove("secret").apply();
    }

    String getpass() {
        return prefs.getString("password","");
    }

    void setsecret(String usename) {
        prefs.edit().putString("secret", usename).apply();
    }

    public String getsecret() {
        return prefs.getString("secret","");
    }
}
