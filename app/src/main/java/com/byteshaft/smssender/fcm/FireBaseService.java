package com.byteshaft.smssender.fcm;

import android.util.Log;

import com.byteshaft.requests.HttpRequest;
import com.byteshaft.smssender.AppGlobals;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class FireBaseService extends FirebaseInstanceIdService implements
        HttpRequest.OnReadyStateChangeListener, HttpRequest.OnErrorListener {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_TOKEN, token);
        Log.e("TAG", "Token " + token);
        sendKey(token);
    }

    private void sendKey(String token) {
        HttpRequest request = new HttpRequest(this);
        request.setOnReadyStateChangeListener(this);
        request.setOnErrorListener(this);
        request.open("POST", String.format("%skey", AppGlobals.BASE_URL));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppGlobals.KEY, token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.send(jsonObject.toString());
    }

    @Override
    public void onReadyStateChange(HttpRequest request, int readyState) {
        switch (readyState) {
            case HttpRequest.STATE_DONE:
                switch (request.getStatus()) {
                    case HttpURLConnection.HTTP_CREATED:
                        Log.e("Created ", "ok kr k");
                }
        }
    }

    @Override
    public void onError(HttpRequest request, int readyState, short error, Exception exception) {

    }
}
