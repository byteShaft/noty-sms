package com.byteshaft.smssender;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.FirebaseApp;

public class AppGlobals extends Application {


    public static final String SERVER_IP = "http://34.209.111.75:8080";
    public static final String BASE_URL = String.format("%s/api/", SERVER_IP);
    public static final String KEY = "key";

    private static final String SERVICE_KEY = "iServiceON";

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }


    public static SharedPreferences getPreferenceManager() {
        return getContext().getSharedPreferences("shared_prefs", MODE_PRIVATE);
    }


    public static void saveState(boolean state) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putBoolean(SERVICE_KEY, state).apply();
    }

    public static boolean isServiceOn() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getBoolean(SERVICE_KEY, false);
    }
}
