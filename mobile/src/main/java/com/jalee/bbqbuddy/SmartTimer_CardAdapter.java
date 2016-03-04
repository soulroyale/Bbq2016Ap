package com.jalee.bbqbuddy;

/**
 * Created by Aaron on 25/01/2016.
 */

import android.app.Dialog;
import android.content.Context;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
        if (SmartTimer_Service.timerActive) {
            if (position < SmartTimer_Service.nextEventindex) {
                holder.itemView.setBackgroundColor(Color.parseColor("#E0E0E0"));
                holder.cardtitle.setTextColor(Color.parseColor("#9E9E9E"));
                holder.cardsubtitle.setTextColor(Color.parseColor("#9E9E9E"));
                holder.cardmins.setTextColor(Color.parseColor("#9E9E9E"));
            }
            if (position == SmartTimer_Service.nextEventindex) {
                holder.cardtitle.setTextColor(Color.parseColor("#D32F2F"));
                holder.cardsubtitle.setTextColor(Color.parseColor("#D32F2F"));
                holder.cardmins.setTextColor(Color.parseColor("#D32F2F"));
            }
        }
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
            cardimage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    remTimeline(context, getAdapterPosition());
                    return true;
                }
            });
            cardtitle.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    remTimeline(context, getAdapterPosition());
                    return true;
                }
            });
            cardsubtitle.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    remTimeline(context, getAdapterPosition());
                    return true;
                }
            });
            cardmins.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    decreaseMin(1, getAdapterPosition(), context);
                    return true;
                }
            });

            cardmins.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    increaseMin(1, getAdapterPosition(), context);
                }
            });

        }

                @Override
        public void onClick(View v) {

        }
    }

    public void increaseMin(int increaseVar, int adapterPos, Context context) {
        SmartTimer_Service.TimelineList.set(adapterPos, new SmartTimer_cardUI(SmartTimer_Service.TimelineList.get(adapterPos).getsubTitle(), SmartTimer_Service.TimelineList.get(adapterPos).getName(), SmartTimer_Service.TimelineList.get(adapterPos).getId() + increaseVar));
        SmartTimer_TimeLine.adapter.notifyDataSetChanged();
        SmartTimer_Service ST = new SmartTimer_Service();
        ST.saveTimeline(context);
        Log.i("info", String.valueOf(adapterPos));
        Log.i("info",String.valueOf(SmartTimer_Service.nextEventindex));
        if (SmartTimer_Service.timerActive & adapterPos == SmartTimer_Service.nextEventindex ) {
            SmartTimer_Service.timerChangeBy = 60000L;
            SmartTimer_Service.timerExtend = true;
            Log.i("info", "Timerextend");
        }
        Toast.makeText(context, SmartTimer_Service.TimelineList.get(adapterPos).getName() + " increased by " + increaseVar + " minute", Toast.LENGTH_SHORT).show();
    }

    public void decreaseMin(int decreaseVar, int adapterPos, Context context) {
        SmartTimer_Service.TimelineList.set(adapterPos, new SmartTimer_cardUI(SmartTimer_Service.TimelineList.get(adapterPos).getsubTitle(), SmartTimer_Service.TimelineList.get(adapterPos).getName(), SmartTimer_Service.TimelineList.get(adapterPos).getId() - decreaseVar));
        SmartTimer_TimeLine.adapter.notifyDataSetChanged();
        SmartTimer_Service ST = new SmartTimer_Service();
        ST.saveTimeline(context);
        if (SmartTimer_Service.timerActive & adapterPos == SmartTimer_Service.nextEventindex) {
            SmartTimer_Service.timerChangeBy = 60000L;
            SmartTimer_Service.timerReduce = true;
        }
        Toast.makeText(context, SmartTimer_Service.TimelineList.get(adapterPos).getName() + " decreased by " + decreaseVar + " minute", Toast.LENGTH_SHORT).show();
    }

    public void remTimeline (final Context context, final int adapterPos) {
        Log.i("Button Click", "Button Pressed at: " + adapterPos);
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

            cardIndex = (Integer) adapterPos;

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage("Would you like to Delete the selected item from the timeline?");

            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Log.i("Selected", "You salected yes");
                    SmartTimer_Service.TimelineList.remove(adapterPos);
                    SmartTimer_TimeLine.adapter.notifyDataSetChanged();
                    Integer newSmartTimerValue = 0;
                    for (int i = 0; i < SmartTimer_Service.TimelineList.size(); i++) {
                        newSmartTimerValue = newSmartTimerValue + (Integer) SmartTimer_Service.TimelineList.get(i).id;
                        System.out.println(newSmartTimerValue);
                    }
                    SmartTimer_Service.smartTimerMax = TimeUnit.MINUTES.toMillis(newSmartTimerValue);
                    SmartTimer_Service  ST= new SmartTimer_Service();
                    ST.saveTimeline(context);
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
                            SmartTimer_Service  ST= new SmartTimer_Service();
                            ST.saveTimeline(context);
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