package com.jalee.bbqbuddy;

/**
 * Created by Aaron on 25/01/2016.
 */

import android.app.Dialog;
import android.content.Context;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class SmartTimer_CardAdapter extends RecyclerView.Adapter<SmartTimer_CardAdapter.ViewHolder> {

    public static Integer cardIndex;
    List<SmartTimer_cardUI> list = new ArrayList<>();

    public SmartTimer_CardAdapter(List<SmartTimer_cardUI> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragmant_smart_timer_card_view, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.SmartTimer_cardUI = getItem(position);
        holder.cardtitle.setText(list.get(position).name);
        holder.cardsubtitle.setText(list.get(position).subTitle);
        holder.cardmins.setText(String.valueOf(list.get(position).id));
        //holder.cardimage.setImageResource(list.get(position).id);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public SmartTimer_cardUI getItem(int i) {
        return list.get(i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView cardimage;
        TextView cardtitle;
        TextView cardsubtitle;
        TextView cardmins;
        SmartTimer_cardUI SmartTimer_cardUI;
        final Context context = itemView.getContext();

        public ViewHolder(View itemView) {
            super(itemView);
            cardimage = (ImageView) itemView.findViewById(R.id.cardimage);
            cardtitle = (TextView) itemView.findViewById(R.id.cardtitle);
            cardsubtitle = (TextView) itemView.findViewById(R.id.carddesc);
            cardmins = (TextView) itemView.findViewById(R.id.cardmins);
            cardimage.setOnClickListener(this);
            cardtitle.setOnClickListener(this);
            cardsubtitle.setOnClickListener(this);
            cardmins.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Log.i("Button Click", "Button Pressed at: " + getAdapterPosition());
            if (SmartTimer_Service.timerActive == true) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Cannot currently edit timeline while timer is active");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            } else {

                cardIndex = (Integer) getAdapterPosition();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Would you like to Delete the selected item from the timeline?");

                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Log.i("Selected", "You salected yes");
                        SmartTimer_Service.TimelineList.remove(getAdapterPosition());
                        SmartTimer_TimeLine.adapter.notifyDataSetChanged();
                        Integer newSmartTimerValue = 0;
                        for (int i = 0; i < SmartTimer_Service.TimelineList.size(); i++) {
                            newSmartTimerValue = newSmartTimerValue + (Integer) SmartTimer_Service.TimelineList.get(i).id;
                            System.out.println(newSmartTimerValue);
                        }
                        SmartTimer_Service.smartTimerMax = TimeUnit.MINUTES.toMillis(newSmartTimerValue);
                    }
                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("Selected", "You salected no");
                    }

                });
                alertDialogBuilder.setNeutralButton("Delete All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder alertDialogBuilderdelAll = new AlertDialog.Builder(context);
                        alertDialogBuilderdelAll.setMessage("WARNING!! This will clear the entire timeline, are you sure?");
                        alertDialogBuilderdelAll.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Log.i("Selected", "You salected yes");
                                SmartTimer_Service.TimelineList.clear();
                                SmartTimer_TimeLine.adapter.notifyDataSetChanged();
                                Integer newSmartTimerValue = 0;
                                for (int i = 0; i < SmartTimer_Service.TimelineList.size(); i++) {
                                    newSmartTimerValue = newSmartTimerValue + (Integer) SmartTimer_Service.TimelineList.get(i).id;
                                    System.out.println(newSmartTimerValue);
                                }
                                SmartTimer_Service.smartTimerMax = TimeUnit.MINUTES.toMillis(newSmartTimerValue);
                            }
                        });

                        alertDialogBuilderdelAll.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("Selected", "You salected no");
                            }

                        });
                        AlertDialog alertDialog = alertDialogBuilderdelAll.create();
                        alertDialog.show();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        }
    }


}