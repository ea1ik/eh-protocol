package com.example.ehprotocol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout usernameText, passwordText;
    private TextView newUser, forgotPass, errorText;
    private Button loginButton;
    private CheckBox rememberMeCB;
    DatabaseReference users;

    private static boolean rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        users = database.getReference("users");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = (TextInputLayout) findViewById(R.id.userTextInput);
        passwordText = (TextInputLayout) findViewById(R.id.passwordTextInput);

        newUser = findViewById(R.id.newUserTextView);
        forgotPass = findViewById(R.id.forgotPasswordTextView);
        errorText = findViewById(R.id.errorTextView);

        loginButton = findViewById(R.id.loginButton);

        rememberMeCB = findViewById(R.id.rememberMeCB);

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                signIn(usernameText.getEditText().getText().toString().trim(),passwordText.getEditText().getText().toString());

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

    private void signIn(final String username, final String password) {
        users.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(username).exists()){
                    User login = dataSnapshot.child(username).getValue(User.class);
                    if (login.password.equals(password)){
                        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                        startActivity(intent);
                    }
                     else {
                            passwordText.setError("Invalid username or password.");
                        }

                } else {
                    usernameText.setError("Invalid username or password.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
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
