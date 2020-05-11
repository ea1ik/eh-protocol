package com.example.ehprotocol;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.util.Calendar;

public class DailyCasesUpdater extends JobService {
    private static final String TAG = "Notifs";
    private boolean jobCancelled = false;

    int count = 0;

    private String oldDate = "", newDate;

    private RequestQueue queue;

    @Override
    public boolean onStartJob(JobParameters params) {
        queue = Volley.newRequestQueue(this);
        Log.d(TAG, "Job started");
        doBackgroundWork(params);
        return true;
    }

    private void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(count < 30) {


                    count++;
                    try {
                        Thread.sleep(15*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "Job Finished");
                }
                count = 0;
                jobFinished(params, false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        jobCancelled = true;
        return true;
    }

    private String transform(String date) {
        String output = "";

        date = date.substring(0, 10);
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8));

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, --month);
        c.set(Calendar.DAY_OF_MONTH, day);

        output = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        return output;
    }
}
