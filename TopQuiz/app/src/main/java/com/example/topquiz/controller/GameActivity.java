package com.example.topquiz.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topquiz.R;
import com.example.topquiz.model.Question;
import com.example.topquiz.model.Quiz;

import java.util.Arrays;

import static com.example.topquiz.controller.MainActivity.SHARED_CURRENT_USER;
import static com.example.topquiz.controller.MainActivity.SHARED_LAST_SCORE;
import static com.example.topquiz.controller.MainActivity.TOPQUIZZ;
import static com.example.topquiz.controller.MainActivity.SHARED_LAST_USER;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mNameText;
    private TextView mScoreText;
    private TextView mQuestionText;
    private Button mAnswerButton01;
    private Button mAnswerButton02;
    private Button mAnswerButton03;
    private Button mAnswerButton04;

    private Quiz mQuiz;

    // internal varirable for game's logic management
    private boolean mEnableTouchEvents = true;
    private String mName;
    private int mQuestionsLast;
    private int mScore;

    // return value to MAIN activity
    public static final String GAME_ACTIVITY_SCORE = "GAME_ACTIVITY_SCORE";

    // saved instance's entities in case of rotation for instance
    public static final String BUNDLE_STATE_NAME = "name";
    public static final String BUNDLE_STATE_SCORE = "currentScore";
    public static final String BUNDLE_STATE_QUESTION = "currentQuestion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(">>> GameActivity::onCreate()");
        setContentView(R.layout.activity_game);

        // set view elements
        mNameText = (TextView) findViewById(R.id.activity_name_text);
        mScoreText = (TextView) findViewById(R.id.activity_score_text);
        mQuestionText = (TextView) findViewById(R.id.activity_game_question_text);
        mAnswerButton01 = (Button) findViewById(R.id.activity_game_answer1_btn);
        mAnswerButton02 = (Button) findViewById(R.id.activity_game_answer2_btn);
        mAnswerButton03 = (Button) findViewById(R.id.activity_game_answer3_btn);
        mAnswerButton04 = (Button) findViewById(R.id.activity_game_answer4_btn);

        // Use the same listener for the four buttons.
        // The tag value will be used to distinguish the button triggered
        mAnswerButton01.setOnClickListener(this);
        mAnswerButton02.setOnClickListener(this);
        mAnswerButton03.setOnClickListener(this);
        mAnswerButton04.setOnClickListener(this);

        // Use the tag property to 'name' the buttons
        mAnswerButton01.setTag(0);
        mAnswerButton02.setTag(1);
        mAnswerButton03.setTag(2);
        mAnswerButton04.setTag(3);

        // generate Quiz
        mQuiz = this.generateQuiz();
        System.out.println("mQuiz: " + mQuiz);

        if (savedInstanceState != null) {
            System.out.println(">>> Game/Bundle info: " + savedInstanceState);
            mName = savedInstanceState.getString(BUNDLE_STATE_NAME);
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            mQuestionsLast = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
        } else {
            System.out.println(">>> Game/preference contents : " + getSharedPreferences(TOPQUIZZ, MODE_PRIVATE).getAll());
            mName = getSharedPreferences(TOPQUIZZ, MODE_PRIVATE).getString(SHARED_CURRENT_USER, "");
            mScore = 0;
            mQuestionsLast = mQuiz.getmQuestionList().size();
        }
        System.out.println(">>> name: " + mName);
        //System.out.println(">>> score: " + mScore);
        //System.out.println(">>> questionIndex: " + mQuestionsLast);

        // display name, score and get new Question
        displayName();
        displayScore();
        displayQuestion();
    }

    @Override
    public void onClick(View v) {
        System.out.println(">>> GameActivity::onClick()");
        int answerTag;
        answerTag = (int) v.getTag();
        //int correctTag = mQuiz.getmQuestionList().get(mQuiz.getmQuestionIndex()).getmAnswerCorrectIndex();
        int correctTag = mQuiz.getmQuestionList().get(mQuiz.getmQuestionIndex()).getmAnswerCorrectIndex();
        System.out.println(">>> answerTag = " + answerTag + " - correctTag = " + correctTag);

        Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        View view = toast.getView();
        TextView text = (TextView) view.findViewById(android.R.id.message);
        // Here you can do anything with above textview like text.setTextColor(Color.parseColor("#000000"));
        text.setTextSize(text.getTextSize() + 1);
        if (answerTag == correctTag) {
            System.out.println(">>> Answer is correct !!");
            text.setText("Correct!");
            text.setTextColor(Color.GREEN);
            this.mScore++;
            //Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println(">>> Answer is wrong !!");
            text.setText("Wrong!");
            text.setTextColor(Color.RED);
            //Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
        }
        toast.show();

        mEnableTouchEvents = false;

        // sleep 2 sec
        //try { Thread.sleep(2000); }
        //catch (InterruptedException e) { e.printStackTrace(); }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mEnableTouchEvents = true;

                // reset Quiz Index
                mQuiz.setNextQuizIndex();

                // check game end
                if (mQuestionsLast == 1) {
                    endGame();
                } else {
                    // Decrease number of Question to display
                    mQuestionsLast--;
                    displayScore();
                    // get new Question and display score
                    displayQuestion();
                }
            }
        }, 2000); // LENGTH_SHORT is usually 2 second long
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }

    private Quiz generateQuiz() {
        Question question1 = new Question("What is the name of the current french president?",
                Arrays.asList("François Hollande", "Emmanuel Macron", "Jacques Chirac", "François Mitterand"),
                1);

        Question question2 = new Question("How many countries are there in the European Union?",
                Arrays.asList("15", "24", "28", "32"),
                2);

        Question question3 = new Question("Who is the creator of the Android operating system?",
                Arrays.asList("Andy Rubin", "Steve Wozniak", "Jake Wharton", "Paul Smith"),
                0);

        Question question4 = new Question("When did the first man land on the moon?",
                Arrays.asList("1958", "1962", "1967", "1969"),
                3);

        Question question5 = new Question("What is the capital of Romania?",
                Arrays.asList("Bucarest", "Warsaw", "Budapest", "Berlin"),
                0);

        Question question6 = new Question("Who did the Mona Lisa paint?",
                Arrays.asList("Michelangelo", "Leonardo Da Vinci", "Raphael", "Carravagio"),
                1);

        Question question7 = new Question("In which city is the composer Frédéric Chopin buried?",
                Arrays.asList("Strasbourg", "Warsaw", "Paris", "Moscow"),
                2);

        Question question8 = new Question("What is the country top-level domain of Belgium?",
                Arrays.asList(".bg", ".bm", ".bl", ".be"),
                3);

        Question question9 = new Question("What is the house number of The Simpsons?",
                Arrays.asList("42", "101", "666", "742"),
                3);

        return new Quiz(Arrays.asList(question1,
                question2,
                question3,
                question4,
                question5,
                question6,
                question7,
                question8,
                question9));
    }

    private void displayQuestion() {
        Question question = mQuiz.getmQuestionList().get(mQuiz.getmQuestionIndex());
        // Set the text for the question text view and the four buttons
        //String questionText = question.getmQuestion() + "\n[" + (question.getmAnswerCorrectIndex() + 1) + "]";
        String questionText = question.getmQuestion();
        mQuestionText.setText(questionText);
        mAnswerButton01.setText(question.getmAnswerList().get(0).second);
        mAnswerButton02.setText(question.getmAnswerList().get(1).second);
        mAnswerButton03.setText(question.getmAnswerList().get(2).second);
        mAnswerButton04.setText(question.getmAnswerList().get(3).second);
    }

    private void displayName() {
        mNameText.setText(mName);
    }

    private void displayScore() {
        System.out.println(">>> GameActivity::displayScore()");
        System.out.println(">>> score: " + mScore);
        System.out.println(">>> questionIndex: " + mQuestionsLast);
        int questionMax = mQuiz.getmQuestionList().size();
        int questionIndex = questionMax - mQuestionsLast + 1;
        mScoreText.setText("Score: " + mScore + " Question: " + questionIndex + "/" + questionMax);
    }

    private void endGame() {
        System.out.println(">>> GameActivity::endGame()");
        // store lastUser and lastScore for next run
        // getPreferences(MODE_PRIVATE).edit().putString(MAIN_LAST_USER, mUser.getmFirstName()).apply();
        // getPreferences(MODE_PRIVATE).edit().putInt(MAIN_LAST_SCORE, mUser.getmScore()).apply();
        getSharedPreferences(TOPQUIZZ, MODE_PRIVATE).edit().putString(SHARED_LAST_USER, mName).apply();
        getSharedPreferences(TOPQUIZZ, MODE_PRIVATE).edit().putInt(SHARED_LAST_SCORE, mScore).apply();
        System.out.println(">>> Game/preference contents : " + getSharedPreferences(TOPQUIZZ, MODE_PRIVATE).getAll());

        // display alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Well done!")
                .setMessage("Your score is " + mScore)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(GAME_ACTIVITY_SCORE, mScore);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .create()
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println(">>> GameActivity::onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println(">>> GameActivity::onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println(">>> GameActivity::onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println(">>> GameActivity::onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println(">>> GameActivity::onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println(">>> GameActivity::onDestroy()");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(BUNDLE_STATE_NAME, mName);
        outState.putInt(BUNDLE_STATE_SCORE, mScore);
        outState.putInt(BUNDLE_STATE_QUESTION, mQuestionsLast);
        super.onSaveInstanceState(outState);
    }
}
