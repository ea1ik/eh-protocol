package com.example.ehprotocol;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class CoronaAlerter extends JobService {
    private static final String TAG = "Notifs";
    private boolean jobCancelled = false;
    private final int refreshRate = 10; // refreshRate seconds
    int count = 0;


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Task started");
        doBackgroundWork(params);
        return true;
    }

    private void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(count < 15 * (60 / refreshRate)) {
                    Log.d(TAG, "Job # " + count + " started");

                    boolean isContacted = getStatus();
                    if(isContacted){
                        String title = "BIG ASS ALERT BUDDY";
                        String message = "LISTEN HERE YOU LIL DUMBFUCK, YOU'VE BEEN FUCKED UP OK? I WANT YOU TO FUCKING CHILL YOU BIG DUMBASS"
                                +" BECAUSE THINGS ARE GONNA BE OKAY. SO DONT FUCKING WORRY AND STAY AT FUCKING HOME, DICK.";
                        NotificationsSender.sendOverNotificationChannel(getApplicationContext(), NotificationsSender.HIGH_PRIORITY_CHANNEL, title, message);
                        resetStatus();
                    }

                    try {
                        Thread.sleep(refreshRate*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Log.d(TAG, "Job # " + count + " finished");
                    count++;
                }
                count = 0;
                jobFinished(params, false);
                Log.d(TAG, "Task finished");
            }
        }).start();
    }

    private boolean getStatus() {
        // comment the return status and query here
        return false;
    }

    private void resetStatus() {
        // make the isSick thingy false, and make the isSick thingy in the user class true
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Task interrupted");
        jobCancelled = true;
        return true;
    }
}
