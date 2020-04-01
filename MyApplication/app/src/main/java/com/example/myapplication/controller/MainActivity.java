package com.example.myapplication.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set view elements
        //new TextView(this);
        //setContentView(text);
        TextView greetingText = findViewById(R.id.activity_main_greeting_txt);
        //greetingText.setText("Bonjour, vous me devez 1 000 000€.");
        //greetingText.setText(R.string.app_name);
        String appName = getResources().getString(R.string.app_name).replace("Application", "way!");
        greetingText.setText(appName);
    }
}
