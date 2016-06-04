package com.jalee.bbqbuddy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class SmartTimer_Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_timer__settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        overridePendingTransition(R.anim.slide_inright, R.anim.stationary);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.jalee.bbqbuddy", MODE_PRIVATE);
        int notiExtend = 1;
        try {
            notiExtend = sharedPreferences.getInt("notiExtendInterval", 1);
        } catch(NumberFormatException nfe) {
            notiExtend = 1;
        }
        TextView txtNotiInt = (TextView) findViewById(R.id.txtNotiExtend);
        txtNotiInt.setText(String.valueOf(notiExtend));
        CheckBox chkextranoti,chkscreenon,chkautopause,chktimelineincrease;


        chktimelineincrease =(CheckBox)findViewById(R.id.chkTimelineIncrease);
        try {
            if (sharedPreferences.getBoolean("TimelineCanIncrease", true)) {
                chktimelineincrease.setChecked(true);
            } else {
                chktimelineincrease.setChecked(false);
            }
        } catch(NumberFormatException nfe) {

        }

        chkautopause =(CheckBox)findViewById(R.id.chkAutoPause);
        try {
            if (sharedPreferences.getBoolean("timerAutoPause", true)) {
                chkautopause.setChecked(true);
            } else {
                chkautopause.setChecked(false);
            }
        } catch(NumberFormatException nfe) {

        }

        chkscreenon =(CheckBox)findViewById(R.id.chkScreenOn);
        try {
            if (sharedPreferences.getBoolean("KeepScreenOn", true)) {
                chkscreenon.setChecked(true);
            } else
                chkscreenon.setChecked(false);
        } catch(NumberFormatException nfe) {

        }

        chkextranoti =(CheckBox)findViewById(R.id.chkExtraNoti);
        try {
            if (sharedPreferences.getBoolean("ExtraNotification", true)) {
                chkextranoti.setChecked(true);
            } else
                chkextranoti.setChecked(false);
        } catch(NumberFormatException nfe) {

        }
    }

    public void saveSettings() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.jalee.bbqbuddy", MODE_PRIVATE);

        TextView txtNotiInt = (TextView) findViewById(R.id.txtNotiExtend);
        int myNum = 1;
        try {
            myNum = Integer.parseInt(txtNotiInt.getText().toString());
        } catch(NumberFormatException nfe) {
            myNum = 1;
        }

        sharedPreferences.edit().putInt("notiExtendInterval", myNum).apply();

        CheckBox chkextranoti,chkscreenon,chkautopause,chktimelineincrease;


        chktimelineincrease =(CheckBox)findViewById(R.id.chkTimelineIncrease);
        if (chktimelineincrease.isChecked()) {
            sharedPreferences.edit().putBoolean("TimelineCanIncrease", true).apply();
            SmartTimer_Service.timelineCanIncrease = true;
        } else {
            sharedPreferences.edit().putBoolean("TimelineCanIncrease", false).apply();
            SmartTimer_Service.timelineCanIncrease = false;
        }

        chkautopause =(CheckBox)findViewById(R.id.chkAutoPause);
        if (chkautopause.isChecked()) {
            sharedPreferences.edit().putBoolean("timerAutoPause", true).apply();
            SmartTimer_Service.timerAutoPause = true;
        } else {
            sharedPreferences.edit().putBoolean("timerAutoPause", false).apply();
            SmartTimer_Service.timerAutoPause = false;
        }

        chkscreenon =(CheckBox)findViewById(R.id.chkScreenOn);
        if (chkscreenon.isChecked()) {
            sharedPreferences.edit().putBoolean("KeepScreenOn", true).apply();
            SmartTimer_Service.KeepScreenOn = true;
        } else {
            sharedPreferences.edit().putBoolean("KeepScreenOn", false).apply();
            SmartTimer_Service.KeepScreenOn = false;
        }

        chkextranoti =(CheckBox)findViewById(R.id.chkExtraNoti);
        if (chkextranoti.isChecked()) {
            sharedPreferences.edit().putBoolean("ExtraNotification", true).apply();
            SmartTimer_Service.ExtraNotification = true;
        } else {
            sharedPreferences.edit().putBoolean("ExtraNotification", false).apply();
            SmartTimer_Service.ExtraNotification = false;
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            //save settings when home pressed
            saveSettings();
            overridePendingTransition(R.anim.stationary, R.anim.slide_outright);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        //save settings when back pressed
        saveSettings();
        overridePendingTransition(R.anim.stationary, R.anim.slide_outright);
        super.finish();
    }
}

