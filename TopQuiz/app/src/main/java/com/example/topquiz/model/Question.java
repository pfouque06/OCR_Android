package com.example.topquiz.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.Map;

public class Question {

    private String mQuestion;
    private Map<Integer, String> mAnswerMap;
    private int mAnswerCorrectIndex;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Question(String mQuestion, List<String> mAnswerList, int mAnswerCorrectIndex) {
        this.mQuestion = mQuestion;
        this.mAnswerCorrectIndex = mAnswerCorrectIndex;
        for( String answer : mAnswerList ) {
            this.mAnswerMap.put(mAnswerMap.size()+1, answer);
        }
    }

    public String getmQuestion() {
        return mQuestion;
    }

    public void setmQuestion(String mQuestion) {
        this.mQuestion = mQuestion;
    }

    public List<String> getmAnswerList() {
        return mAnswerList;
    }

    public void setmAnswerList(List<String> mAnswerList) {
        this.mAnswerList = mAnswerList;
    }

    public int getmAnswerCorrectIndex() {
        return mAnswerCorrectIndex;
    }

    public void setmAnswerCorrectIndex(int mAnswerCorrectIndex) {
        this.mAnswerCorrectIndex = mAnswerCorrectIndex;
    }

    @Override
    public String toString() {
        return "Question{" +
                "mQuestion='" + mQuestion + '\'' +
                ", mChoiceList=" + mAnswerList +
                ", mAnswerIndex=" + mAnswerCorrectIndex +
                '}';
    }
}
