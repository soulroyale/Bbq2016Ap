package com.jalee.bbqbuddy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Aaron on 1/02/2016.
 */
public class SmartTimer_Notes extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_smart_timer_notes, container, false);

        TextView tv = (TextView) v.findViewById(R.id.tvFragSecond);
        //tv.setText(getArguments().getString("msg"));

        return v;
    }

    public static SmartTimer_Notes newInstance(String text) {

        SmartTimer_Notes f = new SmartTimer_Notes();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}