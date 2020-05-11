package com.example.ehprotocol;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

// Base Stitch Packages
// Stitch Authentication Packages
// MongoDB Service Packages
// Utility Packages

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Debug";
    private StitchAppClient stitchClient;
    private RemoteMongoClient mongoClient;
    private RemoteMongoCollection usersCollection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stitch.initializeDefaultAppClient("networksproject-cfpxi");

        //stitchClient = Stitch.getDefaultAppClient();
        //mongoClient = stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        //usersCollection = mongoClient.getDatabase("COVID19ContactTracing").getCollection("Users");
        ImageButton enterButton = findViewById(R.id.enterButton);

        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID); //get ID of device


        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        runBackgroundLocationCheck();
        checkForUpdatedCases();

        enterButton.setOnClickListener(e -> {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String userid = preferences.getString("ID", null);
            if (userid != null){
                Log.d("hi", "in");
                Intent startIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(startIntent);
            }
            else{
            Intent startIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(startIntent);}
        });
    }

    private void runBackgroundLocationCheck() {
        ComponentName componentName = new ComponentName(this, LocationProvider.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setPersisted(true).setPeriodic(15 * 60 * 1000).build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            // success
        } else {
            // failure
        }
    }

    public void checkForUpdatedCases() {
        ComponentName componentName = new ComponentName(this, DailyCasesUpdater.class);
        JobInfo info = new JobInfo.Builder(445, componentName)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            // success
        } else {
            // failure
        }
    }
}
