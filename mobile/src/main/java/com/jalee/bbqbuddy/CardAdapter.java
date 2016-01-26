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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;



public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    List<cardUI> list = new ArrayList<>();

    public CardAdapter(List<cardUI> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.cardUI = getItem(position);
        holder.cardtitle.setText(list.get(position).name);
        holder.cardsubtitle.setText(list.get(position).subTitle);
        holder.cardimage.setImageResource(list.get(position).id);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public cardUI getItem(int i) {
        return list.get(i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView cardimage;
        TextView cardtitle;
        TextView cardsubtitle;
        Button cardlikebtn;
        Button cardfavbutton;
        Button cardsharebutton;
        cardUI cardUI;
        final Context context = itemView.getContext();

        public ViewHolder(View itemView) {
            super(itemView);
            cardimage = (ImageView) itemView.findViewById(R.id.cardimage);
            cardtitle = (TextView) itemView.findViewById(R.id.cardtitle);
            cardsubtitle = (TextView) itemView.findViewById(R.id.carddesc);
            //cardlikebtn.setOnClickListener(this);
            cardimage.setOnClickListener(this);
            cardtitle.setOnClickListener(this);
            cardsubtitle.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Log.i("Button Click","Button Pressed at: "  + getAdapterPosition());
            final Intent intent;
            intent =  new Intent(context, Main_DashDetail.class);
            context.startActivity(intent);
        }

    }

}