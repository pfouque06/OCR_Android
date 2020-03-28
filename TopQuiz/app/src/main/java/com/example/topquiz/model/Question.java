package com.example.topquiz.model;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question {

    private String question;
    private List<Pair<Integer, String>> answerList;
    private int answerCorrectIndex;

    public Question(String question, List<String> answerList, int answerCorrectIndex) {
        this.question = question;
        this.answerCorrectIndex = answerCorrectIndex;
        this.answerList = new ArrayList<>();
        for( String answer : answerList) {
            this.answerList.add(new Pair<>(this.answerList.size(), answer));
        }
    }

    public String getQuestion() {
        return question;
    }

//    public List<Pair<Integer, String>> getAnswerList() {
//        return answerList;
//    }

    public int getAnswerCorrectIndex() {
        return answerCorrectIndex;
    }

    public int getAnswerIndex(int index) {
        return this.answerList.get(index).first;
    }

    public String getAnswerString(int index) {
        return this.answerList.get(index).second;
    }

//    public int getAnswerListSize() {
//        return this.answerList.size();
//    }

    public void shuffle() {
        Collections.shuffle(answerList);
    }
    @Override
    public String toString() {
        String itemList = "";
        for( Pair item : answerList) {
            itemList += (itemList.isEmpty() ? "" :  "\n") +
                    "(" + item.first + ")" +
                    ( (int) item.first == answerCorrectIndex ? "*" : " ") +
                    item.second;
        }
        return "Question: " + question +
                " answerCorrectIndex=" + answerCorrectIndex +
                " answerList=\n" + itemList;
    }
}
