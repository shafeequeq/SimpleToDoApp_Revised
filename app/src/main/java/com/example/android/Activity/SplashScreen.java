package com.example.android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
// Does nothing but launch the main Activity. Demo Splash Screen.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(i);

        // close this activity
        finish();
    }
}
