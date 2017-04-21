package com.byteshaft.smssender;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;

public class AppGlobals extends Application {


    public static final String SERVER_IP = "http://34.209.111.75:8080";
    public static final String BASE_URL = String.format("%s/api/", SERVER_IP);
    public static final String KEY = "key";

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
}
