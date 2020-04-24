package com.example.ehprotocol;

import android.app.Application;

import com.firebase.client.Firebase;

public class EHProtocol extends Application {

    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
