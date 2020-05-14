package com.example.ehprotocol;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class CoronaAlerter extends JobService {
    private static final String TAG = "Notifs";
    private boolean jobCancelled = false;
    private final int refreshRate = 10; // refreshRate seconds
    int count = 0;
    String user;
    private StitchAppClient stitchClient;
    private RemoteMongoClient mongoClient;
    private RemoteMongoCollection usersCollection;
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Task started");
        stitchClient = Stitch.getDefaultAppClient();
        mongoClient = stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        usersCollection = mongoClient.getDatabase("COVID19ContactTracing").getCollection("Users");
        doBackgroundWork(params);
        return true;
    }

    private void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(count < 15 * (60 / refreshRate)) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    user = preferences.getString("username", null);
                    Log.d(TAG, "Job # " + count + " started");
                    if (user != null) {
                        Document filterDoc = new Document().append("username", user);
                        RemoteFindIterable findResults = usersCollection
                                .find(filterDoc);
                        Task<List<Document>> itemsTask = findResults.into(new ArrayList<Document>());
                        itemsTask.addOnCompleteListener(new OnCompleteListener<List<Document>>() {
                            @Override
                            public void onComplete(@com.mongodb.lang.NonNull Task<List<Document>> task) {
                                if (task.isSuccessful()) {
                                    List<Document> items = task.getResult();
                                    boolean isContacted = items.get(0).getBoolean("isContact");
                                    if(isContacted){
                                        String title = "COVID-19 ALERT";
                                        String message = "Our data has detected that you've recently been in contact with someone who tested positive to the COVID-19 virus."
                                                + " You should immediately head to the nearest testing center to get tested, and follow the right procedures.";
                                        NotificationsSender.sendOverNotificationChannel(getApplicationContext(), NotificationsSender.HIGH_PRIORITY_CHANNEL, title, message);
                                        Document updateStatus = new Document().append("$set",
                                                new Document().append("isContact", false));
                                        final Task<RemoteUpdateResult> updateTask =
                                                usersCollection.updateOne(filterDoc, updateStatus);
                                        updateTask.addOnCompleteListener(new OnCompleteListener<RemoteUpdateResult>() {
                                            @Override
                                            public void onComplete(@androidx.annotation.NonNull Task<RemoteUpdateResult> task) {
                                                if (task.isSuccessful()) {
                                                } else {
                                                }
                                            }
                                        });
                                        resetStatus();
                                    }


                                }}});
                   // boolean isContacted = getStatus();
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
        final boolean[] status = new boolean[1];

        return status[0];
    }

    private void resetStatus() {
        Document filterDoc = new Document().append("username", user);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("isContact","true");
        editor.apply();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Task interrupted");
        jobCancelled = true;
        return true;
    }
}
