package com.example.ehprotocol;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton enterButton = findViewById(R.id.enterButton);

        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID); //get ID of device


        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        runBackgroundLocationCheck();

        enterButton.setOnClickListener(e -> {
            Intent startIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(startIntent);
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
}
