package com.jalee.bbqbuddy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    CardAdapter adapter;
    public static List<cardUI> cardUIList;
    public static Long smartTimerMax;
    public static List<SmartTimer_cardUI> TimelineList;
    public static Boolean timerActive = false;
    public static Boolean timerPaused = false;
    public static Boolean timerCancel = false;
    public static Boolean timerComplete = false;
    public static String timerText = "0";
    public static Integer nextEventindex = 0;
    public static Long minsRemaining = 0L;
    public static Long minRemainingElapsed = 0L;
    public static String secondsString;
    public static String minutesString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialise timeline data

        TimelineList = new ArrayList<>();

        TimelineList.add(new SmartTimer_cardUI("Have a drink, preferably a James Squire 150 lashes","Drink Beer",2));
        TimelineList.add(new SmartTimer_cardUI("Cook on BBQ on Medium heat for 7 Minutes","Cook Steak - Side 1",7));
        TimelineList.add(new SmartTimer_cardUI("Cook on BBQ on Medium heat for 7 Minutes","Cook Steak - Side 2",7));
        TimelineList.add(new SmartTimer_cardUI("Have a drink, preferably a James Squire 150 lashes","Drink Beer",2));
        TimelineList.add(new SmartTimer_cardUI("Let your meat rest, its tired","Rest Meat",5));
        TimelineList.add(new SmartTimer_cardUI("Have a drink, preferably a James Squire 150 lashes","Drink Beer",10));

        Integer newSmartTimerValue = 0;
        for (int i = 0; i < MainActivity.TimelineList.size(); i++) {
            newSmartTimerValue = newSmartTimerValue + (Integer) MainActivity.TimelineList.get(i).id;
        }
        MainActivity.smartTimerMax = TimeUnit.MINUTES.toMillis(newSmartTimerValue);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchTimer();

                // Snackbar.make(view, "Feature coming soon!", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        recyclerView=(RecyclerView)findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        initializeData();
        adapter = new CardAdapter(cardUIList);
        recyclerView.setAdapter(adapter);

        if(Constants.type == Constants.Type.FREE) {
            AdView adView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("910E556A53EAB4998843E3E84C3F313F")
                    .build();
            adView.loadAd(adRequest);
        }
    }

    private void launchTimer() {
        Intent intent = new Intent(this, SmartTimer.class);
        startActivity(intent);
    }

    private void initializeData() {
        cardUIList = new ArrayList<>();
        cardUIList.add(new cardUI("Thankyou for trying BBQ buddy, BBQ buddy has been developed to deliver all the features that are currently missing from existing BBQ companion Apps. \n \nIf you have any suggestions for the app please forward them to support@jalee-dev.com.au \n \nI hope you enjoy BBQ Buddy, \nAaron  ", "Introducing BBQ Buddy", R.drawable.sample));

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
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

        if (id == R.id.nav_dash) {


        } else if (id == R.id.nav_recipes) {
            Toast.makeText(getApplicationContext(),"Feature Coming Soon!",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_history) {
            Toast.makeText(getApplicationContext(),"Feature Coming Soon!",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_manage) {
            Toast.makeText(getApplicationContext(),"Feature Coming Soon!",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{""});
            i.putExtra(Intent.EXTRA_SUBJECT, "Check Out BBQ Buddy");
            i.putExtra(Intent.EXTRA_TEXT, "Check Out BBQ Buddy, Join the below Google Plus group to gain access to the Alpha or Beta Realese of BBQ Buddy: https://plus.google.com/u/0/communities/111930892923034410128");
            try {
                startActivity(Intent.createChooser(i, "Share BBQ Buddy:"));
            } catch (android.content.ActivityNotFoundException ex) {

            }
        } else if (id == R.id.nav_send) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"support@jalee-dev.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "BBQ Buddy Feedback");
            i.putExtra(Intent.EXTRA_TEXT, "I tried BBQ Buddy and have the following thoughts:");
            try {
                startActivity(Intent.createChooser(i, "BBQ Buddy Feedback:"));
            } catch (android.content.ActivityNotFoundException ex) {

            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void SmartTimerFunc(final Context context){
        if (timerActive == false) {
            timerComplete =false;
            new CountDownTimer(smartTimerMax, 1000) {
                public void onTick(long millisecondsUntilDone) {

                    Long timerEventsElapsTotal = 0L;
                    Integer timerEventsElapsTotalint = 0;
                    for (int i = 0; i < TimelineList.size(); i++) {
                        if (i <= nextEventindex) {
                            timerEventsElapsTotalint = timerEventsElapsTotalint + (Integer) TimelineList.get(i).id;
                        }
                    }
                    timerEventsElapsTotal = TimeUnit.MINUTES.toMillis(timerEventsElapsTotalint);

                    //Retrieve hours Minutes seconds remaining
                    int minutes = (int) ((millisecondsUntilDone / (1000 * 60)) % 60);
                    int seconds = (int) (millisecondsUntilDone / 1000) % 60;
                    int hours = (int) ((millisecondsUntilDone / 1000) / 60) / 60;

                    //Check next event
                    minsRemaining = (((TimelineList.get(nextEventindex).getId()) + minRemainingElapsed) - (TimeUnit.MILLISECONDS.toMinutes(smartTimerMax) - TimeUnit.MILLISECONDS.toMinutes(millisecondsUntilDone)));

                    //next near next event
                    if (TimelineList.get(nextEventindex).getId() == (TimeUnit.MILLISECONDS.toMinutes(millisecondsUntilDone)) - 1) {
                        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        if (seconds == 1) {
                            if (vibrator.hasVibrator()) {
                                int dot = 200;      // Length of a Morse Code "dot" in milliseconds
                                int dash = 500;     // Length of a Morse Code "dash" in milliseconds
                                int short_gap = 200;    // Length of Gap Between dots/dashes
                                int medium_gap = 500;   // Length of Gap Between Letters
                                int long_gap = 1000;    // Length of Gap Between Words
                                long[] pattern = {
                                        0,  // Start immediately
                                        dot, dot
                                };
                                vibrator.vibrate(pattern, -1);
                            }
                        }
                    }

                    //next event occured


                    if (TimeUnit.MILLISECONDS.toMinutes((smartTimerMax - millisecondsUntilDone) +1) == timerEventsElapsTotalint) {
                        Log.i("Info","Mins reached");
                        if (seconds == 0) {
                            minRemainingElapsed = minRemainingElapsed + TimelineList.get(nextEventindex).getId();
                            nextEventindex = nextEventindex + 1;
                            Log.i("Info","Increase Next event index");
                            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                            if (vibrator.hasVibrator()) {
                                if (vibrator.hasVibrator()) {
                                    int dot = 200;      // Length of a Morse Code "dot" in milliseconds
                                    int dash = 1000;     // Length of a Morse Code "dash" in milliseconds
                                    int short_gap = 200;    // Length of Gap Between dots/dashes
                                    int medium_gap = 500;   // Length of Gap Between Letters
                                    int long_gap = 1000;    // Length of Gap Between Words
                                    long[] pattern = {
                                            0,  // Start immediately
                                            dash, short_gap, dash
                                    };
                                    vibrator.vibrate(pattern, -1);
                                }
                            }
                        }
                    }
                    Log.i("Info",String.valueOf(TimeUnit.MILLISECONDS.toMinutes(smartTimerMax - millisecondsUntilDone)));
                    Log.i("Info",String.valueOf(timerEventsElapsTotalint));
                    if (seconds < 10) {
                        secondsString = "0" + String.valueOf(seconds);
                    } else {
                        secondsString = String.valueOf(seconds);
                    }

                    minutesString = String.valueOf(minutes);

                    //Remove Minutes if no minutes left
                    if (minutes < 1) {
                        timerText = secondsString;

                    } else {
                        if (hours < 1) {
                            timerText = minutesString + ":" + secondsString;
                        } else {
                            timerText = String.valueOf(hours) + ":" + minutesString + ":" + secondsString;
                        }
                    }


                    //Update Notification
                    Intent intent = new Intent(context, SmartTimer.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, 0);
                    PendingIntent pauseIntent = PendingIntent.getActivity(context, 2, intent, 0);
                    PendingIntent playIntent = PendingIntent.getActivity(context, 3, intent, 0);
                    PendingIntent cancelIntent = PendingIntent.getActivity(context, 4, intent, 0);

                    int notiColour = context.getResources().getColor(R.color.colorPrimary);

                    if (nextEventindex + 1 == TimelineList.size()) {
                        if (timerPaused) {
                            Notification timerNotification = new Notification.Builder(context)
                                    .setContentTitle("BBQ Buddy - Smart Timer")
                                    .addAction(R.drawable.ic_media_play, "Play", pauseIntent)
                                    .addAction(R.drawable.places_ic_clear, "Cancel", cancelIntent)
                                    .setContentText(String.valueOf(minsRemaining) + ":" + secondsString + " until finished")
                                    .setContentIntent(pendingIntent)
                                    .setOngoing(true)
                                    .setSmallIcon(R.drawable.cookingicon512px)
                                    .setColor(notiColour)
                                    .build();
                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.notify(1, timerNotification);
                        } else {
                            Notification timerNotification = new Notification.Builder(context)
                                    .setContentTitle("BBQ Buddy - Smart Timer")
                                    .addAction(R.drawable.ic_media_pause, "Pause", playIntent)
                                    .addAction(R.drawable.places_ic_clear, "Cancel", cancelIntent)
                                    .setContentText(String.valueOf(minsRemaining) + ":" + secondsString + " until finished")
                                    .setContentIntent(pendingIntent)
                                    .setOngoing(true)
                                    .setSmallIcon(R.drawable.cookingicon512px)
                                    .setColor(notiColour)
                                    .build();
                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.notify(1, timerNotification);
                        }

                    } else {
                        if (timerPaused) {
                            Notification timerNotification = new Notification.Builder(context)
                                    .setContentTitle("BBQ Buddy - Smart Timer")
                                    .addAction(R.drawable.ic_media_play, "Play", pauseIntent)
                                    .addAction(R.drawable.places_ic_clear, "Cancel", cancelIntent)
                                    .setContentText(String.valueOf(minsRemaining) + ":" + secondsString + " " + TimelineList.get(nextEventindex + 1).getName())
                                    .setContentIntent(pendingIntent)
                                    .setOngoing(true)
                                    .setSmallIcon(R.drawable.cookingicon512px)
                                    .setColor(notiColour)
                                    .build();
                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.notify(1, timerNotification);
                        } else {
                            Notification timerNotification = new Notification.Builder(context)
                                    .setContentTitle("BBQ Buddy - Smart Timer")
                                    .addAction(R.drawable.ic_media_pause, "Pause", playIntent)
                                    .addAction(R.drawable.places_ic_clear, "Cancel", cancelIntent)
                                    .setContentText(String.valueOf(minsRemaining) + ":" + secondsString + " " + TimelineList.get(nextEventindex + 1).getName())
                                    .setContentIntent(pendingIntent)
                                    .setOngoing(true)
                                    .setSmallIcon(R.drawable.cookingicon512px)
                                    .setColor(notiColour)
                                    .build();
                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.notify(1, timerNotification);
                        }
                    }

                    if (timerPaused == true) {
                        timerActive = false;
                        timerPaused = false;
                        smartTimerMax = smartTimerMax - (smartTimerMax - millisecondsUntilDone);
                        cancel();

                    }
                    if (timerCancel == true) {
                        timerActive = false;
                        timerPaused = false;
                        timerCancel = false;
                        nextEventindex = 0;
                        minsRemaining = 0L;
                        minRemainingElapsed = 0L;
                        cancel();
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.cancel(1);
                    }
                }

                public void onFinish() {
                    //On Counter finished
                    timerActive = false;
                    timerPaused = false;
                    timerCancel = false;
                    nextEventindex = 0;
                    minsRemaining = 0L;
                    minRemainingElapsed = 0L;
                    timerComplete = true;

                    Intent intent = new Intent(context, SmartTimer.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, 0);
                    int notiColour = context.getResources().getColor(R.color.colorPrimary);
                    Notification timerNotification = new Notification.Builder(context)
                            .setContentTitle("BBQ Buddy")
                            .setContentText("Your Smart Timer timeline has completed")
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setOngoing(false)
                            .setSmallIcon(R.drawable.cookingicon512px)
                            .setColor(notiColour)
                            .build();

                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(1, timerNotification);
                    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibrator.hasVibrator()) {
                        if (vibrator.hasVibrator()) {
                            int dot = 200;      // Length of a Morse Code "dot" in milliseconds
                            int dash = 1000;     // Length of a Morse Code "dash" in milliseconds
                            int short_gap = 200;    // Length of Gap Between dots/dashes
                            int medium_gap = 500;   // Length of Gap Between Letters
                            int long_gap = 1000;    // Length of Gap Between Words
                            long[] pattern = {
                                    0,  // Start immediately
                                    dash, short_gap, dash
                            };
                            vibrator.vibrate(pattern,-1);
                        }
                    }
                }
            }.start();
            timerActive = true;

        } else {

        }
    }

    public void finish() {
        //remove handler for updating timer text

        

        super.finish();
    }
}
