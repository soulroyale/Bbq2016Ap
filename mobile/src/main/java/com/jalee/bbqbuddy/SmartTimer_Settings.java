package com.jalee.bbqbuddy;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SmartTimer_Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_timer__settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        overridePendingTransition(R.anim.slide_up, R.anim.stationary);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int notiExtend = 1;
        try {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.jalee.bbqbuddy", MODE_PRIVATE);
            notiExtend = sharedPreferences.getInt("notiExtendInterval", 1);
        } catch(NumberFormatException nfe) {
            notiExtend = 1;
        }
        TextView txtNotiInt = (TextView) findViewById(R.id.txtNotiExtend);
        txtNotiInt.setText(String.valueOf(notiExtend));

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView txtNotiInt = (TextView) findViewById(R.id.txtNotiExtend);
                int myNum = 1;
                try {
                    myNum = Integer.parseInt(txtNotiInt.getText().toString());
                } catch(NumberFormatException nfe) {
                    myNum = 1;
                }
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.jalee.bbqbuddy", MODE_PRIVATE);
                sharedPreferences.edit().putInt("notiExtendInterval", myNum).apply();
                finish();
            }
        });
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.slide_down);
    }
}

