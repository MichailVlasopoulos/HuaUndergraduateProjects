package com.example.semesterproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;
    private BroadcastReceiver startActivityReceiver;
    int i=0;

    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String lat = (String) intent.getExtras().get("Coordinates");
                    Toast.makeText(MainActivity.this, lat, Toast.LENGTH_SHORT).show();
                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("Location Update"));
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null) {
            Intent i = new Intent(getApplicationContext(),GetLocationService.class);
            stopService(i);
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("Welcome!");

        Intent i = new Intent(getApplicationContext(),GetLocationService.class);
        //stopService(i);

        startActivityReceiver = new BroadcastReceiver() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!requestLocationPermissions()) {
                    int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
                    if (status == 4) { //not charging
                        textView.setText("Phone is not charging: Getting Locations...");
                        startService(i);
                    }
                    if (status == 2) { //charging
                        textView.setText("Phone is charging: No Location Updates");
                        stopService(i);
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(startActivityReceiver,intentFilter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        TextView t = MainActivity.this.findViewById(R.id.textView);
        if (requestCode == 100){
            if (grantResults[0] == (PackageManager.PERMISSION_GRANTED)) {
                    System.out.println("accept");
                    t.setText("Permissions Granted");
            } else {
                    System.out.println("deny");
                    if(i<1) {
                        t.setText("Permissions Denied");
                        requestLocationPermissions();
                    } else {
                        t.setText("Denied 2 times in a row. \nPlease reset permissions from settings and restart the app");
                    }
                    i++;
            }
        }
    }

    private boolean requestLocationPermissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return true;
        }
        return false;
    }


}