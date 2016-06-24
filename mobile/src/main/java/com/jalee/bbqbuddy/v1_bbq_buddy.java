package com.jalee.bbqbuddy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
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
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class v1_bbq_buddy extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Boolean onTimeline = false;
    Boolean onSmartTimer = true;
    Boolean onLogs = false;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private InterstitialAd mInterstitialAd;
    final Handler handler = new Handler();
    private Boolean closingActivity = false;
    private Boolean isFabOpen = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_bbq_buddy);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        Log.i("info","pre start service");
        Intent startIntent = new Intent(v1_bbq_buddy.this, SmartTimer_Service.class);
        startIntent.setAction("Start Foreground");
        startService(startIntent);
        Log.i("info", "post start service");

        // First run alerts


        //Battery Optimisation alerts
        if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.jalee.bbqbuddy", MODE_PRIVATE);
            Boolean hidekitkatalert = sharedPreferences.getBoolean("hidekitkatalert", false);
            if (!hidekitkatalert) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("On Android KitKat (the android version you are running) Foreground Services, which BBQ Buddy relies on get exited if it is not open when the screen is turned off. To ensure your timer does not get cancelled ensure that BBQ Buddy is open before turning off your devices screen.");
                alertDialogBuilder.setTitle("Important!");
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

                alertDialogBuilder.setNegativeButton("Don't Show Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        sharedPreferences.edit().putBoolean("hidekitkatalert", true).apply();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(getPackageName())) {
                final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.jalee.bbqbuddy", MODE_PRIVATE);
                Boolean hideDozeModeAlert = sharedPreferences.getBoolean("hideDozeModeAlert", false);
                if (!hideDozeModeAlert) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Android Devices Marshmallow and higher, such as your device, have a feature called Doze mode and enhanced battery saving features. \n \nThese features can cause BBQ Buddy to exit unexpectedly.\n\nTo resolve this exclude BBQ Buddy from Battery optimisations. You can continue without completing this step but you may experience issues.");
                    alertDialogBuilder.setTitle("Important!");
                    alertDialogBuilder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

                    alertDialogBuilder.setPositiveButton("Exclude", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent();
                            String packageName = SmartTimer_Service.pubContext.getPackageName();
                            PowerManager pm = (PowerManager) SmartTimer_Service.pubContext.getSystemService(Context.POWER_SERVICE);
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (pm.isIgnoringBatteryOptimizations(packageName))
                                    intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                                else {
                                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                                    intent.setData(Uri.parse("package:" + packageName));
                                }
                            }
                            startActivity(intent);
                        }
                    });

                    alertDialogBuilder.setNeutralButton("Don't Show Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            sharedPreferences.edit().putBoolean("hideDozeModeAlert", true).apply();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

            }

        }


        if(Constants.type == Constants.Type.FREE) {

            //set padding of content view to allow for ad
            //padding in dp
            int padding_in_dp = 95;
            //convert to px
            final float scale = getResources().getDisplayMetrics().density;
            int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
            View content = findViewById(R.id.v1_content);
            //set padding
            content.setPadding(0,0,0,padding_in_px);

            AdView adView = (AdView) findViewById(R.id.adViewTimer);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("63477755EE05E10016CC8C5A71F18B64")
                    .build();
            adView.loadAd(adRequest);

            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-6523970465102586/4340248157");
            AdRequest IadRequest = new AdRequest.Builder()
                    .addTestDevice("63477755EE05E10016CC8C5A71F18B64")
                    .build();

            mInterstitialAd.loadAd(IadRequest);

        }

        final ImageView toolbaredit = (ImageView) findViewById(R.id.toolbarEdit);
        final Animation openToolbar = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        final Animation closeToolbar = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        toolbaredit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                editingStartStop();
            }
        });
       toolbaredit.startAnimation(closeToolbar);


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.v1_fab);
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.v1_fab1);
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.v1_fab2);

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
                if (SmartTimer_Service.editing) {
                    editingStartStop();
                }
                animateFAB();
                Intent intent = new Intent(getApplicationContext(), SmartTimer_TimeLine_Add.class);
                startActivity(intent);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SmartTimer_Service.editing) {
                    editingStartStop();
                }
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



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        //Setup Viewpager
        mViewPager = (ViewPager) findViewById(R.id.v1_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // if listener is set - when using an indicator, must update that here
                Animation fab360 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate360);
                Animation fab180 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate180);
                Button btnsmarttimer = (Button) findViewById(R.id.btnST);
                Button btntimeline = (Button) findViewById(R.id.btnTL);
                Button btnlog = (Button) findViewById(R.id.btnLog);


                //toolbardelete.setVisibility(View.VISIBLE);
                if (position == 0) {

                    if (SmartTimer_Service.editing) {
                        editingStartStop();
                    }
                    toolbaredit.startAnimation(closeToolbar);


                    btnsmarttimer.setTypeface(btnsmarttimer.getTypeface(), Typeface.BOLD);
                    btntimeline.setTypeface(btntimeline.getTypeface(), Typeface.ITALIC);
                    btnlog.setTypeface(btnlog.getTypeface(), Typeface.ITALIC);
                    if (!onLogs) {
                        fab.startAnimation(fab180);
                        fab.startAnimation(fab360);
                    }
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
                    onSmartTimer = true;
                    onTimeline = false;
                    onLogs = false;
                }
                if (position == 1) {
                    toolbaredit.startAnimation(openToolbar);

                    btnsmarttimer.setTypeface(btnsmarttimer.getTypeface(), Typeface.ITALIC);
                    btntimeline.setTypeface(btntimeline.getTypeface(), Typeface.BOLD);
                    btnlog.setTypeface(btnlog.getTypeface(), Typeface.ITALIC);
                    if (!onLogs) {
                        fab.startAnimation(fab180);
                        fab.startAnimation(fab360);
                    }
                    SmartTimer_TimeLine.adapter.notifyDataSetChanged();
                    fab.setImageResource(R.drawable.plus_white);
                    fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                    SmartTimer_TimeLine.fabHidden = false;
                    onTimeline = true;
                    onSmartTimer = false;
                    onLogs = false;
                }
                if (position == 2) {
                    if (SmartTimer_Service.editing) {
                        editingStartStop();
                    }
                    toolbaredit.startAnimation(closeToolbar);
                    btnsmarttimer.setTypeface(btnsmarttimer.getTypeface(), Typeface.ITALIC);
                    btntimeline.setTypeface(btntimeline.getTypeface(), Typeface.ITALIC);
                    btnlog.setTypeface(btnlog.getTypeface(), Typeface.BOLD);
                    if (!SmartTimer_TimeLine.fabHidden) {
                        SmartTimer_TimeLine.fabHidden = true;
                        fab.animate().translationY(fab.getHeight() + 180).setInterpolator(new AccelerateInterpolator(2)).start();
                    }
                    if (isFabOpen) {
                        animateFAB();
                    }
                    onTimeline = false;
                    onSmartTimer = false;
                    onLogs = true;
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

        Button btnsmarttimer = (Button) findViewById(R.id.btnST);
        btnsmarttimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
            }
        });
        Button btntimeline = (Button) findViewById(R.id.btnTL);
        btntimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);
            }
        });
        Button btnlog = (Button) findViewById(R.id.btnLog);
        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(2);
            }
        });

        //start runnable for uopdating icon and timertext
        handler.post(run);
    }

    public void editingStartStop() {
        final ImageView toolbaredit = (ImageView) findViewById(R.id.toolbarEdit);
        if (SmartTimer_Service.editing) {
            toolbaredit.setImageResource(R.drawable.ic_menu_edit_white);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }



            SmartTimer_Service.editing = false;
        } else {
            toolbaredit.setImageResource(R.drawable.ic_menu_close_clear_cancel_white);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
            }
            SmartTimer_Service.editing = true;
        }
        SmartTimer_TimeLine.adapter.notifyDataSetChanged();
    }

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            //checking if activity has started to close and if so bypassing
            if (!closingActivity) {
                try {
                    final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.v1_fab);
                    //Set play or pause on FAB
                        if (onSmartTimer) {
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
                        if (SmartTimer_Service.KeepScreenOn) {
                            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        }

                        if (txtSmartTimer != null) {
                            txtSmartTimer.setText(String.valueOf(SmartTimer_Service.timerEventsRem) + ":" + SmartTimer_Service.secondsString);
                            txtSmartTimerNext.setText(SmartTimer_Service.timerText);
                        }
                        TextView txtcurrent = (TextView) findViewById(R.id.txtCurrentEvent);
                        TextView txtuntilnext = (TextView) findViewById(R.id.txtUntilNext);
                        if (SmartTimer_Service.nextEventindex + 1 < SmartTimer_Service.TimelineList.size()) {
                            if (SmartTimer_Service.TimelineList.get(SmartTimer_Service.nextEventindex + 1).getName() != "") {
                                txtcurrent.setText(SmartTimer_Service.TimelineList.get(SmartTimer_Service.nextEventindex).getName());
                                txtuntilnext.setText(SmartTimer_Service.TimelineList.get(SmartTimer_Service.nextEventindex + 1).getName());
                            } else {
                                txtcurrent.setText("Next event starts in");
                                txtuntilnext.setText("");
                            }
                        } else {
                            txtcurrent.setText("Next event starts in");
                            txtuntilnext.setText("");
                        }
                    } else {
                        if (SmartTimer_Service.KeepScreenOn) {
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        }
                    }
                    if (!SmartTimer_Service.timerActive) {
                        SmartTimer_Service ST = new SmartTimer_Service();
                        ST.calcMinsRem();
                    }
                    Calendar estimateD = Calendar.getInstance();
                    estimateD.add(Calendar.MINUTE,SmartTimer_Service.timerEventsRem +1);
                    SimpleDateFormat estimate = new SimpleDateFormat("HH:mm");
                    String formatted = estimate.format(estimateD.getTime());
                    TextView toolbarestimate = (TextView) findViewById(R.id.toolbarEstimate);
                    String s= "Estimated Ready at\n" + formatted;
                    toolbarestimate.setText(Html.fromHtml("<small>" + "Estimated Ready at\n" + "</small>" + "<br />" + "<b>" + formatted + "</b>"));

                    //SpannableString ss1=  new SpannableString(s);
                    //ss1.setSpan(new AbsoluteSizeSpan(60), 18,ss1.length(), 0); // set size
                    //toolbarestimate.setText(ss1);






                } catch (Throwable e) {
                    //e.printStackTrace();
                }
                handler.postDelayed(this, 300);
            }
        }
    };

    public void animateFAB(){
        Animation fab_open,fab_close,rotate_forward,rotate_backward;

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.v1_fab);
        FloatingActionButton fab1 = (FloatingActionButton)findViewById(R.id.v1_fab1);
        FloatingActionButton fab2 = (FloatingActionButton)findViewById(R.id.v1_fab2);

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

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;

        }
    }

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
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{""});
            i.putExtra(Intent.EXTRA_SUBJECT, "Check Out BBQ Buddy");
            i.putExtra(Intent.EXTRA_TEXT, "Check Out BBQ Buddy, Dowload BBQ buddy from the Play Store: https://play.google.com/store/apps/details?id=com.jalee.bbqbuddy&hl=en");
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

        }  else if (id == R.id.nav_beta) {
            String url;
            if (getPackageName() == "com.jalee.bbqbuddy") {
                url = "https://play.google.com/apps/testing/com.jalee.bbqbuddy";
            } else {
                url = "https://play.google.com/apps/testing/com.jalee.bbqbuddy.paid";
            }
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            try {
                startActivity(i);
            } catch (android.content.ActivityNotFoundException ex) {

            }

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    public class SectionsPagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {

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
