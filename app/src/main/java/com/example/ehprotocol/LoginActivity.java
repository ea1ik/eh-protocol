package com.example.ehprotocol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout usernameText, passwordText;
    private TextView newUser, forgotPass, errorText;
    private Button loginButton;
    private CheckBox rememberMeCB;

    private static String username = "ea1ik";
    private static String password = "123";
    private static boolean rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = (TextInputLayout) findViewById(R.id.userTextInput);
        passwordText = (TextInputLayout) findViewById(R.id.passwordTextInput);

        newUser = findViewById(R.id.newUserTextView);
        forgotPass = findViewById(R.id.forgotPasswordTextView);
        errorText = findViewById(R.id.errorTextView);

        loginButton = findViewById(R.id.loginButton);

        rememberMeCB = findViewById(R.id.rememberMeCB);

        loginButton.setOnClickListener(e -> {
            if(validateUsername() & validatePassword()) {
                if (usernameText.getEditText().getText().toString().equals(username) && passwordText.getEditText().getText().toString().equals(password)) {
                    if (rememberMeCB.isChecked())
                        rememberMe = true;
                    Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
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

    private boolean validateUsername() {
        if(usernameText.getEditText().getText().toString().isEmpty()){
            usernameText.setError("Field is empty");
            return false;
        }
        usernameText.setError(null);
        return true;
    }

    private boolean validatePassword() {
        if(passwordText.getEditText().getText().toString().isEmpty()){
            passwordText.setError("Field is empty");
            return false;
        }
        passwordText.setError(null);
        return true;
    }
}
