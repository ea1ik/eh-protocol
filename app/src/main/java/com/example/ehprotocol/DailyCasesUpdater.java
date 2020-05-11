package com.example.ehprotocol;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Calendar;

public class DailyCasesUpdater extends JobService {
    private static final String TAG = "Notifs";
    private boolean jobCancelled = false;
    private final int refreshRate = 60; //60s
    int count = 0;

    private static boolean unlocked = false;

    private static String savedDate;

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
                while(count < 15 * (60 / refreshRate)) {
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url("https://covid-19-data.p.rapidapi.com/country/code?format=json&code=lb")
                            .get()
                            .addHeader("x-rapidapi-host", "covid-19-data.p.rapidapi.com")
                            .addHeader("x-rapidapi-key", "942ffaca3amsh6182aa878ceed86p14709cjsn11a537a29ed5")
                            .build();

                    Thread thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try  {
                                Response response = client.newCall(request).execute();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream(), "UTF-8"));
                                String line = reader.readLine();
                                JSONTokener tokener = new JSONTokener(line);
                                JSONArray mainArray = new JSONArray(tokener);
                                JSONObject mainObject = mainArray.getJSONObject(0);
                                String lastChange = mainObject.getString("lastChange");
                                if(savedDate == null)
                                    savedDate = lastChange;
                                else if(!lastChange.equals(savedDate)) {
                                    unlocked = true;
                                    savedDate = lastChange;
                                    int totalCases, totalActive, totalDeaths, totalRecoveries;

                                    totalCases = mainObject.getInt("confirmed");
                                    totalRecoveries = mainObject.getInt("recovered");
                                    totalActive = totalCases - totalRecoveries;
                                    totalDeaths = mainObject.getInt("deaths");

                                    String updates = "The total number of cases for " + transform(savedDate) + " has reached " + totalCases
                                            + ", where there are " + totalActive + " cases. The total death toll is " + totalDeaths;
                                    NotificationsSender.sendOverNotificationChannel(getApplicationContext(), NotificationsSender.MEDIUM_PRIORITY_CHANNEL,
                                            "Daily COVID-19 Updates", updates);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    thread.start();
                    count++;

                    Log.d(TAG, "Thread Finished");

                    try {
                        Thread.sleep(refreshRate*1000);
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
