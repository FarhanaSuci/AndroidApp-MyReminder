package com.example.smartalarm;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class action_bar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    GridLayout mainGrid;
    Button btnSignOut;
    FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth=FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_action_bar);
        mainGrid = (GridLayout) findViewById(R.id.mainGrid);
        setSingleEvent(mainGrid);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);
        // setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.drawer_layout);
        }

    }


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home){
            //switch (item.getItemId()) {
            //case R.id.nav_home:
            Toast.makeText(this, "Home is clicked", Toast.LENGTH_SHORT).show();
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        } if (item.getItemId() == R.id.user_manual) {
            Toast.makeText(this, "User-manual is clicked", Toast.LENGTH_SHORT).show();
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
            Intent intent = new Intent(getApplicationContext(),UserManual.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.nav_share) {
            Toast.makeText(this, "Share is clicked", Toast.LENGTH_SHORT).show();

            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShareFragment()).commit();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String subject = "My Reminder app";
            String body = "This app is very useful to our daily activities.It reminds us about  our daily activities" +
                    " and save tasks.By using this app, we can efficiently manage our time.\ncom.example.smartalarm";
            intent.putExtra(Intent.EXTRA_SUBJECT,subject);
            intent.putExtra(Intent.EXTRA_TEXT,body);
            startActivity(Intent.createChooser(intent,"share with"));

        }
        if (item.getItemId() == R.id.nav_about) {
            Toast.makeText(this, "About us is clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),Ratingbar.class);
            startActivity(intent);


            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
        }
        if (item.getItemId() == R.id.nav_feedback) {
            //case R.id.nav_logout:
            Toast.makeText(this, "Feedback is clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),FeedbackActivity.class);
            startActivity(intent);

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //public boolean onCreateOptionMenu(Menu menu){
    //MenuInflater inflater = getMenuInflater();
    //inflater.inflate(R.menu.nav_menu,menu);
    //return true;
    //}
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    private void setSingleEvent(GridLayout mainGrid) {
        for(int i=0;i<mainGrid.getChildCount();i++){
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI == 0) {

                        Intent intent = new Intent(action_bar.this,SplashTimer.class);
                       // Intent intent = new Intent(action_bar.this,LocTrip.class);
                        startActivity(intent);



                    }
                    if (finalI == 1) {

                        Intent intent = new Intent(action_bar.this,SplashStopWatch.class);
                        startActivity(intent);


                    }
                    if (finalI == 2) {

                        Intent intent = new Intent(action_bar.this,SpashTodo.class );
                        startActivity(intent);


                    }
                    if (finalI == 3) {

                        Intent intent = new Intent(action_bar.this, ExDboard.class);
                        startActivity(intent);


                    }
                    if (finalI == 4) {

                        Intent intent = new Intent(action_bar.this, TaskReminderSplash.class);
                        startActivity(intent);


                    }
                    if (finalI == 5) {

                        Intent intent = new Intent(action_bar.this, DiarySplash.class);
                        startActivity(intent);


                    }
                    if (finalI == 6) {

                        Intent intent = new Intent(action_bar.this,  Activity2.class);
                        startActivity(intent);


                    }
                   if (finalI ==7) {

                                mAuth.signOut();
                                Intent intent = new Intent(action_bar.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            }


                            //Intent intent = new Intent(Intent.ACTION_MAIN);
                            //intent.addCategory(Intent.CATEGORY_HOME);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //startActivity(intent);


                    }

            });
        }
    }




}

