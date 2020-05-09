package com.example.ehprotocol;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LocationProvider extends JobService {
    private static final String TAG = "Debug";
    private boolean jobCancelled = false;
    private static int count = 0;
    private int refreshRate = 10; // in seconds
    private FusedLocationProviderClient fusedLocationProviderClient;

    private static Location location;

    @Override
    public boolean onStartJob(JobParameters params) {
        doBackgroundWork(params);
        return true;
    }

    private void doBackgroundWork(JobParameters params) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (count < 15 * (60 / refreshRate)) {
                    getLocation();
                    if(location!= null) {
                        Log.d(TAG, "Latitude: " + location.getLatitude());
                        Log.d(TAG, "Longitude: " + location.getLongitude());
                    }
                    else{
                        Log.d(TAG, "error");
                    }
                    // push it to the databas
                    // query to see if anyone is nearby
                    // compare it with other locations
                    // update database
                    try {
                        Thread.sleep(refreshRate*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count += 1;
                }
                jobFinished(params, false);
                count = 0;
            }
        }).start();
    }

    private void getLocation(){
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                LocationRequest locationRequest = new LocationRequest();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setNumUpdates(1);
                //locationRequest.setInterval(refreshRate*1000);

                fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                location = locationResult.getLastLocation();

                            }
                        },
                        Looper.myLooper()
                );
            }
        });
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        //In case location couldn't be found
        jobCancelled = true;
        return true;
    }
}
