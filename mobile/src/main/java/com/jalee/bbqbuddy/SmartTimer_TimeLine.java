package com.jalee.bbqbuddy;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.jalee.bbqbuddy.helper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Aaron on 1/02/2016.
 */
public class SmartTimer_TimeLine extends Fragment {

    private ItemTouchHelper mItemTouchHelper;

    public static Boolean fabHidden = false;

    public static RecyclerView recyclerView;
    public static SmartTimer_CardAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_smart_timer_timeline, container, false);

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
                        FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.v1_fab);
                        floatingActionButton.animate().translationY(floatingActionButton.getHeight() + 180).setInterpolator(new AccelerateInterpolator(2)).start();
                    }

                } else {
                    if (fabHidden == true) {
                        Log.i("scroll", "SCrolling down");
                        fabHidden = false;
                        FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.v1_fab);
                        floatingActionButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                    }
                }
            }
        });




        LinearLayoutManager linman = new LinearLayoutManager(null);
        linman.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linman);
        recyclerView.setHasFixedSize(true);

        adapter = new SmartTimer_CardAdapter(SmartTimer_Service.TimelineList);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

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
