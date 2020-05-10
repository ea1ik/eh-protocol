package com.example.ehprotocol;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;

import java.io.IOException;
import java.util.Arrays;

public class LocationProvider extends JobService {
    private static final String TAG = "Debug";
    private boolean jobCancelled = false;
    private static int count = 0;
    private int refreshRate = 10; // in seconds
    private FusedLocationProviderClient fusedLocationProviderClient;
    private StitchAppClient stitchClient;
    private RemoteMongoClient mongoClient;
    private RemoteMongoCollection usersCollection;
    private static Location location;

    @Override
    public boolean onStartJob(JobParameters params) {
        doBackgroundWork(params);
        return true;

    }

    private void doBackgroundWork(JobParameters params) {
        stitchClient = Stitch.getDefaultAppClient();
        mongoClient = stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        usersCollection = mongoClient.getDatabase("COVID19ContactTracing").getCollection("Users");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String user = preferences.getString("username", null);
                Document filterDoc = new Document().append("username", user);
                if (user != null){
                while (count < 15 * (60 / refreshRate)) {
                    getLocation();
                    if(location!= null) {
                        Log.d(TAG, "Latitude: " + location.getLatitude());
                        Log.d(TAG, "Longitude: " + location.getLongitude());

                        Document updateDoc = new Document().append("$set",
                                new Document().append("location", Arrays.asList(location.getLatitude(), location.getLongitude())
                                ));

                        final Task<RemoteUpdateResult> updateTask =
                                usersCollection.updateOne(filterDoc, updateDoc);
                        updateTask.addOnCompleteListener(new OnCompleteListener <RemoteUpdateResult> () {
                            @Override
                            public void onComplete(@NonNull Task <RemoteUpdateResult> task) {
                                if (task.isSuccessful()) {
                                    long numMatched = task.getResult().getMatchedCount();
                                    long numModified = task.getResult().getModifiedCount();
                                    Log.d("app", String.format("successfully matched %d and modified %d documents",
                                            numMatched, numModified));
                                } else {
                                    Log.e("app", "failed to update document with: ", task.getException());
                                }
                            }
                        });
                    }
                    else{
                        Log.d(TAG, "error");
                    }
                    // push it to the database
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
            }}
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
