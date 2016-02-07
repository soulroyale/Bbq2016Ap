package com.jalee.bbqbuddy;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class SmartTimer_TimeLine_Add extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Integer hours;
    Integer minutes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_timer__time_line__add);
        overridePendingTransition(R.anim.slide_up, R.anim.stationary);
        minutes = 0;
        hours = 0;

        Spinner spinHours = (Spinner)findViewById(R.id.spinnerHours);
        ArrayAdapter adapterHours=ArrayAdapter.createFromResource(this,R.array.timerHours,android.R.layout.simple_spinner_dropdown_item);
        spinHours.setAdapter(adapterHours);
        spinHours.setOnItemSelectedListener(this);

        Spinner spinMins = (Spinner)findViewById(R.id.spinnerMinutes);
        ArrayAdapter adapterMin=ArrayAdapter.createFromResource(this,R.array.timerMinutes,android.R.layout.simple_spinner_dropdown_item);
        spinMins.setAdapter(adapterMin);
        spinMins.setOnItemSelectedListener(this);




        final Button btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView txtTitle = (TextView)findViewById(R.id.txtTitle);
                TextView txtDesc = (TextView)findViewById(R.id.txtDesc);

                SmartTimer_TimeLine.TimelineList.add(new SmartTimer_cardUI(String.valueOf(txtDesc.getText()), String.valueOf(txtTitle.getText()), (hours * 60) + minutes));
                SmartTimer_TimeLine.adapter.notifyDataSetChanged();
                Integer newSmartTimerValue = 0;
                for (int i = 0; i < SmartTimer_TimeLine.TimelineList.size(); i++) {
                    newSmartTimerValue = newSmartTimerValue + (Integer) SmartTimer_TimeLine.TimelineList.get(i).id;
                    System.out.println(newSmartTimerValue);
                }
                SmartTimer.smartTimerMax = TimeUnit.MINUTES.toMillis(newSmartTimerValue);
                finish();
            }
        });
        final Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary, R.anim.slide_down);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView selMins = (TextView) view;
        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.spinnerHours)
        {
            hours = Integer.parseInt(selMins.getText().toString());
        }
        else if(spinner.getId() == R.id.spinnerMinutes)
        {
            minutes = Integer.parseInt(selMins.getText().toString());
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
