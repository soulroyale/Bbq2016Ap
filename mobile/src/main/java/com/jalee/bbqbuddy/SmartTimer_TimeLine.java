package com.jalee.bbqbuddy;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron on 1/02/2016.
 */
public class SmartTimer_TimeLine extends Fragment {

    public static List<SmartTimer_cardUI> TimelineList;
    RecyclerView recyclerView;
    SmartTimer_CardAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_smart_timer_timeline, container, false);

        //TextView tv = (TextView) v.findViewById(R.id.tvFragFirst);
        //tv.setText(getArguments().getString("msg"));

        recyclerView=(RecyclerView) v.findViewById(R.id.smarttimer_timeline_cardrecycler);
        LinearLayoutManager linman = new LinearLayoutManager(null);
        linman.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linman);
        recyclerView.setHasFixedSize(true);
        initializeData();
        adapter = new SmartTimer_CardAdapter(TimelineList);
        recyclerView.setAdapter(adapter);

        return v;
    }



    private void initializeData() {
        TimelineList = new ArrayList<>();
        TimelineList.add(new SmartTimer_cardUI("Prep","15",R.drawable.sample));
        TimelineList.add(new SmartTimer_cardUI("Drink Beer","20",R.drawable.sample));
        TimelineList.add(new SmartTimer_cardUI("Cook Meat","120",R.drawable.sample));
        TimelineList.add(new SmartTimer_cardUI("Drink Beer","20",R.drawable.sample));
        TimelineList.add(new SmartTimer_cardUI("Rest Meat","20",R.drawable.sample));
        TimelineList.add(new SmartTimer_cardUI("Drink Beer","20",R.drawable.sample));
        TimelineList.add(new SmartTimer_cardUI("Drink Beer","20",R.drawable.sample));
        TimelineList.add(new SmartTimer_cardUI("Drink Beer","20",R.drawable.sample));
        TimelineList.add(new SmartTimer_cardUI("Drink Beer","20",R.drawable.sample));
        TimelineList.add(new SmartTimer_cardUI("Drink Beer","20",R.drawable.sample));
        TimelineList.add(new SmartTimer_cardUI("Drink Beer","20",R.drawable.sample));
        TimelineList.add(new SmartTimer_cardUI("Drink Beer","20",R.drawable.sample));
        TimelineList.add(new SmartTimer_cardUI("Drink Beer","20",R.drawable.sample));


    }

    public static SmartTimer_TimeLine newInstance(String text) {

        SmartTimer_TimeLine f = new SmartTimer_TimeLine();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}
