package com.example.ehprotocol;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class LocationProvider extends JobService {
    private static final String TAG = "ExampleJobService";
    private boolean jobCancelled = false;
    private static int count = 0;
    private int refreshRate = 30; // in seconds

    @Override
    public boolean onStartJob(JobParameters params) {
        doBackgroundWork(params);
        return true;
    }

    private void doBackgroundWork(JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (count < 30) {
                    // get location
                    // compare it with other locations
                    // update local database
                    /*
                    System sleeps for 30s before checking for location again
                     */
                    try {
                        Thread.sleep(refreshRate * 1000);
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

    @Override
    public boolean onStopJob(JobParameters params) {
        //In case location couldn't be found
        jobCancelled = true;
        return true;
    }
}
