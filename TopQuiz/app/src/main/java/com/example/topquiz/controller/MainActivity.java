package com.example.topquiz.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.topquiz.R;
import com.example.topquiz.model.User;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView greetingText;
    private EditText nameInput;
    private TextView nameProgressText;
    private Button playButton;
    private TextView lastScoreText;
    private User user;

    // shared preference storage name
    public static final String TOPQUIZZ = "TOPQUIZZ";
    private SharedPreferences shared;
    // user and score within shared preferences storage
    //public static final String MAIN_LAST_USER = "firstName";
    //public static final String MAIN_LAST_SCORE = "lastScore";
    public static final String SHARED_LAST_USER = "lastName";
    public static final String SHARED_LAST_SCORE = "lastScore";
    public static final String SHARED_CURRENT_USER = "currentName";
    // define request code to get score when return to MAIN activity
    private static final int GAME_ACTIVITY_REQUEST_CODE = 42;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(">>> MainActivity::onActivityResult()");
        if (data != null && requestCode == GAME_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fetch the score from the Intent
            System.out.println(">>> Game Intent: " + data);
            user.setmScore(data.getIntExtra(GameActivity.GAME_ACTIVITY_SCORE, 0));
            lastScoreText.setText(user.getmFirstName() + ", your previous score is " + user.getmScore() + " points");
            System.out.println(">>> previous score is: " + user.getmScore());
        }

        // handle display
        greetUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(">>> MainActivity::onActivityResult()");
        shared = getSharedPreferences(TOPQUIZZ, MODE_PRIVATE);
        cleanSharedPreferences();

        setContentView(R.layout.activity_main);

        // set view elements
        greetingText = findViewById(R.id.activity_main_greeting_txt);
        nameInput = findViewById(R.id.activity_main_name_input);
        nameProgressText = findViewById(R.id.activity_main_name_progress_txt);
        playButton = findViewById(R.id.activity_main_play_btn);
        lastScoreText = findViewById(R.id.activity_main_last_score_txt);

        //set model attributes
        user = new User("", 0);

        // disable button
        playButton.setEnabled(false);

        // handle display
        greetUser();

        // add keyEvent on text input
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameProgressText.setText(nameInput.getText());
                playButton.setEnabled(s.toString().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // add actionEvent on Button
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(">>> MainActivity::setOnClickListener()");

                // The user just clicked
                user.setmFirstName(nameInput.getText().toString());

                // store User and last score for GAME activity
                shared.edit().putString(SHARED_CURRENT_USER, user.getmFirstName()).apply();
                //mShared.edit().putInt(SHARED_LAST_SCORE, mUser.getmScore()).apply();
                System.out.println(">>> Main/preference contents : " + shared.getAll());

                // clean TextView
                nameInput.setText("");

                // start Game
                Intent gameActivity = new Intent(MainActivity.this, GameActivity.class);
                //startActivity(gameActivity);
                startActivityForResult(gameActivity, GAME_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private void greetUser() {
        String greetingMessage = "Bienvenue dans TopQuiz!\nQuel est votre prénom ?";
        String firstName = user.getmFirstName();

        // check user's name
        if (firstName == null || firstName.isEmpty()) {
            // not assigned yet, check in shared preferences
            //firstName = getPreferences(MODE_PRIVATE).getString(MAIN_LAST_USER, null);
            firstName = shared.getString(SHARED_LAST_USER, null);
            if (firstName == null || firstName.isEmpty()) {
                // store is empty then display basic greeting message
                greetingText.setText(greetingMessage);
                return;
            }
            user.setmFirstName(firstName);
        }

        // assign last score if any
        //mUser.setmScore(getPreferences(MODE_PRIVATE).getInt(MAIN_LAST_SCORE, 0));
        user.setmScore(shared.getInt(SHARED_LAST_SCORE, 0));

        // greeting mgt
        greetingMessage = "Welcome back, " + user.getmFirstName() + "!";
        greetingMessage += "\nYour last score was " + user.getmScore() + ", will you do better this time ?";
        greetingText.setText(greetingMessage);

        // assign inputText mgt and caret/cursor position
        nameInput.setText(firstName);
        nameInput.setSelection(firstName.length());

        // enable button
        playButton.setEnabled(true);
    }

    private void cleanSharedPreferences() {
        System.out.println(">>> cleanSharedPreferences()");
        System.out.println(">>> BEFORE : Main/preference contents : " + shared.getAll());

        // backup shared Map
        Map<String, ?> sharedMap = shared.getAll();
        if ( sharedMap == null || sharedMap.isEmpty() ) return;

        // clear shared preferences
        shared.edit().clear().apply();
        // assign back lastUser
        String lastUser = sharedMap.containsKey(SHARED_LAST_USER) ?
                sharedMap.get(SHARED_LAST_USER).toString() : "";
        shared.edit().putString(SHARED_LAST_USER, lastUser).apply();
        // assigne back lastScore
        int lastScore = sharedMap.containsKey(SHARED_LAST_SCORE) ?
                Integer.parseInt(sharedMap.get(SHARED_LAST_SCORE).toString()) : 0;
        shared.edit().putInt(SHARED_LAST_SCORE, lastScore).apply();
        // reset currentUSer
        shared.edit().putString(SHARED_CURRENT_USER, "").apply();

        System.out.println(">>> AFTER : Main/preference contents : " + shared.getAll());
    }
}
