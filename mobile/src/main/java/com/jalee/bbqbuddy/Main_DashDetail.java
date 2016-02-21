package com.jalee.bbqbuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Main_DashDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__dash_detail);
        overridePendingTransition(R.anim.slide_inright, R.anim.slide_outleft);
        setTitle(MainActivity.cardUIList.get(CardAdapter.cardIndex).getName());
        TextView content = (TextView) findViewById(R.id.txtContent);
        content.setText((MainActivity.cardUIList.get(CardAdapter.cardIndex).subTitle) + "\n\n\n");
        ImageView image = (ImageView) findViewById(R.id.bannerImage);
        image.setImageResource(MainActivity.cardUIList.get(CardAdapter.cardIndex).id);
        ScrollView sv = (ScrollView) findViewById(R.id.dashDetailScrollView);
        sv.setVerticalScrollBarEnabled(false);
        sv.setHorizontalScrollBarEnabled(false);
        if(Constants.type == Constants.Type.FREE) {
            AdView adView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("63477755EE05E10016CC8C5A71F18B64")
                    .build();
            adView.loadAd(adRequest);
        } else {

        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_inleft, R.anim.slide_outright);
    }
}
