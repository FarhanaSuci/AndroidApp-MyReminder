package com.example.smartalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class stopwatch extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        Button cntUp;
        Button cntDown;
        cntUp = (Button) findViewById(R.id.cntUp);
        cntDown=(Button) findViewById(R.id.cntDown);
        cntUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),trip.class);
                startActivity(intent);

            }
        });
        cntDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),stopwatch1.class);
                startActivity(intent);

            }
        });
    }
}