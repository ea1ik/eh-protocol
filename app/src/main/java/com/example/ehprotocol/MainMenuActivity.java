package com.example.ehprotocol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
                        int confirmed = mainObject.getInt("confirmed");
                        Log.d(TAG, ""+confirmed);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();

        });
    }
}