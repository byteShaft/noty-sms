package com.byteshaft.smssender;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

    private static final int SMS_PERMISSION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Switch serviceSwitch = (Switch) findViewById(R.id.service_switch);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSION);

        } else {
            if (AppGlobals.isRunningFirstTime()) {
                showDialog();
            }
            if (AppGlobals.isServiceOn()) {
                serviceSwitch.setChecked(true);
            } else {
                serviceSwitch.setChecked(false);
            }
            serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        serviceSwitch.setText("Service Enabled");
                        serviceSwitch.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

                    } else {

                        serviceSwitch.setText("Service Disabled");
                        serviceSwitch.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    }
                    AppGlobals.saveState(b);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);switch (requestCode) {
            case SMS_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(findViewById(android.R.id.content), "permission granted!",
                            Snackbar.LENGTH_SHORT).show();
                    if (AppGlobals.isRunningFirstTime()) {
                        showDialog();
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void sendKey(String token, String name) {
        HttpRequest request = new HttpRequest(this);
        request.setOnReadyStateChangeListener(this);
        request.setOnErrorListener(this);
        request.open("POST", String.format("%skey", AppGlobals.BASE_URL));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppGlobals.KEY, token);
            jsonObject.put("full_name", name);
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
                        break;
                    case HttpURLConnection.HTTP_OK:
                        Log.i("TAG", request.getResponseText());
                        break;
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
                        AppGlobals.saveBoolean(false);
                    }
                });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        nameField.setHint("Enter Your Full Name here...");
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
