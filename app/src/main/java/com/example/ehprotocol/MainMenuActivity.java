package com.example.ehprotocol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    private Button checkInButton, checkOutButton, testButton, aboutButton, logoutButton;
    private TextView welcomeText;

    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        checkInButton = (Button) findViewById(R.id.checkInButton);
        checkOutButton = (Button) findViewById(R.id.checkOutButton);
        testButton = (Button) findViewById(R.id.testButton);
        aboutButton = (Button) findViewById(R.id.aboutButton);
        logoutButton = (Button) findViewById(R.id.logoutButton);

        welcomeText = (TextView) findViewById(R.id.welcomeText);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            name = extras.getString("username");
            welcomeText.setText("Welcome, " + name);
        }

        logoutButton.setOnClickListener(e->{
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
    }
}
