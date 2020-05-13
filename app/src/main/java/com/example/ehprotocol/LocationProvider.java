package com.example.ehprotocol;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
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
import com.mongodb.BasicDBObject;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.BsonDateTime;
import org.bson.BsonTimestamp;
import org.bson.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    private ArrayList<Document> contact_people;
    SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    public boolean onStartJob(JobParameters params) {
        stitchClient = Stitch.getDefaultAppClient();
        mongoClient = stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        usersCollection = mongoClient.getDatabase("COVID19ContactTracing").getCollection("Users");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        contact_people = new ArrayList<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
         doBackgroundWork(params);
        return true;
    }
    private void doBackgroundWork(JobParameters params) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                    while (count < 15 * (60 / refreshRate)) {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        String user = preferences.getString("username", null);
                            getLocation();
                            if (user != null) {

                                Document filterDoc = new Document().append("username", user);
                                if (location != null) {
                            String message = "Latitude: " + location.getLatitude() + "\n" + "Longitude: " + location.getLongitude();
                           // NotificationsSender.sendOverNotificationChannel(getApplicationContext(), NotificationsSender.HIGH_PRIORITY_CHANNEL, ".", message);

                            Document updateDoc = new Document().append("$set",
                                    new Document().append("location", Arrays.asList(location.getLatitude(), location.getLongitude())
                                    ));

                            final Task<RemoteUpdateResult> updateTask =
                                    usersCollection.updateOne(filterDoc, updateDoc);
                            updateTask.addOnCompleteListener(new OnCompleteListener<RemoteUpdateResult>() {
                                @Override
                                public void onComplete(@NonNull Task<RemoteUpdateResult> task) {
                                    if (task.isSuccessful()) {
                                        long numMatched = task.getResult().getMatchedCount();
                                        long numModified = task.getResult().getModifiedCount();
                                    } else {
                                        Log.e("app", "failed to update document with: ", task.getException());
                                    }
                                }
                            });
                            double lat=location.getLatitude();
                            double lon=location.getLongitude();
                            //find my acc & get my list
                            user = preferences.getString("username", null);
                            RemoteFindIterable myAcc = usersCollection
                                    .find(new Document().append("username",user));

                           Task<List<Document>> itemTask = myAcc.into(new ArrayList<Document>());
                            itemTask.addOnCompleteListener(new OnCompleteListener<List<Document>>() {
                                @Override
                                public void onComplete(@com.mongodb.lang.NonNull Task<List<Document>> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("h", "hello");
                                        List<Document> items = task.getResult();
                                        ArrayList<Document> list = (ArrayList<Document>) items.get(0).get("contacts");
                                        Log.d("size", String.valueOf(list.size()));
                                        for (Document a : list){
                                            contact_people.add(new Document().append("_id",a.get("id").toString()).append("date", a.getDate("date")));
                                        }
                                        Log.d("inside", String.valueOf(contact_people.size()));

                            Log.d("out", String.valueOf(contact_people.size()));

                            RemoteFindIterable findResults = usersCollection
                                    .find();

                            Task<List<Document>> itemsTask = findResults.into(new ArrayList<Document>());
                            itemsTask.addOnCompleteListener(new OnCompleteListener<List<Document>>() {
                                @Override
                                public void onComplete(@com.mongodb.lang.NonNull Task<List<Document>> task) {
                                    if (task.isSuccessful()) {
                                        List<Document> items = task.getResult();
                                        for (Document item: items) {
                                            ArrayList newloc= (ArrayList) item.get("location");
                                            if(newloc.size()!=0){
                                                Double newlat = (Double)newloc.get(0);
                                                Double newlon = (Double) newloc.get(1);
                                                double distance=distance(lat,lon,newlat,newlon);
                                                 if (distance < 5) {
                                                    Document contacts=new Document();
                                                    contacts.append("id", item.get("_id").toString()).append("date", new BsonDateTime(System.currentTimeMillis()));
                                                    if( !inSideArrayList(contacts, contact_people)){
                                                        Log.d("list", String.valueOf(contact_people.size()));
                                                        Log.d("hh",  item.get("_id").toString());
                                                        Document update2 = new Document().append("$push",
                                                                new Document().append("contacts", contacts)
                                                        );
                                                        final Task<RemoteUpdateResult> updateTask2 =
                                                                usersCollection.updateOne(new Document().append("username",preferences.getString("username", null)), update2);
                                                        updateTask.addOnCompleteListener(new OnCompleteListener<RemoteUpdateResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<RemoteUpdateResult> task) {
                                                                if (task.isSuccessful()) {
                                                                    contact_people.add(new Document().append("_id",item.get("_id").toString()).append("date", new BsonDateTime(System.currentTimeMillis())));
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

                                                }
                                        }}


                                        }
                                    }
                                }
                            );
                                    }}});



                        }
                    }

                        // query to see if anyone is nearby
                        // compare it with other locations
                        // update database
                        try {
                            Thread.sleep(refreshRate * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        count += 1;

                    jobFinished(params, false);
                    count = 0;
                }
            }
        }).start();
    }

    private boolean inSideArrayList(Document contacts, ArrayList<Document> contact_people) {
        if (contact_people.size()==0)
            return false;
        for (Document a : contact_people){
            if (a.get("_id").equals(contacts.get("id"))){
                Calendar c = Calendar.getInstance();
                Date d =new Date(System.currentTimeMillis());
                c.setTime(d);
                c.add(Calendar.DATE, -1);
                d.setTime( c.getTime().getTime() );
                long millisec = d.getTime();
                if(a.getDate("date").before(new Date(millisec)))
                    return false;
                else
                    return true;}}
        return false;
    }

    private void getLocation() {
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

    private static double distance(double lat1, double lon1, double lat2, double lon2) {

        double radEarth = 6378.137;
        double deltaLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double deltaLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) + Math.cos(lat1 * Math.PI / 180)
                * Math.cos(lat2 * Math.PI / 180) * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = radEarth * c;
        return round(distance * 1000);
    }

    private static double round(double value) {
        double scale = Math.pow(10, 5);
        return Math.round(value * scale) / scale;
    }
}
