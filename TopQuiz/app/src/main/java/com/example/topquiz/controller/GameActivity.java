package com.example.topquiz.controller;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.topquiz.R;
import com.example.topquiz.model.Question;
import com.example.topquiz.model.Quiz;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.topquiz.controller.MainActivity.SHARED_CURRENT_USER;
import static com.example.topquiz.controller.MainActivity.SHARED_LAST_SCORE;
import static com.example.topquiz.controller.MainActivity.SHARED_LAST_USER;
import static com.example.topquiz.controller.MainActivity.TOPQUIZZ;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView nameText;
    private TextView scoreText;
    private TextView questionText;
    private Button answerButton01;
    private Button answerButton02;
    private Button answerButton03;
    private Button answerButton04;

    private Quiz quiz;

    // internal varirable for game's logic management
    private boolean enableTouchEvents = true;
    private String name;
    //private int questionsLast;
    private int score;

    // return value to MAIN activity
    public static final String GAME_ACTIVITY_SCORE = "GAME_ACTIVITY_SCORE";

    // saved instance's entities in case of rotation for instance
    public static final String BUNDLE_STATE_NAME = "name";
    public static final String BUNDLE_STATE_CURRENT_SCORE = "currentScore";
    public static final String BUNDLE_STATE_QUIZ_QUESTION_ORDER = "quizQuestionOrder";
    public static final String BUNDLE_STATE_QUIZ_QUESTION_INDEX = "quizQuestionIndex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println(">>> GameActivity::onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // set view elements
        nameText = (TextView) findViewById(R.id.activity_name_text);
        scoreText = (TextView) findViewById(R.id.activity_score_text);
        questionText = (TextView) findViewById(R.id.activity_game_question_text);
        answerButton01 = (Button) findViewById(R.id.activity_game_answer1_btn);
        answerButton02 = (Button) findViewById(R.id.activity_game_answer2_btn);
        answerButton03 = (Button) findViewById(R.id.activity_game_answer3_btn);
        answerButton04 = (Button) findViewById(R.id.activity_game_answer4_btn);

        // Use the same listener for the four buttons.
        // The tag value will be used to distinguish the button triggered
        answerButton01.setOnClickListener(this);
        answerButton02.setOnClickListener(this);
        answerButton03.setOnClickListener(this);
        answerButton04.setOnClickListener(this);

        // Use the tag property to 'name' the buttons
        answerButton01.setTag(0);
        answerButton02.setTag(1);
        answerButton03.setTag(2);
        answerButton04.setTag(3);

        // generate Quiz
        quiz = this.generateQuiz();

        if (savedInstanceState != null) {
            System.out.println(">>> Game/Bundle info: " + savedInstanceState);
            name = savedInstanceState.getString(BUNDLE_STATE_NAME);
            score = savedInstanceState.getInt(BUNDLE_STATE_CURRENT_SCORE);
            quiz.setQuestionsOrder(savedInstanceState.getIntegerArrayList(BUNDLE_STATE_QUIZ_QUESTION_ORDER));
            quiz.setCurrentIndex(savedInstanceState.getInt(BUNDLE_STATE_QUIZ_QUESTION_INDEX));
        } else {
            System.out.println(">>> Game/preference contents : " + getSharedPreferences(TOPQUIZZ, MODE_PRIVATE).getAll());
            name = getSharedPreferences(TOPQUIZZ, MODE_PRIVATE).getString(SHARED_CURRENT_USER, "");
            score = 0;
        }
        System.out.println(">>> name: " + name);
        System.out.println(">>> mQuiz: " + quiz);

        // display name, score and get new Question
        displayName();
        displayScore();
        displayQuestion();
    }

    @Override
    public void onClick(View v) {
        System.out.println(">>> GameActivity::onClick()");
        int answerTag = (int) v.getTag();
        int answerIndex = quiz.getCurrentQuestion().getAnswerIndex(answerTag);
        int correctTag = quiz.getCurrentQuestion().getAnswerCorrectIndex();
        System.out.println(">>> answerTag = " + answerTag + " -> answerIndex = " + answerIndex + " | correctTag = " + correctTag);

        Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        View view = toast.getView();
        TextView text = (TextView) view.findViewById(android.R.id.message);
        // Here you can do anything with above textview like text.setTextColor(Color.parseColor("#000000"));
        text.setTextSize(text.getTextSize() + 1);
        if (answerIndex == correctTag) {
            System.out.println(">>> Answer is correct !!");
            text.setText("Correct!");
            text.setTextColor(Color.GREEN);
            this.score++;
            //Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println(">>> Answer is wrong !!");
            text.setText("Wrong!");
            text.setTextColor(Color.RED);
            //Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
        }
        toast.show();

        enableTouchEvents = false;

        // sleep 2 sec
        //try { Thread.sleep(2000); }
        //catch (InterruptedException e) { e.printStackTrace(); }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                enableTouchEvents = true;
                // check game end
                if ( quiz.getCurrentIndex() == quiz.getQuestionListSize() - 1 ) {
                    endGame();
                } else {
                    // pull next Quiz Index
                    quiz.setNextQuizIndex();
                    // Decrease number of Question to display
                    //mQuestionsLast--;
                    // get display score and new Question
                    displayScore();
                    displayQuestion();
                }
            }
        }, 2000); // LENGTH_SHORT is usually 2 second long
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return enableTouchEvents && super.dispatchTouchEvent(ev);
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
        System.out.println(">>> GameActivity::displayScore()");
        Question question = quiz.getCurrentQuestion();
        // Set the text for the question text view and the four buttons
        questionText.setText(question.getQuestion());
        answerButton01.setText(question.getAnswerString(0));
        answerButton02.setText(question.getAnswerString(1));
        answerButton03.setText(question.getAnswerString(2));
        answerButton04.setText(question.getAnswerString(3));
    }

    private void displayName() {
        nameText.setText(name);
    }

    private void displayScore() {
        System.out.println(">>> GameActivity::displayScore()");
        System.out.println(">>> score: " + score);
        System.out.println(">>> questionIndex: " + quiz.getCurrentIndex());
        //int questionMax = quiz.getQuestionListSize();
        //int questionIndex = quiz.getCurrentIndex();
        scoreText.setText("Score: " + score +
                " Question: " + (quiz.getCurrentIndex() + 1) + "/" + quiz.getQuestionListSize());
    }

    private void endGame() {
        System.out.println(">>> GameActivity::endGame()");
        // store lastUser and lastScore for next run
        // getPreferences(MODE_PRIVATE).edit().putString(MAIN_LAST_USER, mUser.getmFirstName()).apply();
        // getPreferences(MODE_PRIVATE).edit().putInt(MAIN_LAST_SCORE, mUser.getmScore()).apply();
        getSharedPreferences(TOPQUIZZ, MODE_PRIVATE).edit().putString(SHARED_LAST_USER, name).apply();
        getSharedPreferences(TOPQUIZZ, MODE_PRIVATE).edit().putInt(SHARED_LAST_SCORE, score).apply();
        System.out.println(">>> Game/preference contents : " + getSharedPreferences(TOPQUIZZ, MODE_PRIVATE).getAll());

        // display alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Well done, " + name + "!")
                .setMessage("Your score is " + score)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(GAME_ACTIVITY_SCORE, score);
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
    protected void onSaveInstanceState(Bundle outState) {
        if (outState == null) return;
        outState.putString(BUNDLE_STATE_NAME, name);
        outState.putInt(BUNDLE_STATE_CURRENT_SCORE, score);
        outState.putIntegerArrayList(BUNDLE_STATE_QUIZ_QUESTION_ORDER, new ArrayList<Integer>(quiz.getQuestionsOrder()));
        outState.putInt(BUNDLE_STATE_QUIZ_QUESTION_INDEX, quiz.getCurrentIndex());
        System.out.println(outState);
        super.onSaveInstanceState(outState);
    }
}
