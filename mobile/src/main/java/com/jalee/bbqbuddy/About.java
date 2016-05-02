package com.jalee.bbqbuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class About extends AppCompatActivity {

    public void closeMe (View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        overridePendingTransition(R.anim.slide_up,R.anim.stationary);
        setTitle("About");

        Button btnCHange = (Button) findViewById(R.id.btnChange);
        btnCHange.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchChangeLog();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.slide_down);
    }

    public void launchChangeLog() {
        Intent intent = new Intent(this, SmartTimer_Changelog.class);
        startActivity(intent);
    }
}
