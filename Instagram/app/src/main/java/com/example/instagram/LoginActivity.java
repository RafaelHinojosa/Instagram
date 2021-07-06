package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instagram.databinding.ActivityLoginBinding;

// Class for login activity. Connects with Parse to give a user access
public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    EditText etUsername;
    EditText etPassword;
    Button btnLogin;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

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
                checkCredentials(username, password);
            }
        });
    }

    // Checks credentials for login
    public void checkCredentials(String username, String password) {
        Log.d(TAG, "ola" + username + password);
        Toast.makeText(LoginActivity.this, "login credentials on review...", Toast.LENGTH_SHORT).show();
        Toast.makeText(LoginActivity.this, "Username = " + username, Toast.LENGTH_SHORT).show();
        Toast.makeText(LoginActivity.this, "Password = " + password, Toast.LENGTH_SHORT).show();

        return;
    }
}