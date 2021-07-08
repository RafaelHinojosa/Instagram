package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instagram.databinding.ActivityLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

// Class for login activity. Connects with Parse to give a user access
public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    EditText etUsername;
    EditText etPassword;
    Button btnLogin;

    // Starts the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // If user already logged in, go to main activity
        if(ParseUser.getCurrentUser() != null) {
            goFeedActivity();
        }

        // Associate variables with components in the view
        etUsername = binding.etUsername;
        etPassword = binding.etPassword;
        btnLogin = binding.btnLogin;


        // Set onClickListener to the button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            // Calls a function to connect with parse
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });
    }

    // Checks credentials for login
    private void loginUser(String username, String password) {
        // InBackground is preferred so that the user can't do anything while this op is running
        // This method searches for a username and password in the user table to try to log in
        Log.d(TAG, username);
        Log.d(TAG, password);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            // This is the "success" response to logInInBackground
            @Override
            public void done(ParseUser user, ParseException e) {
                // e is null if everything's ok
                if(e != null) {
                    Log.e(TAG, "Issue with login", e);
                    Toast.makeText(LoginActivity.this, "Credentials are not correct!", Toast.LENGTH_SHORT).show();
                    return;
                }
                goFeedActivity();
                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // User goes to FeedActivity class
    private void goFeedActivity() {
        Intent i = new Intent(this, FeedActivity.class);
        startActivity(i);
    }
}