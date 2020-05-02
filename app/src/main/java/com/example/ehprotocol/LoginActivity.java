package com.example.ehprotocol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

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
                Firebase userRef= new Firebase("https://ehprotocol.firebaseio.com/users/");
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference userNameRef = rootRef.child("users");
                Query queries=userNameRef.orderByChild("Username").equalTo(usernameText.getEditText().getText().toString().trim());
                boolean[] flag = new boolean[1];
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()) {
                            flag[0] =false;
                            usernameText.setError("Invalid username.");
                        }
                        else
                            flag[0]=true;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                queries.addListenerForSingleValueEvent(eventListener);
                if (flag[0]){
                    Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                    // intent.putExtra("username", username);
                    startActivity(intent);
                }


                }
            }
        );

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
