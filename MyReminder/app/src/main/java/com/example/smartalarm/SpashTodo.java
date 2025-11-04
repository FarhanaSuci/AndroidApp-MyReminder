package com.example.smartalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;


public class SpashTodo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_todo);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SpashTodo.this,ToDo.class));
                finish();

            }
        },2000);
       //Button splash;
       //splash= findViewById(R.id.splash);

       /*getSupportActionBar().hide();
        final Intent i = new Intent(SpashTodo.this,ToDoSplash.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
                finish();

            }
        },1000);*/


       /* splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),ToDoSplash.class);
                startActivity(intent);

            }
        });*/
    }
}
