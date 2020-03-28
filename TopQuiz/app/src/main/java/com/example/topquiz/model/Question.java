package com.example.topquiz.model;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question {

    private String mQuestion;
    private List<Pair<Integer, String>> mAnswerList;
    private int mAnswerCorrectIndex;

    public Question(String mQuestion, List<String> mAnswerList, int mAnswerCorrectIndex) {
        this.mQuestion = mQuestion;
        this.mAnswerCorrectIndex = mAnswerCorrectIndex;
        this.mAnswerList= new ArrayList<>();
        for( String answer : mAnswerList ) {
            this.mAnswerList.add(new Pair<>(this.mAnswerList.size(), answer));
        }
    }

    public String getQuestion() {
        return mQuestion;
    }

    public List<Pair<Integer, String>> getAnswerList() {
        return mAnswerList;
    }

    public int getAnswerCorrectIndex() {
        return mAnswerCorrectIndex;
    }

    public int getAnswerIndex(int index) {
        return this.mAnswerList.get(index).first;
    }

    public String getAnswerString(int index) {
        return this.mAnswerList.get(index).second;
    }

    public int getAnswerListSize() {
        return this.mAnswerList.size();
    }

    public void shuffle() {
        Collections.shuffle(mAnswerList);
    }
    @Override
    public String toString() {
        String itemList = "";
        for( Pair item : mAnswerList) {
            itemList += (itemList.isEmpty() ? "" :  "\n") +
                    "(" + item.first + ")" +
                    ( (int) item.first == mAnswerCorrectIndex ? "*" : " ") +
                    item.second;
        }
        return "Question: " + mQuestion +
                " answerCorrectIndex=" + mAnswerCorrectIndex +
                " answerList=\n" + itemList;
    }
}
