package com.example.semesterproject2;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.sql.Timestamp;

public class GetLocationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private LocationListener locationListener;
    private LocationManager locationManager;



    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {

        ContentResolver resolver = this.getContentResolver();
        //DatabaseHelper db = new DatabaseHelper(this);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(@NonNull Location location) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                ContentValues values = new ContentValues();
                values.put("LATITUDE",location.getLatitude());
                values.put("LONGITUDE",location.getLongitude());
                values.put("TIMESTAMP",timestamp.toString());
                resolver.insert(Uri.parse("content://com.example.semesterproject2/coordinates"),values);

                System.out.println(values);

                Intent i = new Intent("Location Update");
                i.putExtra("Coordinates","Longitude: "+location.getLongitude()+"\nLatitude: "+location.getLatitude());
                sendBroadcast(i);
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000, 10, locationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null){
            locationManager.removeUpdates(locationListener);
        }
    }
}
