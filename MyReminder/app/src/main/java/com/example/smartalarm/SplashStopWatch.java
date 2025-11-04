package com.example.smartalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class SplashStopWatch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_stop_watch);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashStopWatch.this,stopwatch2.class));
                finish();

            }
        }, 2000);
        /*Button stop;
        stop= findViewById(R.id.stopp);

       /*getSupportActionBar().hide();
        final Intent i = new Intent(SpashTodo.this,ToDoSplash.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
                finish();

            }
        },1000);*/


        /*stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),stopwatch2.class);
                startActivity(intent);

            }
        });*/
    }
    }
