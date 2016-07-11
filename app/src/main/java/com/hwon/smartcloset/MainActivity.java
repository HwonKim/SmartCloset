package com.hwon.smartcloset;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar Setting//
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        TextView title = (TextView)findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

        //기존 타이틀 없애고 새로운 타이틀 생성//
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title.setText("Smart Closet");

    }
}
