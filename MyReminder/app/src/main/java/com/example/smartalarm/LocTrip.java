package com.example.smartalarm;

import static android.content.ContentValues.TAG;
import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartalarm.databinding.ActivityMapsBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Locale;

public class LocTrip extends AppCompatActivity  {

    private static final int PERMISSION_FINE_LOCATION = 99;
    TextView tv_lat,tv_lon,tv_altitude,tv_accuracy,tv_speed,tv_sensor,tv_updates,tv_address,tv_wayPoint;
    Button btn_newWayPoint,btn_showWayPoint,btn_showMap;
    Switch sw_locationsupdates,sw_gps;
    Location currentLocation;
    List<Location>savedLocations;
    boolean updateOn = false;
    String TAG ="location";
    private LocationRequest locationRequest;
    private LocationRequest.Builder builder;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationSettingsRequest mLocationSettingRequest;
    private Location lastlocation;
    private SettingsClient mSettingClient;
    private LocationCallback locationCallback;
    Double d_lat,d_long;
    String fetched_address="";
    Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_trip);
        tv_lat = (TextView) findViewById(R.id.tv_lat);
        tv_lon = (TextView) findViewById(R.id.tv_lon);
        tv_altitude = (TextView) findViewById(R.id.tv_altitude);
        tv_accuracy = (TextView) findViewById(R.id.tv_accuracy);
        tv_speed = (TextView) findViewById(R.id.tv_speed);
        tv_sensor = (TextView) findViewById(R.id.tv_sensor);
        tv_updates = (TextView) findViewById(R.id.tv_updates);
        tv_address = (TextView) findViewById(R.id.tv_address);
        sw_gps = findViewById(R.id.sw_gps);
        btn_showMap = findViewById(R.id.btn_showMap);
        btn_newWayPoint =  findViewById(R.id.btn_newWayPoint);
        btn_showWayPoint =findViewById(R.id.btn_showWayPoint);
        sw_locationsupdates = findViewById(R.id.sw_locationsupdates);
        tv_wayPoint = (TextView)findViewById(R.id.tv_count);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                updateUIValues(location);
            }
        };
        btn_newWayPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the gps location
                MyApplication myApplication = (MyApplication) getApplicationContext();
                savedLocations = myApplication.getMyLocations();
                savedLocations.add(currentLocation);
            }
        });
        btn_showWayPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LocTrip.this,ShowSavedLocationList.class);
                startActivity(i);
            }
        });

        btn_showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LocTrip.this, MapsActivity2.class);
                startActivity(i);
            }
        });


        //builder = new LocationRequest.Builder(B);
       /* locationRequest = new  LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,100)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(2000)
                .setMaxUpdateDelayMillis(100)
                .build();*/



        /*locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, locationInterval)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(locationFastestInterval)
                .setMaxUpdateDelayMillis(locationMaxWaitTime)
                .build();*/


        //mlocationRequest = new LocationRequest.Builder(PRIORITY_HIGH_ACCURACY).setMinUpdateIntervalMillis(100)
        //.setMaxUpdateDelayMillis(300).build();
locationRequest = new LocationRequest.Builder(PRIORITY_HIGH_ACCURACY).setMinUpdateIntervalMillis(3000)
        .setIntervalMillis(5000).setMinUpdateDistanceMeters(5).build();

     sw_gps.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             if (sw_gps.isChecked()){
                 tv_sensor.setText("Using GPS sensors");
             }
             else {
                 tv_sensor.setText("Using Towers + Wifi");
             }
         }
     });
     sw_locationsupdates.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             if (sw_locationsupdates.isChecked()){
                 startLocationUpdates();

             }
             else {
                 stopLocationUpdates();

             }
         }
     });
     updateGPS();

    }

    private void startLocationUpdates() {
        tv_updates.setText("Location is being Tracked");
        //fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
        updateGPS();

    }

    private void stopLocationUpdates() {
        tv_updates.setText("Location is not being Tracked");
        tv_lat.setText("Not tracking location");
        tv_lon.setText("Not tracking location");
        tv_speed.setText("Not tracking location");
        tv_address.setText("Not tracking location");
        tv_accuracy.setText("Not tracking location");
        tv_altitude.setText("Not tracking location");
        tv_sensor.setText("Not tracking location");
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updateGPS();
                }
                else {
                    Toast.makeText(this, "This app requires permission to be grated in order to access", Toast.LENGTH_SHORT).show();
                finish();
                }
                break;
        }
    }

    private void updateGPS()
    {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(LocTrip.this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //we got permissons
                    updateUIValues(location);
                    currentLocation = location;
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_FINE_LOCATION);
            //if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ){
                //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FINE_PERMISSION_CODE);


            }
        }

    }

    private void updateUIValues(Location location) {
        tv_lat.setText(String.valueOf(location.getLatitude()));
        tv_lon.setText(String.valueOf(location.getLongitude()));
        tv_accuracy.setText(String.valueOf(location.getAccuracy()));
        if (location.hasAltitude()){
            tv_altitude.setText(String.valueOf(location.getAltitude()));
        }
        else {
            tv_altitude.setText("Not available");
        }
        if (location.hasSpeed()){
            tv_speed.setText(String.valueOf(location.getSpeed()));
        }
        else {
            tv_speed.setText("Not available");
        }
        Geocoder geocoder = new Geocoder(LocTrip.this);
        try {
             geocoder = new Geocoder(this,Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            tv_address.setText(addresses.get(0).getAddressLine(0));
           // fetched_address = addresses.get(0).getAddressLine(0);
           // Log.d(TAG,""+fetched_address);
            //tv_address.setText(fetched_address+"");

        }
        catch (Exception e){
            tv_address.setText("Unable to get street Address");

        }
        MyApplication myApplication = (MyApplication) getApplicationContext();
        savedLocations = myApplication.getMyLocations();
        tv_wayPoint.setText(Integer.toString(savedLocations.size()));

    }

}
