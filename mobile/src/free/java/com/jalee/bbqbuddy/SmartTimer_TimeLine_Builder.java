package com.jalee.bbqbuddy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.jalee.bbqbuddy.R.array.*;

public class SmartTimer_TimeLine_Builder extends AppCompatActivity {

    private Spinner spinnerMeats, spinnerThick;
    String meat = "";
    Boolean weightBased = false;
    String activeWeight = "";
    String activeThickness = "";
    String cooktype = "";

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
        weight.add("500g");
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
                updateEstimate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        RadioGroup radioCooktype = (RadioGroup)findViewById(R.id.radioCookType);
        radioCooktype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                updateEstimate();
            }
        });

    }

    public void updateEstimate() {
        RadioGroup radioCooktype = (RadioGroup)findViewById(R.id.radioCookType);
        int selectedId=radioCooktype.getCheckedRadioButtonId();
        RadioButton radiocookButton=(RadioButton)findViewById(selectedId);
        cooktype = (String) radiocookButton.getText();

        TextView txtestimate = (TextView) findViewById(R.id.txtEstimate);

        if (weightBased) {
            txtestimate.setText(meat + " " + activeWeight + " " + cooktype);
        } else {
            txtestimate.setText(meat + " " + activeThickness + " " + cooktype);
        }


        switch (meat) {
            case "Steak":

                break;
            case "Chicken":

                break;
            case "Fish":

                break;
            case "Roast Beef":

                break;
            case "Roast Chicken":

                break;
            case "Roast Pork":

                break;
        }

    }





    @Override
    public void finish() {
        //remove handler for updating timer text

        overridePendingTransition(R.anim.stationary, R.anim.slide_down);
        super.finish();
    }

}
