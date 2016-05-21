package com.jalee.bbqbuddy;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.jalee.bbqbuddy.R.array.*;

public class SmartTimer_TimeLine_Builder extends AppCompatActivity {

    private Spinner spinnerMeats, spinnerThick;
    String meat = "";
    Boolean weightBased = false;
    String activeWeight = "";
    String activeThickness = "";
    String cooktype = "";
    String meatName;
    Double curWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_timer__time_line__builder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        overridePendingTransition(R.anim.slide_up, R.anim.stationary);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Timeline Builder");

        spinnerMeats = (Spinner) findViewById(R.id.spinnerMeats);
        spinnerThick = (Spinner) findViewById(R.id.spinnerThick);
        List<String> meats = new ArrayList<String>();
        meats.add("Steak");
        meats.add("Chicken");
        meats.add("Fish");
        meats.add("Roast Beef");
        meats.add("Roast Chicken");
        meats.add("Roast Pork");

        List<String> thickness = new ArrayList<String>();
        thickness.add("1/2");
        thickness.add("3/4");
        thickness.add("1");
        thickness.add("1 1/4");
        thickness.add("1 2/4");
        thickness.add("1 3/4");
        thickness.add("2");

        final List<String> weight = new ArrayList<String>();
        weight.add(".5kg");
        weight.add("1kg");
        weight.add("1.5kg");
        weight.add("2kg");
        weight.add("2.5kg");
        weight.add("3kg");
        weight.add("3.5kg");
        weight.add("4kg");
        weight.add("4.5kg");
        weight.add("5kg");

