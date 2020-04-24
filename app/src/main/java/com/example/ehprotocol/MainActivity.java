package com.example.ehprotocol;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton enterButton = (ImageButton) findViewById(R.id.enterButton);

        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        enterButton.setOnClickListener(e -> {
            System.out.println("Device ID: " + id);
            Intent startIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(startIntent);
        });
    }
}
