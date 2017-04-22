package com.byteshaft.smssender;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.byteshaft.requests.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class MainActivity extends AppCompatActivity implements
        HttpRequest.OnReadyStateChangeListener, HttpRequest.OnErrorListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Switch serviceSwitch = (Switch) findViewById(R.id.service_switch);
        showDialog();
//        if (AppGlobals.isRunningFirstTime()) {
//            showDialog();
//            AppGlobals.saveBoolean(false);
//        }
        if (AppGlobals.isServiceOn()) {
            serviceSwitch.setChecked(true);
        } else {
            serviceSwitch.setChecked(false);
        }
        serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    System.out.println("Service ON");

                } else {
                    System.out.println("Service off");

                }
                AppGlobals.saveState(b);
            }
        });
    }

    private void sendKey(String token, String name) {
        HttpRequest request = new HttpRequest(this);
        request.setOnReadyStateChangeListener(this);
        request.setOnErrorListener(this);
        request.open("POST", String.format("%skey", AppGlobals.BASE_URL));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppGlobals.KEY, token);
            jsonObject.put("username", name);          // TODO: 22/04/2017 Set username accordingly
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.send(jsonObject.toString());
        AppGlobals.showProgressDialog(MainActivity.this, "Please wait...");
    }

    @Override
    public void onReadyStateChange(HttpRequest request, int readyState) {

        switch (readyState) {
            case HttpRequest.STATE_DONE:
                AppGlobals.dismissProgressDialog();
                switch (request.getStatus()) {
                    case HttpURLConnection.HTTP_CREATED:
                        AppGlobals.showSnackBar(findViewById(android.R.id.content),
                                "Device Registered");
                        Log.e("Created ", "Created  !");
                }
        }
    }

    @Override
    public void onError(HttpRequest request, int readyState, short error, Exception exception) {

    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final EditText nameField = new EditText(MainActivity.this);
        builder.setTitle("Welcome!");
        builder.setCancelable(false);
        builder.setMessage("Please provide your name");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        sendKey(AppGlobals.getStringFromSharedPreferences(
                                AppGlobals.KEY_TOKEN), nameField.getText().toString());
                    }
                });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        nameField.setHint("Enter Your Name here...");
        nameField.setLayoutParams(lp);
        builder.setView(nameField);
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Initially disable the button
        (dialog).getButton(AlertDialog.BUTTON_POSITIVE)
                .setEnabled(false);
        // OR you can use here setOnShowListener to disable button at first time.

        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    (dialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    // Something into edit text. Enable the button.
                    (dialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }

            }
        });
    }
}
