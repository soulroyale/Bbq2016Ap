package com.jalee.bbqbuddy;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.concurrent.TimeUnit;

public class SmartTimer_TimeLine_Add extends AppCompatActivity {

    Integer hours;
    Integer minutes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_timer__time_line__add);
        overridePendingTransition(R.anim.slide_up, R.anim.stationary);
        minutes = 0;
        hours = 0;

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final ListView listHours = (ListView)findViewById(R.id.listHours);
        ArrayAdapter adapterHours=ArrayAdapter.createFromResource(this,R.array.timerHours,android.R.layout.simple_list_item_activated_1);
        listHours.setAdapter(adapterHours);
        listHours.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Resources res = getResources();
                TypedArray hour = res.obtainTypedArray(R.array.timerHours);
                hours = hour.getInt(position,0);
                listHours.smoothScrollToPosition(position);

            }
        });

        final ListView listMins = (ListView)findViewById(R.id.listMinutes);
        ArrayAdapter adapterMin=ArrayAdapter.createFromResource(this,R.array.timerMinutes,android.R.layout.simple_list_item_activated_1);
        listMins.setAdapter(adapterMin);
        listMins.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Resources res = getResources();
                TypedArray min = res.obtainTypedArray(R.array.timerMinutes);
                minutes = min.getInt(position, 0);
                listMins.smoothScrollToPosition(position);
            }
        });

        final Button btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView txtTitle = (TextView)findViewById(R.id.txtTitle);
                TextView txtDesc = (TextView)findViewById(R.id.txtDesc);

                if (hours == 0 & minutes == 0) {
                    //Creating dialogue in a Method as unable to get context from here
                    nothingSelAlert();
                } else {
                    SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(String.valueOf(txtDesc.getText()), String.valueOf(txtTitle.getText()), (hours * 60) + minutes,0));
                    SmartTimer_TimeLine.adapter.notifyDataSetChanged();
                    Integer newSmartTimerValue = 0;
                    for (int i = 0; i < SmartTimer_Service.TimelineList.size(); i++) {
                        newSmartTimerValue = newSmartTimerValue + (Integer) SmartTimer_Service.TimelineList.get(i).id;
                        System.out.println(newSmartTimerValue);
                    }
                    SmartTimer_Service.smartTimerMax = TimeUnit.MINUTES.toMillis(newSmartTimerValue);

                    SmartTimer_Service ST = new SmartTimer_Service();
                    ST.saveTimeline(getApplicationContext());
                    finish();
                }
            }
        });
        final Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void nothingSelAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("You haven't selected valid Hours or Minutes, please try again.");

        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Log.i("Selected", "You salected Ok");

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.slide_down);
    }

}
