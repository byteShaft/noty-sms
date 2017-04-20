package com.byteshaft.smssender;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;

public class AppGlobals extends Application {

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
