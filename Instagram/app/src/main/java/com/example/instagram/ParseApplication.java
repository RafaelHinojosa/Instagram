package com.example.instagram;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    // Get secret api keys
    public static final String APPLICATION_ID = BuildConfig.APPLICATION_ID;
    public static final String CLIENT_KEY = BuildConfig.CLIENT_KEY;
    public static final String SERVER = BuildConfig.SERVER;

    // Initialize the connection with Parse Database
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(APPLICATION_ID)
                .clientKey(CLIENT_KEY)
                .server(SERVER)
                .build()
        );
    }
}