        ArrayAdapter<String> mdataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_selectable_list_item, meats);
        mdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMeats.setAdapter(mdataAdapter);

        final ArrayAdapter<String> thickDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_selectable_list_item, thickness);
        thickDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final ArrayAdapter<String> weightDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_selectable_list_item, weight);
        weightDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerThick.setAdapter(thickDataAdapter);

        spinnerMeats.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                meat = (String) parent.getItemAtPosition(position);
                Log.v("item", (String) parent.getItemAtPosition(position));
                switch ((String) parent.getItemAtPosition(position)) {
                    case "Steak":
                        spinnerThick.setAdapter(thickDataAdapter);
                        weightBased = false;
                        break;
                    case "Chicken":
                        spinnerThick.setAdapter(weightDataAdapter);
                        weightBased = true;
                        break;
                    case "Fish":
                        spinnerThick.setAdapter(weightDataAdapter);
                        weightBased = true;
                        break;
                    case "Roast Beef":
                        spinnerThick.setAdapter(weightDataAdapter);
                        weightBased = true;
                        break;
                    case "Roast Chicken":
                        spinnerThick.setAdapter(weightDataAdapter);
                        weightBased = true;
                        break;
                    case "Roast Pork":
                        spinnerThick.setAdapter(weightDataAdapter);
                        weightBased = true;
                        break;
                    default:
                        spinnerThick.setAdapter(weightDataAdapter);
                        weightBased = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        spinnerThick.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                if (weightBased == true) {
                    activeWeight = (String) parent.getItemAtPosition(position);
                    Log.i("Info",activeWeight);
                } else {
                    activeThickness = (String) parent.getItemAtPosition(position);
                    Log.i("Info",activeThickness);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        Button btnadd = (Button) findViewById(R.id.btnAdd);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSelection();
            }
        });

    }

    public void addSelection() {
        RadioGroup radioCooktype = (RadioGroup)findViewById(R.id.radioCookType);
        int selectedId=radioCooktype.getCheckedRadioButtonId();
        RadioButton radiocookButton=(RadioButton)findViewById(selectedId);
        cooktype = (String) radiocookButton.getText();

        switch (activeWeight) {
            case ".5kg":
                curWeight = .5;
                break;
            case "1kg":
                curWeight = 1.0;
                break;
            case "1.5kg":
                curWeight = 1.5;
                break;
            case "2kg":
                curWeight = 2.0;
                break;
            case "2.5kg":
                curWeight = 2.5;
                break;
            case "3kg":
                curWeight = 3.0;
                break;
            case "3.5kg":
                curWeight = 3.5;
                break;
            case "4kg":
                curWeight = 4.0;
                break;
            case "4.5kg":
                curWeight = 4.5;
                break;
            case "5kg":
                curWeight = 5.0;
                break;
        }

        final EditText txtCustName = new EditText(this);

        txtCustName.setHint(meat + " " + activeThickness + " inch - " + cooktype);

        new AlertDialog.Builder(this)
            .setTitle("Name Your Meat")
            .setMessage("Set a custom name for you cut of meat")
            .setView(txtCustName)
            .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    meatName = txtCustName.getText().toString();
                    if (meatName == "") {
                        meatName = meat + " " + activeThickness + " inch - " + cooktype;
                    }
                    addToTimeline();
                }
            })
        .show();
    }

    public void addToTimeline() {
        switch (meat) {
            case "Steak":
                switch (activeThickness) {
                    case "1/2":
                        switch (cooktype) {
                            case "Rare":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 2,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 2,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                            case "Medium Rare":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 3,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 2,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                            case "Medium":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 4,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 2,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                            case "Well Done":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 5,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 3,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                        }
                        break;
                    case "3/4":
                        switch (cooktype) {
                            case "Rare":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 4,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 2,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                            case "Medium Rare":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 4,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 3,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                            case "Medium":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 5,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 3,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                            case "Well Done":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 7,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 5,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                        }
                        break;
                    case "1":
                        switch (cooktype) {
                            case "Rare":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 5,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 3,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                            case "Medium Rare":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 5,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 4,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                            case "Medium":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 6,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 4,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                            case "Well Done":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 8,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 6,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                        }
                        break;
                    case "1 1/4":
                        switch (cooktype) {
                            case "Rare":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 5,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 4,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                            case "Medium Rare":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 6,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 5,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                            case "Medium":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 7,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 5,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                            case "Well Done":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 9,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 7,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                        }
                        break;
                    case "1 1/2":
                        switch (cooktype) {
                            case "Rare":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 6,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 4,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                            case "Medium Rare":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 7,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side",5,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                            case "Medium":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 7,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 6,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                            case "Well Done":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 10,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 8,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                        }
                        break;
                    case "1 3/4":
                        switch (cooktype) {
                            case "Rare":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 7,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 5,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                            case "Medium Rare":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 8,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 6,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                            case "Medium":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 8,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 7,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                            case "Well Done":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 11,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 9,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                        }
                        break;
                    case "2":
                        switch (cooktype) {
                            case "Rare":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 8,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side",6,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                            case "Medium Rare":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 9,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 8,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                            case "Medium":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 10,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 8,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                            case "Well Done":
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " First Side", 13,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Second Side", 11,0));
                                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + activeThickness + " inch - " + cooktype, meatName + " Done - Rest", 3,0));
                                saveToTimeLine();
                                break;
                        }
                        break;
                }
                break;
            case "Chicken":
                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat, meatName + " place on grill", 3,0));
                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat, meatName + " rotate 45 degrees", 4,0));
                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat, meatName + " flip", 3,0));
                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat, meatName + " rotate 45 degrees", 2,0));
                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat, meatName + " Done - Rest", 3,0));
                saveToTimeLine();
                break;
            case "Fish":
                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat, meatName, (int) (curWeight * 15.0),0));
                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat, meatName + " Done - Rest", 10,0));
                saveToTimeLine();
                break;
            case "Roast Beef":
                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + cooktype, meatName, (int) (curWeight * 55.0),0));
                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat + " " + cooktype, meatName + " Done - Rest", 10,0));
                saveToTimeLine();
                break;
            case "Roast Chicken":
                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat, meatName, (int) (curWeight * 45.0),0));
                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat, meatName + " Done - Rest", 10,0));
                saveToTimeLine();
                break;
            case "Roast Pork":
                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat, meatName, (int) (curWeight * 40.0),0));
                SmartTimer_Service.TimelineList.add(new SmartTimer_cardUI(meat, meatName + " Done - Rest", 10,0));
                saveToTimeLine();
                break;
        }

    }

    public void saveToTimeLine () {
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


    @Override
    public void finish() {
        //remove handler for updating timer text

        overridePendingTransition(R.anim.stationary, R.anim.slide_down);
        super.finish();
    }

}
