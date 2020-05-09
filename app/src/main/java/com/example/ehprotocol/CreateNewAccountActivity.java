package com.example.ehprotocol;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;

import org.bson.Document;

import java.util.Arrays;

// Base Stitch Packages
// Stitch Authentication Packages
// MongoDB Service Packages
// Utility Packages

public class CreateNewAccountActivity extends AppCompatActivity {
    final String Debugg = "stuff";
    private TextInputLayout usernameInput, passwordInput, confirmPasswordInput;
    private String username, password, confirmPassword;
    private boolean remember;

    private Button createAccount;

    private StitchAppClient stitchClient;
    private RemoteMongoClient mongoClient;
    private RemoteMongoCollection usersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Stitch.getDefaultAppClient().getAuth().loginWithCredential(new AnonymousCredential()).addOnCompleteListener(new OnCompleteListener<StitchUser>() {
            @Override
            public void onComplete(@NonNull final Task<StitchUser> task) {
                if (task.isSuccessful()) {
                    Log.d("stitch", "logged in anonymously");
                } else {
                    Log.e("stitch", "failed to log in anonymously", task.getException());
                }
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);

        stitchClient = Stitch.getDefaultAppClient();
        mongoClient = stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        usersCollection = mongoClient.getDatabase("COVID19ContactTracing").getCollection("Users");

        remember = true;

        usernameInput = findViewById(R.id.usernameField);
        passwordInput = findViewById(R.id.passwordField);
        confirmPasswordInput = findViewById(R.id.passwordConfField);

        createAccount = findViewById(R.id.createAccountButton);


        createAccount.setOnClickListener(e -> {
            if (validatePassword() & validateUsername() & confirmPassword()) {
                username = usernameInput.getEditText().getText().toString().trim();
                password = passwordInput.getEditText().getText().toString();

                Document filterDoc = new Document()
                        .append("username", username);
                usersCollection.count(filterDoc).addOnCompleteListener(new OnCompleteListener<Long>() {
                    @Override
                    public void onComplete(@NonNull Task<Long> task) {
                        if (task.isSuccessful()) {
                            Long numDocs = task.getResult();
                            Log.d(Debugg, numDocs.toString());
                            if (numDocs.longValue() == 0l) {
                                Log.d(Debugg, "inside");
                                Document newUser = new Document()
                                        .append("username", username)
                                        .append("password", password)
                                        .append("contacts", Arrays.asList()
                                        );
                                usersCollection.insertOne(newUser);
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Log.d(Debugg, "inside else");
                                usernameInput.setError("Username already exists.");
                            }
                        }
                    }
                });
            }
        });

    }

    private boolean validatePassword() {
        username = usernameInput.getEditText().getText().toString().trim();
        password = passwordInput.getEditText().getText().toString();
        if (password.isEmpty()) {
            passwordInput.setError("Field is empty");
            return false;
        }
        if (password.length() < 8) {
            passwordInput.setError("Password must be at least 8 characters.");
            return false;
        }
        passwordInput.setError(null);
        return true;
    }

    private boolean confirmPassword() {
        confirmPassword = confirmPasswordInput.getEditText().getText().toString();
        if (confirmPassword.isEmpty()) {
            confirmPasswordInput.setError("Field is empty");
            return false;
        }
        if (!confirmPassword.equals(password)) {
            confirmPasswordInput.setError("Passwords do not match");
            return false;
        }
        confirmPasswordInput.setError(null);
        return true;
    }

    private boolean validateUsername() {
        username = usernameInput.getEditText().getText().toString().trim();
        if (username.isEmpty()) {
            usernameInput.setError("Field is empty");
            return false;
        }
        if (username.length() > 15) {
            usernameInput.setError("Username too long");
            return false;
        }
        usernameInput.setError(null);
        return true;
    }

}
