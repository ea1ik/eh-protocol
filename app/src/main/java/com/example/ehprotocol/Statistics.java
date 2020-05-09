package com.example.ehprotocol;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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

public class Statistics extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "FrontendDebug";
    private RequestQueue queue;

    private Button changeDate;
    private TextView totalCasesTextView, totalDeathsTextView, totalRecoveriesTextView,
            totalActiveTextView, newCasesTextView, newDeathsTextView, newRecoveriesTextView,
            currentDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        queue= Volley.newRequestQueue(this);

        changeDate = findViewById(R.id.datePicker);

        totalCasesTextView = findViewById(R.id.totalCases);
        totalDeathsTextView = findViewById(R.id.totalDeaths);
        totalRecoveriesTextView = findViewById(R.id.totalRecoveries);
        totalActiveTextView = findViewById(R.id.totalActive);
        newCasesTextView = findViewById(R.id.newCases);
        newDeathsTextView = findViewById(R.id.newDeaths);
        newRecoveriesTextView = findViewById(R.id.newRecoveries);

        currentDateTextView = findViewById(R.id.currentDate);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);

        try {
            getDayByDayStats(c);
        } catch (Exception e) {

        }

        changeDate.setOnClickListener(e -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        try {
            getDayByDayStats(c);
        } catch (Exception e) {

        }
    }

    private void getDayByDayStats(Calendar c) throws Exception {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        month++;

        String currDate = "" + year + "-" + (month < 10 ? "0" : "") + month + "-" + (day < 10 ? "0" : "") + day
                + "T00:00:00Z";

        String url = "https://api.covid19api.com/total/country/lebanon";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray allData) {
                        try {
                            JSONObject dayData;

                            for (int i = 0; i < allData.length(); i++) {
                                dayData = allData.getJSONObject(i);
                                String date = dayData.getString("Date");

                                if (date.equals(currDate)) {
                                    int totalCases, totalActive, totalDeaths, totalRecovered;
                                    totalCases = dayData.getInt("Confirmed");
                                    totalActive = dayData.getInt("Active");
                                    totalDeaths = dayData.getInt("Deaths");
                                    totalRecovered = dayData.getInt("Recovered");

                                    JSONObject prevDayData = allData.getJSONObject(i - 1);
                                    int dailyCases, dailyDeaths, dailyRecoveries;
                                    int prevCases, prevDeaths, prevRecoveries;

                                    prevCases = prevDayData.getInt("Confirmed");
                                    prevDeaths = prevDayData.getInt("Deaths");
                                    prevRecoveries = prevDayData.getInt("Recovered");

                                    dailyCases = totalCases - prevCases;
                                    dailyDeaths = totalDeaths - prevDeaths;
                                    dailyRecoveries = totalRecovered - prevRecoveries;

                                    totalCasesTextView.setText(""+totalCases);
                                    totalActiveTextView.setText(""+totalActive);
                                    totalDeathsTextView.setText(""+totalDeaths);
                                    totalRecoveriesTextView.setText(""+totalRecovered);
                                    newCasesTextView.setText(""+dailyCases);
                                    newDeathsTextView.setText(""+dailyDeaths);
                                    newRecoveriesTextView.setText(""+dailyRecoveries);
                                    currentDateTextView.setText(DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime()));
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }
}
