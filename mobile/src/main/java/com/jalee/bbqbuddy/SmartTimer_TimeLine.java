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
public class SmartTimer_TimeLine extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_smart_timer_timeline, container, false);

        TextView tv = (TextView) v.findViewById(R.id.tvFragFirst);
        //tv.setText(getArguments().getString("msg"));

        return v;
    }

    public static SmartTimer_TimeLine newInstance(String text) {

        SmartTimer_TimeLine f = new SmartTimer_TimeLine();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}
