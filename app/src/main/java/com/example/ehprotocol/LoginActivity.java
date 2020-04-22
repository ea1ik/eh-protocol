package com.example.ehprotocol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private TextView usernameText, passwordText, newUser, forgotPass, errorText;
    private Button loginButton;
    private CheckBox rememberMeCB;

    private static String username = "ea1ik";
    private static String password = "123";
    private static boolean rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = (TextView) findViewById(R.id.userPlainText);
        passwordText = (TextView) findViewById(R.id.passwordPlainText);
        newUser = (TextView) findViewById(R.id.newUserTextView);
        forgotPass = (TextView) findViewById(R.id.forgotPasswordTextView);
        errorText = (TextView) findViewById(R.id.errorTextView);

        loginButton = (Button) findViewById(R.id.loginButton);

        rememberMeCB = (CheckBox) findViewById(R.id.rememberMeCB);

        loginButton.setOnClickListener(e -> {
            if (usernameText.getText().toString().equals(username) && passwordText.getText().toString().equals(password)) {
                if (rememberMeCB.isChecked())
                    rememberMe = true;
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intent);
            } else {
                errorText.setVisibility(View.VISIBLE);
            }
        });

        newUser.setOnClickListener(e -> {
            Intent intent = new Intent(getApplicationContext(), CreateNewAccountActivity.class);
            startActivity(intent);
        });

        forgotPass.setOnClickListener(e -> {
            Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }
}
