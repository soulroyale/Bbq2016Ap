package com.jalee.bbqbuddy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class v1_bbq_buddy extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final Handler handler = new Handler();
    private Boolean closingActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_bbq_buddy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.i("info","pre start service");
        Intent startIntent = new Intent(v1_bbq_buddy.this, SmartTimer_Service.class);
        startIntent.setAction("Start Foreground");
        startService(startIntent);
        Log.i("info", "post start service");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.v1_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SmartTimer_Service.timerActive) {
                    if (SmartTimer_Service.TimelineList.size() > 0) {
                        SmartTimer_Service.startTimer = true;
                        Log.i("info", "Set start variable to " + SmartTimer_Service.startTimer);
                    }
                } else {
                    SmartTimer_Service.timerPaused = true;
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //start runnable for uopdating icon and timertext
        handler.post(run);
    }


    private Runnable run = new Runnable() {
        @Override
        public void run() {
            //checking if activity has started to close and if so bypassing
            if (!closingActivity) {
                try {
                    final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.v1_fab);
                        if (SmartTimer_Service.timerActive) {
                            fab.setImageResource(R.drawable.ic_media_pause);
                        } else {
                            fab.setImageResource(R.drawable.ic_media_play);
                        }

                } catch (Throwable e) {

                }
                handler.postDelayed(this, 300);
            }
        }
    };




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Would you like to exit BBQ Buddy?");

            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Log.i("Selected", "You salected yes");
                    Intent stopIntent = new Intent(v1_bbq_buddy.this, SmartTimer_Service.class);
                    stopIntent.setAction("stop");
                    startService(stopIntent);
                    finish();
                }
            });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("Selected", "You salected no");
                }

            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.v1_bbq_buddy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, SmartTimer_Settings.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void finish() {
        //remove handler for updating timer text
        closingActivity = true;
        handler.removeCallbacksAndMessages(run);
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.slide_down);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closingActivity = true;
        handler.removeCallbacksAndMessages(run);
    }
}
