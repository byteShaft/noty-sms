package com.byteshaft.smssender;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private Switch serviceSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceSwitch = (Switch) findViewById(R.id.service_switch);
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

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("Welcome!");
        builder.setCancelable(false);
        builder.setMessage("Please provide your name");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        // DO TASK
                    }
                });
// Set `EditText` to `dialog`. You can add `EditText` from `xml` too.
        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setHint("Enter Your Name here...");
        input.setLayoutParams(lp);
        builder.setView(input);
        final AlertDialog dialog = builder.create();
        dialog.show();
// Initially disable the button
        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE)
                .setEnabled(false);
// OR you can use here setOnShowListener to disable button at first
// time.

// Now set the textchange listener for edittext
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Check if edittext is empty
                if (TextUtils.isEmpty(s)) {
                    // Disable ok button
                    ((AlertDialog) dialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    // Something into edit text. Enable the button.
                    ((AlertDialog) dialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }

            }
        });
    }
}
