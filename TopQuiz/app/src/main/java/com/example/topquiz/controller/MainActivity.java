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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.topquiz.R;
import com.example.topquiz.model.User;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView mGreetingText;
    private EditText mNameInput;
    private TextView mNameProgressText;
    private Button mPlayButton;
    private TextView mLastScoreText;
    private User mUser;

    // shared preference storage name
    public static final String TOPQUIZZ = "TOPQUIZZ";
    private SharedPreferences mShared;
    // user and score within shared preferences storage
    //public static final String MAIN_LAST_USER = "firstName";
    //public static final String MAIN_LAST_SCORE = "lastScore";
    public static final String SHARED_LAST_USER = "lastName";
    public static final String SHARED_LAST_SCORE = "lastScore";
    public static final String SHARED_CURRENT_USER = "currentName";
    public static final String SHARED_CURRENT_SCORE = "currentScore";
    // define request code to get score when return to MAIN activity
    private static final int GAME_ACTIVITY_REQUEST_CODE = 42;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(">>> MainActivity::onActivityResult()");
        if (data != null && requestCode == GAME_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fetch the score from the Intent
            System.out.println(">>> Game Intent: " + data);
            mUser.setmScore(data.getIntExtra(GameActivity.GAME_ACTIVITY_SCORE, 0));
            mLastScoreText.setText(mUser.getmFirstName() + ", Your previous score is " + mUser.getmScore() + " points");
            System.out.println(">>> previous score is: " + mUser.getmScore());

            // handle display
            greetUser();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(">>> MainActivity::onActivityResult()");
        mShared = getSharedPreferences(TOPQUIZZ, MODE_PRIVATE);
        cleanSharedPreferences();

        setContentView(R.layout.activity_main);

        // set view elements
        mGreetingText = findViewById(R.id.activity_main_greeting_txt);
        mNameInput = findViewById(R.id.activity_main_name_input);
        mNameProgressText = findViewById(R.id.activity_main_name_progress_txt);
        mPlayButton = findViewById(R.id.activity_main_play_btn);
        mLastScoreText = findViewById(R.id.activity_main_last_score_txt);

        //set model attributes
        mUser = new User("", 0);

        // disable button
        mPlayButton.setEnabled(false);

        // handle display
        greetUser();

        // add keyEvent on text input
        mNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNameProgressText.setText(mNameInput.getText());
                mPlayButton.setEnabled(s.toString().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // add actionEvent on Button
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(">>> MainActivity::setOnClickListener()");

                // The user just clicked
                mUser.setmFirstName(mNameInput.getText().toString());

                // store User and last score for GAME activity
                mShared.edit().putString(SHARED_CURRENT_USER, mUser.getmFirstName()).apply();
                //mShared.edit().putInt(SHARED_LAST_SCORE, mUser.getmScore()).apply();
                System.out.println(">>> Main/preference contents : " + mShared.getAll());

                // clean TextView
                mNameInput.setText("");

                // start Game
                Intent gameActivity = new Intent(MainActivity.this, GameActivity.class);
                //startActivity(gameActivity);
                startActivityForResult(gameActivity, GAME_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private void greetUser() {
        String greetingMessage = "Bienvenue dans TopQuiz!\nQuel est votre prénom ?";
        String firstName = mUser.getmFirstName();

        // check user's name
        if (firstName == null || firstName.isEmpty()) {
            // not assigned yet, check in shared preferences
            //firstName = getPreferences(MODE_PRIVATE).getString(MAIN_LAST_USER, null);
            firstName = mShared.getString(SHARED_LAST_USER, null);
            if (firstName == null || firstName.isEmpty()) {
                // store is empty then display basic greeting message
                mGreetingText.setText(greetingMessage);
                return;
            }
            mUser.setmFirstName(firstName);
        }

        // assign last score if any
        //mUser.setmScore(getPreferences(MODE_PRIVATE).getInt(MAIN_LAST_SCORE, 0));
        mUser.setmScore(mShared.getInt(SHARED_LAST_SCORE, 0));

        // greeting mgt
        greetingMessage = "Welcome back, " + mUser.getmFirstName() + "!";
        greetingMessage += "\nYour last score was " + mUser.getmScore() + ", will you do better this time ?";
        mGreetingText.setText(greetingMessage);

        // assign inputText mgt and caret/cursor position
        mNameInput.setText(firstName);
        mNameInput.setSelection(firstName.length());

        // enable button
        mPlayButton.setEnabled(true);
    }

    private void cleanSharedPreferences() {
        System.out.println(">>> cleanSharedPreferences()");
        System.out.println(">>> BEFORE : Main/preference contents : " + mShared.getAll());

        // backup shared Map
        Map<String, ?> sharedMap = mShared.getAll();
        if ( sharedMap == null || sharedMap.isEmpty() ) return;

        // clear shared preferences
        mShared.edit().clear().apply();
        // assign back lastUser
        String lastUser = sharedMap.containsKey(SHARED_LAST_USER) ?
                sharedMap.get(SHARED_LAST_USER).toString() : "";
        mShared.edit().putString(SHARED_LAST_USER, lastUser).apply();
        // assigne back lastScore
        int lastScore = sharedMap.containsKey(SHARED_LAST_SCORE) ?
                Integer.parseInt(sharedMap.get(SHARED_LAST_SCORE).toString()) : 0;
        mShared.edit().putInt(SHARED_LAST_SCORE, lastScore).apply();
        // reset currentUSer
        mShared.edit().putString(SHARED_CURRENT_USER, "").apply();

        System.out.println(">>> AFTER : Main/preference contents : " + mShared.getAll());
    }
}
