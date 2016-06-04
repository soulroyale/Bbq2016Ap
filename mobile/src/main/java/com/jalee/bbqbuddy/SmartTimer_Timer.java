package com.jalee.bbqbuddy;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Aaron on 1/02/2016.
 */
public class SmartTimer_Timer extends Fragment {

    private List<String> myData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            View v = inflater.inflate(R.layout.fragment_smart_timer_timer, container, false);
            View rootView = inflater.inflate(R.layout.fragment_smart_timer_timer, container, false);
            return v;
        } else {
            View v = inflater.inflate(R.layout.fragment_smart_timer_timer__landscape, container, false);
            View rootView = inflater.inflate(R.layout.fragment_smart_timer_timer__landscape, container, false);
            return v;
        }
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