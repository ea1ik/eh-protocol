package com.example.ehprotocol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class CreateNewAccountActivity extends AppCompatActivity {

    private TextInputLayout emailInput, usernameInput, passwordInput, confirmPasswordInput;
    private String email, username, password, confirmPassword;

    private Button createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);

        emailInput = (TextInputLayout) findViewById(R.id.emailField);
        usernameInput = (TextInputLayout) findViewById(R.id.usernameField);
        passwordInput = (TextInputLayout) findViewById(R.id.passwordField);
        confirmPasswordInput = (TextInputLayout) findViewById(R.id.passwordConfField);

        createAccount = (Button) findViewById(R.id.createAccountButton);

        createAccount.setOnClickListener(e -> {
            if(validateEmail() & validatePassword() & validateUsername() & confirmPassword()){

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }


    private boolean validateEmail(){
        email = emailInput.getEditText().getText().toString().trim();
        if(email.isEmpty()) {
            emailInput.setError("Field is empty");
            return false;
        }
        emailInput.setError(null);
        return true;
    }

    private boolean validatePassword(){
        password = passwordInput.getEditText().getText().toString();
        if(password.isEmpty()){
            passwordInput.setError("Field is empty");
            return false;
        }
        passwordInput.setError(null);
        return true;
    }

    private boolean confirmPassword() {
        confirmPassword = confirmPasswordInput.getEditText().getText().toString();
        if(confirmPassword.isEmpty()){
            confirmPasswordInput.setError("Field is empty");
            return false;
        }
        if(!confirmPassword.equals(password)){
            confirmPasswordInput.setError("Passwords do not match");
            return false;
        }
        confirmPasswordInput.setError(null);
        return true;
    }

    private boolean validateUsername(){
        username = usernameInput.getEditText().getText().toString().trim();
        if(username.isEmpty()) {
            usernameInput.setError("Field is empty");
            return false;
        }
        if(username.length() > 15) {
            usernameInput.setError("Username too long");
            return false;
        }
        usernameInput.setError(null);
        return true;
    }
}
