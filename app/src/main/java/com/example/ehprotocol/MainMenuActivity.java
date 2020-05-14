package com.example.ehprotocol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity implements PopUpMessage.LoggingOut {
    public static final String TAG = "FrontendDebug";

    private Button checkInButton, checkOutButton, statistics, aboutButton, logoutButton, tipsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        checkInButton = findViewById(R.id.checkInButton);
        checkOutButton = findViewById(R.id.checkOutButton);
        statistics = findViewById(R.id.statisticsButton);
        aboutButton = findViewById(R.id.aboutButton);
        logoutButton = findViewById(R.id.logoutButton);
        tipsButton = findViewById(R.id.tipsButton);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String isContact = preferences.getString("isContact", null);
        //its a stringcant be a boolean so like do whatever warninghere

        logoutButton.setOnClickListener(e -> {
            openDialog();
        });

        statistics.setOnClickListener(e -> {
            Intent intent = new Intent(getApplicationContext(), Statistics.class);
            startActivity(intent);
        });

        checkInButton.setOnClickListener(e -> {
            Intent intent = new Intent(getApplicationContext(), CheckIn.class);
            startActivity(intent);
        });

        checkOutButton.setOnClickListener(e -> {
            Intent intent = new Intent(getApplicationContext(), CheckOut.class);
            startActivity(intent);
        });

        aboutButton.setOnClickListener(e -> {
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
        });

        tipsButton.setOnClickListener(e -> {
            Intent intent = new Intent(getApplicationContext(), TipsActivity.class);
            startActivity(intent);
        });
    }

    public void openDialog() {
        PopUpMessage dialog = new PopUpMessage("Are you sure you want to logout? We personally do not advise you so, " +
                "unless you are logging in on another device.", "Yes, I'm sure", "Cancel");
        dialog.show(getSupportFragmentManager(), "Dialog");

    }

    @Override
    public void onConfirm() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ID", null);
        editor.putString("username", null);
        editor.apply();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}