package com.jalee.bbqbuddy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.internal.request.StringParcel;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class SmartTimer extends AppCompatActivity {


    Boolean onTimeline = false;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private InterstitialAd mInterstitialAd;
    final Handler handler = new Handler();

    //Setup runable for updating timer text
    private Runnable run = new Runnable() {
        @Override
        public void run() {
            final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            if (!onTimeline) {
                if (MainActivity.timerActive) {
                    fab.setImageResource(R.drawable.ic_media_pause);
                } else {
                    fab.setImageResource(R.drawable.ic_media_play);
                }
            }
            TextView txtSmartTimer = (TextView) findViewById(R.id.txtSmartTimer);
            TextView txtSmartTimerNext = (TextView) findViewById(R.id.txtSmartTimerNext);
            if (MainActivity.timerComplete == true) {
                txtSmartTimer.setText("0");
                txtSmartTimerNext.setText("0");
            }
            if (MainActivity.timerActive) {
                if (txtSmartTimer != null) {
                    txtSmartTimer.setText(MainActivity.timerText);
                    if (MainActivity.minsRemaining < 1) {
                        txtSmartTimerNext.setText(MainActivity.secondsString);
                    } else {
                        txtSmartTimerNext.setText(String.valueOf(MainActivity.minsRemaining) + ":" + MainActivity.secondsString);
                    }
                }
            }else {
                if (MainActivity.timerPaused) {
                    txtSmartTimer.setText("0");
                    txtSmartTimerNext.setText("0");
                }
            }
            handler.postDelayed(this, 500);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_timer);
        overridePendingTransition(R.anim.slide_up, R.anim.stationary);



        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Start Runable for updating timerText
        handler.post(run);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // if listener is set - when using an indicator, must update that here
                if (position == 0) {
                    if (SmartTimer_TimeLine.fabHidden) {
                        SmartTimer_TimeLine.fabHidden = false;
                        if (MainActivity.timerActive) {
                            fab.setImageResource(R.drawable.ic_media_pause);
                        } else {
                            fab.setImageResource(R.drawable.ic_media_play);
                        }
                        fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                    } else {
                        if (MainActivity.timerActive) {
                            fab.setImageResource(R.drawable.ic_media_pause);
                        } else {
                            fab.setImageResource(R.drawable.ic_media_play);
                        }
                    }
                    onTimeline = false;
                }
                if (position == 1) {
                    fab.setImageResource(R.drawable.plus64);
                    fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                    onTimeline = true;
                }
                if (position == 2) {
                    if (!SmartTimer_TimeLine.fabHidden) {
                        SmartTimer_TimeLine.fabHidden = true;
                        fab.animate().translationY(fab.getHeight() + 70).setInterpolator(new AccelerateInterpolator(2)).start();
                    }
                    onTimeline = true;
                }
                String msg = "onPageSelected - position: " + position;
                Log.i("debug1", "Page = " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int position, float arg1, int arg2) {
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if(Constants.type == Constants.Type.FREE) {
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-6523970465102586/4340248157");
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("4183AA4B7DBD269E1F1D0D51DF9FB52B")
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!onTimeline) {
                    if (Constants.type == Constants.Type.FREE) {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }
                    }

                    final TextView txtSmartTimer = (TextView) findViewById(R.id.txtSmartTimer);
                    MainActivity mainActivity = new MainActivity();
                    if (!MainActivity.timerActive) {
                        mainActivity.SmartTimerFunc(getApplicationContext());
                    } else {
                        mainActivity.timerPaused = true;
                    }
                } else {
                    if (MainActivity.timerActive == true) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SmartTimer.this);
                        alertDialogBuilder.setMessage("Cannot currently edit timeline while timer is active");
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else {
                        //Launch add activity
                        Intent intent = new Intent(getApplicationContext(), SmartTimer_TimeLine_Add.class);
                        startActivity(intent);
                    }
                }
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MainActivity.timerCancel = true;
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancel(1);
                MainActivity.smartTimerMax = TimeUnit.MINUTES.toMillis(4);
                TextView txtSmartTimer = (TextView) findViewById(R.id.txtSmartTimer);
                TextView txtSmartTimernext = (TextView) findViewById(R.id.txtSmartTimerNext);
                txtSmartTimer.setText("0");
                txtSmartTimernext.setText("0");
                return true;
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
        //remove handler for updating timer text
        handler.removeCallbacks(run);
        super.finish();

        overridePendingTransition(R.anim.stationary, R.anim.slide_down);
    }
}
