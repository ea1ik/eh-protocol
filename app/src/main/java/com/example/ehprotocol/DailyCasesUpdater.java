package com.example.ehprotocol;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

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
                    String url = "https://api.covid19api.com/total/country/lebanon";

                    JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                        JSONObject info = response.getJSONObject(response.length() - 1);
                                        if (newDate == null) {
                                            oldDate = info.getString("Date");
                                            newDate = info.getString("Date");
                                        } else {
                                            oldDate = newDate;
                                            newDate = info.getString("Date");
                                            if (!newDate.equals(oldDate)) {
                                                int totalCases, totalActive, totalDeaths, totalRecovered;
                                                totalCases = info.getInt("Confirmed");
                                                totalActive = info.getInt("Active");
                                                totalDeaths = info.getInt("Deaths");
                                                totalRecovered = info.getInt("Recovered");

                                                JSONObject prevDayData = response.getJSONObject(response.length() - 2);
                                                int dailyCases, dailyDeaths, dailyRecoveries;
                                                int prevCases, prevDeaths, prevRecoveries;

                                                prevCases = prevDayData.getInt("Confirmed");
                                                prevDeaths = prevDayData.getInt("Deaths");
                                                prevRecoveries = prevDayData.getInt("Recovered");

                                                dailyCases = totalCases - prevCases;
                                                dailyDeaths = totalDeaths - prevDeaths;
                                                dailyRecoveries = totalRecovered - prevRecoveries;

                                                String updates = "The total number of cases for " + transform(newDate) + " has reached " + totalCases + " (+" + dailyCases + ")"
                                                        + ", where there are " + totalActive + " cases. The total death number is " + totalDeaths + " (+"
                                                        + dailyDeaths + "). There has been " + dailyRecoveries + ".";

                                                NotificationsSender.sendOverNotificationChannel(getApplicationContext(), NotificationsSender.MEDIUM_PRIORITY_CHANNEL,
                                                        "Daily COVID-19 Updates", updates);
                                            }
                                        }
                                        Log.d(TAG, "Old Date: " + oldDate);
                                        Log.d(TAG, "New Date: " + newDate);
                                    } catch (Exception e) {

                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                    queue.add(request);
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
