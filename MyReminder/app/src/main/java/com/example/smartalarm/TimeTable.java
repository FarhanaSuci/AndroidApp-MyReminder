package com.example.smartalarm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class TimeTable extends AppCompatActivity {
    CustomCalenderView customCalenderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        customCalenderView = findViewById(R.id.Custom_Calender_View);


    }
}