package com.jalee.bbqbuddy;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import java.util.concurrent.TimeUnit;

public class SmartTimer extends AppCompatActivity {


    Boolean onTimeline = false;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private InterstitialAd mInterstitialAd;
    final Handler handler = new Handler();
    private Boolean closingActivity = false;
    private Boolean isFabOpen = false;

    //Setup runnable for updating timer text
    private Runnable run = new Runnable() {
        @Override
        public void run() {
            //checking if activity has started to close and if so bypassing

            if (!closingActivity) {
                try {
                    final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                    if (!onTimeline) {
                        if (SmartTimer_Service.timerActive) {
                            fab.setImageResource(R.drawable.ic_media_pause);
                        } else {
                            fab.setImageResource(R.drawable.ic_media_play);
                        }
                    }
                    TextView txtSmartTimer = (TextView) findViewById(R.id.txtSmartTimer);
                    TextView txtSmartTimerNext = (TextView) findViewById(R.id.txtSmartTimerNext);
                    if (SmartTimer_Service.timerComplete) {
                        txtSmartTimer.setText("0");
                        txtSmartTimerNext.setText("0");
                    }
                    if (SmartTimer_Service.timerActive) {
                        if (txtSmartTimer != null) {
                            txtSmartTimer.setText(String.valueOf(SmartTimer_Service.timerEventsRem) + ":" + SmartTimer_Service.secondsString);
                            txtSmartTimerNext.setText(SmartTimer_Service.timerText);
                        }
                        TextView txtuntilnext = (TextView) findViewById(R.id.txtUntilNext);
                        if (SmartTimer_Service.nextEventindex + 1 < SmartTimer_Service.TimelineList.size()) {
                            if (SmartTimer_Service.TimelineList.get(SmartTimer_Service.nextEventindex + 1).getName() != "") {
                                txtuntilnext.setText(SmartTimer_Service.TimelineList.get(SmartTimer_Service.nextEventindex + 1).getName() + " starts in");
                            } else {
                                txtuntilnext.setText("Next event starts in");
                            }
                        } else {
                            txtuntilnext.setText("Next event starts in");
                        }
                    } else {

                    }

                } catch (Throwable e) {
                    //e.printStackTrace();
                }
                handler.postDelayed(this, 300);
            }
        }
    };

    public void animateFAB(){
        Animation fab_open,fab_close,rotate_forward,rotate_backward;

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        FloatingActionButton fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        FloatingActionButton fab2 = (FloatingActionButton)findViewById(R.id.fab2);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);


        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            Log.d("Raj","open");

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_timer);
        overridePendingTransition(R.anim.slide_up, R.anim.stationary);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
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
                Animation fab360 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate360);
                Animation fab180 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate180);
                fab.startAnimation(fab180);
                if (position == 0) {
                    if (SmartTimer_TimeLine.fabHidden) {
                        SmartTimer_TimeLine.fabHidden = false;
                        if (SmartTimer_Service.timerActive) {
                            fab.setImageResource(R.drawable.ic_media_pause);
                        } else {
                            fab.setImageResource(R.drawable.ic_media_play);
                        }
                        fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                    } else {
                        if (SmartTimer_Service.timerActive) {
                            fab.setImageResource(R.drawable.ic_media_pause);
                        } else {
                            fab.setImageResource(R.drawable.ic_media_play);
                        }
                    }
                    if (isFabOpen) {
                        animateFAB();
                    }

                    onTimeline = false;
                }
                if (position == 1) {
                    SmartTimer_TimeLine.adapter.notifyDataSetChanged();
                    fab.setImageResource(R.drawable.plus64);
                    fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                    SmartTimer_TimeLine.fabHidden = false;
                    onTimeline = true;
                }
                if (position == 2) {
                    if (!SmartTimer_TimeLine.fabHidden) {
                        SmartTimer_TimeLine.fabHidden = true;
                        fab.animate().translationY(fab.getHeight() + 100).setInterpolator(new AccelerateInterpolator(2)).start();
                    }
                    if (isFabOpen) {
                        animateFAB();
                    }
                    onTimeline = false;
                }
                fab.startAnimation(fab360);
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

            /*
            AdView adView = (AdView) findViewById(R.id.adViewTimer);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("63477755EE05E10016CC8C5A71F18B64")
                    .build();
            adView.loadAd(adRequest);
            */

            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-6523970465102586/4340248157");
            AdRequest IadRequest = new AdRequest.Builder()
                    .addTestDevice("63477755EE05E10016CC8C5A71F18B64")
                    .build();

            mInterstitialAd.loadAd(IadRequest);

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
                    if (!SmartTimer_Service.timerActive) {
                        if (SmartTimer_Service.TimelineList.size() > 0) {
                            SmartTimer_Service.startTimer = true;
                            Log.i("info", "Set start variable to " + SmartTimer_Service.startTimer);
                        }
                    } else {
                        SmartTimer_Service.timerPaused = true;
                    }
                } else {
                    //open additional FABs
                    animateFAB();
                }
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB();
                Intent intent = new Intent(getApplicationContext(), SmartTimer_TimeLine_Add.class);
                startActivity(intent);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB();
                Intent intent = new Intent(getApplicationContext(), SmartTimer_TimeLine_Builder.class);
                startActivity(intent);
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SmartTimer_Service.timerCancel = true;
                SmartTimer_Service.smartTimerMax = TimeUnit.MINUTES.toMillis(4);
                TextView txtSmartTimer = (TextView) findViewById(R.id.txtSmartTimer);
                TextView txtSmartTimernext = (TextView) findViewById(R.id.txtSmartTimerNext);
                txtSmartTimer.setText("0");
                txtSmartTimernext.setText("0");
                return true;
            }
        });
        closingActivity = false;

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
            Intent intent = new Intent(this, SmartTimer_Settings.class);
            startActivity(intent);
            return true;
        }

        if (id == android.R.id.home) {
            Log.i("info", "Home clicked");

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
                    return SmartTimer_Timer.newInstance("");
                case 1:
                    return SmartTimer_TimeLine.newInstance("");
                case 2:
                    return SmartTimer_Notes.newInstance("");
                default:
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
                    return "Log";
            }
            return null;
        }
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
