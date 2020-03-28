package com.example.topquiz.model;

import java.util.Collections;
import java.util.List;

public class Quiz {

    private List<Question> mQuestionList;
    private int mQuestionIndex;

    public Quiz(List<Question> questionList) {
        // Shuffle the question list before storing it
        this.mQuestionList = questionList;
        resetQuiz();
    }

    public void setNextQuizIndex() {
        // Loop over the questions and return a new one at each call
        //int result = mQuestionIndex;
        mQuestionIndex = ++mQuestionIndex >= mQuestionList.size() ? 0 : mQuestionIndex;
        if ( mQuestionIndex == 0 ) resetQuiz();
        //return result;
        return;
    }

    public List<Question> getmQuestionList() {
        return mQuestionList;
    }

    public void setmQuestionList(List<Question> mQuestionList) {
        this.mQuestionList = mQuestionList;
    }

    public int getmQuestionIndex() {
        return mQuestionIndex;
    }

    public void setmQuestionIndex(int mQuestionIndex) {
        this.mQuestionIndex = mQuestionIndex;
    }

    void resetQuiz() {
        Collections.shuffle(mQuestionList);
        mQuestionIndex = 0;
    }
}
