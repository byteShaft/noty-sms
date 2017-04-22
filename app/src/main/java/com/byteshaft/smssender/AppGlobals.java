package com.byteshaft.smssender;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.firebase.FirebaseApp;

public class AppGlobals extends Application {


    public static final String SERVER_IP = "http://34.209.111.75:8080";
    public static final String BASE_URL = String.format("%s/api/", SERVER_IP);
    public static final String KEY = "key";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_FIRST_RUN = "first_run";
    private static final String SERVICE_KEY = "iServiceON";


    private static ProgressDialog progressDialog;
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

    public static void saveBoolean(boolean value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putBoolean(AppGlobals.KEY_FIRST_RUN, value).apply();
    }

    public static boolean isRunningFirstTime() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getBoolean(AppGlobals.KEY_FIRST_RUN, true);
    }

    public static void saveDataToSharedPreferences(String key, String value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static String getStringFromSharedPreferences(String key) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getString(key, "");
    }


    public static void saveState(boolean state) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putBoolean(SERVICE_KEY, state).apply();
    }

    public static boolean isServiceOn() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getBoolean(SERVICE_KEY, false);
    }

    public static void showProgressDialog(Activity activity, String message) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    public static void showSnackBar(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                .setActionTextColor(
                        AppGlobals.getContext().getResources().getColor(android.R.color.holo_red_light))
                .show();
    }
}
