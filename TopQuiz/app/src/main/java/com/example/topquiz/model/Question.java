package com.example.topquiz.model;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class Question {

    private String mQuestion;
    private List<Pair<Integer, String>> mAnswerList;
    private int mAnswerCorrectIndex;

    public Question(String mQuestion, List<String> mAnswerList, int mAnswerCorrectIndex) {
        this.mQuestion = mQuestion;
        this.mAnswerCorrectIndex = mAnswerCorrectIndex;
        this.mAnswerList= new ArrayList<>();
        for( String answer : mAnswerList ) {
            this.mAnswerList.add( new Pair<Integer, String>(this.mAnswerList.size(), answer));
        }
    }

    public String getmQuestion() {
        return mQuestion;
    }

    public void setmQuestion(String mQuestion) {
        this.mQuestion = mQuestion;
    }

    public List<Pair<Integer, String>> getmAnswerList() {
        return mAnswerList;
    }

    public void setmAnswerList(List<Pair<Integer, String>> mAnswerList) {
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
        String itemList = "";
        for( Pair item : mAnswerList) {
            itemList += (itemList.isEmpty() ? "" :  "\n") +
                    item.first + " - " + item.second;
        }
        return "Question:" +
                " mQuestion='" + mQuestion +
                " mAnswerIndex=" + mAnswerCorrectIndex +
                " mAnswerList=\n" + itemList;
    }
}
