package com.jalee.bbqbuddy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Aaron on 1/02/2016.
 */
public class SmartTimer_Timer extends Fragment {

    private List<String> myData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_smart_timer_timer, container, false);
        View rootView = inflater.inflate(R.layout.fragment_smart_timer_timer, container, false);
        AdView bannerAdView = (AdView) rootView.findViewById(R.id.adViewTimer);
        AdRequest bannerAdRequest = new AdRequest.Builder()
                .addTestDevice("4183AA4B7DBD269E1F1D0D51DF9FB52B")
                .build();
        bannerAdView.loadAd(bannerAdRequest);
        return v;
    }


    public static SmartTimer_Timer newInstance(String text) {

        SmartTimer_Timer f = new SmartTimer_Timer();
        Bundle b = new Bundle();
        //b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("list", (Serializable) myData);
    }


}