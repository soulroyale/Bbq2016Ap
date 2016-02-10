package com.jalee.bbqbuddy;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Aaron on 1/02/2016.
 */
public class SmartTimer_TimeLine extends Fragment {

    public static Boolean fabHidden = false;

    public static List<SmartTimer_cardUI> TimelineList;
    public static RecyclerView recyclerView;
    public static SmartTimer_CardAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_smart_timer_timeline, container, false);

        //TextView tv = (TextView) v.findViewById(R.id.tvFragFirst);
        //tv.setText(getArguments().getString("msg"));
        fabHidden = false;
        recyclerView=(RecyclerView) v.findViewById(R.id.smarttimer_timeline_cardrecycler);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (fabHidden == false) {
                        Log.i("scroll", "Scrolling up");
                        fabHidden = true;
                        FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
                        floatingActionButton.animate().translationY(floatingActionButton.getHeight() + 70).setInterpolator(new AccelerateInterpolator(2)).start();
                    }

                } else {
                    if (fabHidden == true) {
                        Log.i("scroll", "SCrolling down");
                        fabHidden = false;
                        FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
                        floatingActionButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                    }
                }
            }
        });




        LinearLayoutManager linman = new LinearLayoutManager(null);
        linman.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linman);
        recyclerView.setHasFixedSize(true);
        initializeData();
        adapter = new SmartTimer_CardAdapter(TimelineList);
        recyclerView.setAdapter(adapter);
        return v;
    }


    public void initializeData() {
        TimelineList = new ArrayList<>();
        /*
        TimelineList.add(new SmartTimer_cardUI("Prep the Meat for Cooking, i.e. using a rub", "Prep", 30));
        TimelineList.add(new SmartTimer_cardUI("Have a drink, preferably a James Squire 150 lashes","Drink Beer",10));
        TimelineList.add(new SmartTimer_cardUI("Cook Meat","Cook Meat",240));
        TimelineList.add(new SmartTimer_cardUI("Have a drink, preferably a James Squire 150 lashes","Drink Beer",10));
        TimelineList.add(new SmartTimer_cardUI("Let your meat rest, its tired","Rest Meat",30));
        TimelineList.add(new SmartTimer_cardUI("Have a drink, preferably a James Squire 150 lashes","Drink Beer",10));
        TimelineList.add(new SmartTimer_cardUI("Have a drink, preferably a James Squire 150 lashes","Drink Beer",10));
        TimelineList.add(new SmartTimer_cardUI("Have a drink, preferably a James Squire 150 lashes", "Drink Beer", 10));
        TimelineList.add(new SmartTimer_cardUI("Have a drink, preferably a James Squire 150 lashes", "Drink Beer", 10));
        TimelineList.add(new SmartTimer_cardUI("Have a drink, preferably a James Squire 150 lashes", "Drink Beer", 10));
        TimelineList.add(new SmartTimer_cardUI("Have a drink, preferably a James Squire 150 lashes", "Drink Beer", 10));
        TimelineList.add(new SmartTimer_cardUI("Have a drink, preferably a James Squire 150 lashes", "Drink Beer", 10));
        TimelineList.add(new SmartTimer_cardUI("Have a drink, preferably a James Squire 150 lashes", "Drink Beer", 10));
        */
        MainActivity.smartTimerMax = TimeUnit.MINUTES.toMillis(4);
    }

    public static SmartTimer_TimeLine newInstance(String text) {

        SmartTimer_TimeLine f = new SmartTimer_TimeLine();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
    /*
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {

            if (!isVisibleToUser) {
                Log.d("MyFragment", "Not visible anymore.  Stopping audio.");
                // TODO stop audio playback
                FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
                fab.setImageResource(R.drawable.ic_media_play);
            }
        }
    }
    */
}
