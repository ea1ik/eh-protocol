package com.example.ehprotocol;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CheckIn extends AppCompatActivity {

    private TextView code1, code2, code3, code4, code5, code6, code7, code8;
    private String TAG = "FrontendDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        code1 = findViewById(R.id.code1);
        code2 = findViewById(R.id.code2);
        code3 = findViewById(R.id.code3);
        code4 = findViewById(R.id.code4);
        code5 = findViewById(R.id.code5);
        code6 = findViewById(R.id.code6);
        code7 = findViewById(R.id.code7);
        code8 = findViewById(R.id.code8);
    }
}
