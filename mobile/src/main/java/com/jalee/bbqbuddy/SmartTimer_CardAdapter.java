package com.jalee.bbqbuddy;

/**
 * Created by Aaron on 25/01/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


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
            cardIndex = (Integer) getAdapterPosition();
            //final Intent intent;
            //intent =  new Intent(context, Main_DashDetail.class);
            //context.startActivity(intent);
        }

    }

}