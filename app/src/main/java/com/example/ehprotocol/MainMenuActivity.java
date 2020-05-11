package com.example.ehprotocol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {
    public static final String TAG = "FrontendDebug";

    private Button checkInButton, checkOutButton, statistics, aboutButton, logoutButton;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        checkInButton = findViewById(R.id.checkInButton);
        checkOutButton = findViewById(R.id.checkOutButton);
        statistics = findViewById(R.id.statisticsButton);
        aboutButton = findViewById(R.id.aboutButton);
        logoutButton = findViewById(R.id.logoutButton);

        Bundle extras = getIntent().getExtras();

        logoutButton.setOnClickListener(e->{
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("ID",null);
            editor.putString("username",null);
            editor.apply();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        statistics.setOnClickListener(e->{
            Intent intent = new Intent(getApplicationContext(), Statistics.class);
            startActivity(intent);
        });

        checkInButton.setOnClickListener(e->{
            Intent intent = new Intent(getApplicationContext(), CheckIn.class);
            startActivity(intent);
        });

        aboutButton.setOnClickListener(e->{


        });
    }
}