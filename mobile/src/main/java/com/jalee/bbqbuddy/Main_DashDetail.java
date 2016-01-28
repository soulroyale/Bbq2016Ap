package com.jalee.bbqbuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class Main_DashDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__dash_detail);
        overridePendingTransition(R.anim.slide_inright, R.anim.slide_outleft);
        TextView title = (TextView) findViewById(R.id.txtTitle);
        title.setText(MainActivity.cardUIList.get(CardAdapter.cardIndex).getName());
        TextView content = (TextView) findViewById(R.id.txtContent);
        content.setText((MainActivity.cardUIList.get(CardAdapter.cardIndex).subTitle));
        ImageView image = (ImageView) findViewById(R.id.bannerImage);
        image.setImageResource(MainActivity.cardUIList.get(CardAdapter.cardIndex).id);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_inleft, R.anim.slide_outright);
    }
}
