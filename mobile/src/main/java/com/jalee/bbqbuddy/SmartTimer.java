package com.jalee.bbqbuddy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.concurrent.TimeUnit;

public class SmartTimer extends AppCompatActivity {

    Boolean timerActive = false;


    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static Long smartTimerMax;
    private ViewPager mViewPager;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_timer);
        overridePendingTransition(R.anim.slide_up, R.anim.stationary);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if(Constants.type == Constants.Type.FREE) {
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-6523970465102586/4340248157");
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("2ED849A00EAE479CF470A821E825E638")
                    .build();

            mInterstitialAd.loadAd(adRequest);

            /*
           AdView bannerAdView = (AdView) findViewById(R.id.adViewSmartTimer);
           AdRequest bannerAdRequest = new AdRequest.Builder()
                    .addTestDevice("2ED849A00EAE479CF470A821E825E638")
                    .build();
           bannerAdView.loadAd(bannerAdRequest);
           */

        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.type == Constants.Type.FREE) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                }
                final TextView txtSmartTimer = (TextView) findViewById(R.id.txtSmartTimer);
                if (timerActive == false) {

                    Intent intent = new Intent(getApplicationContext(),SmartTimer.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),1,intent,0);

                    Notification notification = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Timer is Active")
                            .setContentText("00:00")
                            .setContentIntent(pendingIntent)
                            .addAction(R.drawable.cookingicon512px, "View",pendingIntent)
                            .setSmallIcon(R.drawable.cookingicon512px)
                            .build();

                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(1,notification);


                    new CountDownTimer(smartTimerMax, 1000) {
                        public void onTick(long millisecondsUntilDone) {
                            //Countdown method, runs at specified interval
                            //Log.i("Seconds Left: ", String.valueOf(millisecondsUntilDone / 1000));

                            int minutes = (int) ((millisecondsUntilDone / (1000 * 60)) % 60);
                            int seconds = (int) (millisecondsUntilDone / 1000) % 60;
                            int hours =  (int)((millisecondsUntilDone /1000) / 60) / 60;


                            String secondsString;
                            String minutesString;
                            if (seconds < 10) {
                                secondsString = "0" + String.valueOf(seconds);
                            } else {
                                secondsString = String.valueOf(seconds);
                            }

                            if (minutes < 10) {
                                minutesString = "0" + String.valueOf(minutes);
                            } else {
                                minutesString = String.valueOf(minutes);
                            }

                            //Remove Minutes if no minutes left
                            if (minutes < 1) {
                                Log.i("Time left", secondsString);
                                txtSmartTimer.setText(secondsString);
                            } else {
                                Log.i("Time left", String.valueOf(hours) + ":" + minutesString + ":" + secondsString);
                                txtSmartTimer.setText(String.valueOf(hours) + ":" + minutesString + ":" + secondsString);
                            }
                        }

                        public void onFinish() {
                            //On Counter finished
                            Log.i("Done", "Done");
                            txtSmartTimer.setText("0");
                            timerActive = false;
                        }
                    }.start();
                    timerActive = true;
                    Snackbar.make(view, "Timer Started", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else {
                    Snackbar.make(view, "Timer Already Active...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_smart_timer, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

      /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            switch(position) {
                case 0:
                    //fab.show();
                    return SmartTimer_Timer.newInstance("");
                case 1:
                    //fab.hide();
                    return SmartTimer_TimeLine.newInstance("");
                case 2:
                    //fab.hide();
                    return SmartTimer_Notes.newInstance("");
                default:
                    //fab.show();
                    return SmartTimer_Timer.newInstance("");
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Smart Timer";
                case 1:
                    return "Timeline";
                case 2:
                    return "Notes";
            }
            return null;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.slide_down);
    }
}
