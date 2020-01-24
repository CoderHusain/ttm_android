package com.example.imageload;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class locationService extends Service {
    MediaPlayer mediaPlayer;
    LocationManager locationManager;
    LocationListener locationListener;
    String locString = "";
    private static final String TAG =
            "LocUpload";
    public static final String mBroadcastLocationAction = "com.example.imageload.location";


    @Override
    public void onCreate() {
        super.onCreate();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locString = location.getLatitude() + ", " + location.getLongitude();
                Intent broadcastIntent = new Intent("location_update");
                broadcastIntent.setAction(locationService.mBroadcastLocationAction);
                broadcastIntent.putExtra("data", locString);
                sendBroadcast(broadcastIntent);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };
        startLocationInfo();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer=MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        startLocationInfo();
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String deviceID = telephonyManager.getDeviceId();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss");
            String currentDateandTime = sdf.format(new Date());
            //String str = "{ \"device_id\" : "+deviceID+", \"frequency\" : "+(freqString.equals("Peak:    0.0Hz") ? null : freqString)+",  \"location\" :  "+(locString.equals("") ? null : locString)+" , \"time\": "+currentDateandTime+" }";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("device_id", deviceID);
            jsonObject.put("location", (locString.equals("") ? null : locString));
            jsonObject.put("time", currentDateandTime);
            Log.d(TAG, "10 Sec Data:" + jsonObject.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if(locationManager != null){
            locationManager.removeUpdates(locationListener);
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }

    public void startLocationInfo() {

        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 50, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 50, locationListener);
            Log.d(TAG, "StartLocationInfo working");
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }





}
