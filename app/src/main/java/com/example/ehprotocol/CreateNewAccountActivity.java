package com.example.ehprotocol;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;

public class CreateNewAccountActivity extends AppCompatActivity {

    private TextInputLayout usernameInput, passwordInput, confirmPasswordInput;
    private String username, password, confirmPassword;
    private boolean remember;

    private Button createAccount;

    private Firebase root, users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);

        remember = true;

        usernameInput = findViewById(R.id.usernameField);
        passwordInput = findViewById(R.id.passwordField);
        confirmPasswordInput = findViewById(R.id.passwordConfField);

        createAccount = findViewById(R.id.createAccountButton);


        createAccount.setOnClickListener(e -> {
            if (validatePassword() & validateUsername() & confirmPassword() & alreadyExists()) {
                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                DatabaseReference users = mDatabase.getReference("/users/");
                DatabaseReference newChildRef = users.push();
                root = new Firebase("https://ehprotocol.firebaseio.com/users/" + newChildRef.getKey() + "/");
                Firebase usernameChild = root.child("Username");
                usernameChild.setValue(username);
                Firebase passwordChild = root.child("Password");
                passwordChild.setValue(password);
                Firebase deviceID = root.child("Device IDs");
                Firebase contacts = root.child("Contacts");
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean alreadyExists(){
        Firebase userRef= new Firebase("https://ehprotocol.firebaseio.com/users/");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userNameRef = rootRef.child("users");
        Query queries=userNameRef.orderByChild("Username").equalTo(username);
        boolean[] flag = new boolean[1];
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    flag[0] =false;
                    usernameInput.setError("Username already exists.");
                }
                else
                    flag[0]=true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        queries.addListenerForSingleValueEvent(eventListener);
        return flag[0];
    }

    private boolean validatePassword() {
        username = usernameInput.getEditText().getText().toString().trim();
        password = passwordInput.getEditText().getText().toString();
        if (password.isEmpty()) {
            passwordInput.setError("Field is empty");
            return false;
        }
        if (password.length() <8 ) {
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
