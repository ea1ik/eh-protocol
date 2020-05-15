package com.example.ehprotocol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

// Base Stitch Packages
// Stitch Authentication Packages
// MongoDB Service Packages
// Utility Packages

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout usernameText, passwordText;
    private TextView newUser, forgotPass, errorText;
    private Button loginButton;
    private StitchAppClient stitchClient;
    private RemoteMongoClient mongoClient;
    private RemoteMongoCollection usersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        stitchClient = Stitch.getDefaultAppClient();
        mongoClient = stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        usersCollection = mongoClient.getDatabase("COVID19ContactTracing").getCollection("Users");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = (TextInputLayout) findViewById(R.id.userTextInput);
        passwordText = (TextInputLayout) findViewById(R.id.passwordTextInput);

        newUser = findViewById(R.id.newUserTextView);

        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    signIn(usernameText.getEditText().getText().toString().trim(), passwordText.getEditText().getText().toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        });

        newUser.setOnClickListener(e -> {
            Intent intent = new Intent(getApplicationContext(), CreateNewAccountActivity.class);
            startActivity(intent);
        });
    }

    private void signIn(final String username, final String password) throws FileNotFoundException {
        Document filterDoc = new Document().append("username", username);

        RemoteFindIterable findResults = usersCollection
                .find(filterDoc);

        Task<List<Document>> itemsTask = findResults.into(new ArrayList<Document>());
        itemsTask.addOnCompleteListener(new OnCompleteListener<List<Document>>() {
            @Override
            public void onComplete(@NonNull Task<List<Document>> task) {
                if (task.isSuccessful()) {
                    List<Document> items = task.getResult();
                    Log.d("app", String.format("successfully found %d documents", items.size()));
                    if (items.size() == 0) {
                        usernameText.setError("Invalid username or password.");
                    } else {
                        Log.d("app", items.get(0).get("password").toString());
                        if (password.equals(items.get(0).get("password").toString())) {
                            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                            startActivity(intent);
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("ID", items.get(0).get("_id").toString());
                            editor.putString("username", items.get(0).get("username").toString());
                            editor.apply();
                        } else {
                            usernameText.setError("Invalid username or password.");
                        }
                    }
                    for (Document item : items) {
                        Log.d("app", String.format("successfully found:  %s", item.toString()));
                    }
                } else {
                    usernameText.setError("Failed to connect. Try again.");
                }
            }
        });

    }

    private boolean validateUsername() {
        if (usernameText.getEditText().getText().toString().isEmpty()) {
            usernameText.setError("Field is empty");
            return false;
        }
        usernameText.setError(null);
        return true;
    }

    private boolean validatePassword() {
        if (passwordText.getEditText().getText().toString().isEmpty()) {
            passwordText.setError("Field is empty");
            return false;
        }
        passwordText.setError(null);
        return true;
    }

}