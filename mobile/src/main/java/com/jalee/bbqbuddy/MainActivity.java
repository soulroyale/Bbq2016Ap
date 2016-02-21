package com.jalee.bbqbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    CardAdapter adapter;
    public static List<cardUI> cardUIList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startService(new Intent(getApplicationContext(), SmartTimer_Service.class));
        Log.i("Info", "SmartTimer service started");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SmartTimer.class);
                startActivity(intent);

                // Snackbar.make(view, "Feature coming soon!", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!SmartTimer_Service.timerActive) {
                    SmartTimer_Service.startTimer =true;
                    Intent intent = new Intent(getApplicationContext(), SmartTimer.class);
                    startActivity(intent);
                }
                return true;
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
                    .addTestDevice("63477755EE05E10016CC8C5A71F18B64")
                    .build();
            adView.loadAd(adRequest);
        }
        if (SmartTimer_Service.timerActive) {
            Intent intent = new Intent(this, SmartTimer.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //If Timer already active, display it
        if (SmartTimer_Service.timerActive) {
            Intent reloadIntent = new Intent(this, SmartTimer.class);
            startActivity(reloadIntent);
        }
    }

    private void initializeData() {
        cardUIList = new ArrayList<>();
        cardUIList.add(new cardUI("Thankyou for trying BBQ buddy, BBQ buddy has been developed to deliver all the features that are currently missing from existing BBQ companion Apps. \n \nIf you have any suggestions for the app please forward them to support@jalee-dev.com.au \n \nI hope you enjoy BBQ Buddy, \nAaron  \n \n \n \n \n \n \n \n test data For Scroll test", "Introducing BBQ Buddy", R.drawable.sample));
        cardUIList.add(new cardUI("About 20 minutes before grilling, remove the steaks from the refrigerator and let sit, covered, at room temperature. \n\nHeat your grill to high. Brush the steaks on both sides with oil and season liberally with salt and pepper.\nPlace the steaks on the grill and cook until golden brown and slightly charred, 4 to 5 minutes. Turn the steaks over and continue to grill:\n\n3 to 5 minutes for medium-rare (an internal temperature of 57 degrees Celsius or 135 degrees F)\n5 to 7 minutes for medium (60 degrees Celsius or 140 degrees F)\n8 to 10 minutes for medium-well (65 degrees Celsius or 150 degrees F)\n\nTransfer the steaks to a cutting board or platter, tent loosely with foil and let rest 5 minutes before slicing.", "Medium Rare Steak", R.raw.steak));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
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

    public void finish() {
        //remove handler for updating timer text

        super.finish();
    }
}
