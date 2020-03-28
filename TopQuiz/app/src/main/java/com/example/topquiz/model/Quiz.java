package com.example.topquiz.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Quiz {

    //private List<Question> mQuestionList;
    private List<Integer> questionsOrder;
    private Map<Integer, Question> questions;
    private int currentIndex;

    public Quiz(List<Question> questionList) {
        // Shuffle the question list before storing it
        //this.mQuestionList = questionList;
        this.questions= new HashMap<>();
        this.questionsOrder= new ArrayList<>();
        for( Question question : questionList ) {
            int questionIndex = this.questions.size();
            this.questionsOrder.add(questionIndex);
            this.questions.put(questionIndex, question);
        }
        resetQuiz();
    }

    public void setNextQuizIndex() {
        // Loop over the questions and return a new one at each call
        currentIndex = ++currentIndex >= questions.size() ? 0 : currentIndex;
        if ( currentIndex == 0 ) resetQuiz();
    }

    public List<Integer> getQuestionsOrder() {
        return this.questionsOrder;
    }

    public void setQuestionsOrder(List<Integer> questionsOrder) {
        this.questionsOrder = questionsOrder;
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public  Question getCurrentQuestion() {
        return this.questions.get(this.questionsOrder.get(currentIndex));
    }

    private void resetQuiz() {
        currentIndex = 0;
        Collections.shuffle(questionsOrder);
        for( Integer keyIndex : questions.keySet())
            questions.get(keyIndex).shuffle();
    }

    public int getQuestionListSize() {
        return this.questionsOrder.size();
    }
    @Override
    public String toString() {
        String itemList = "";
        for( Integer keyIndex : questionsOrder) {
            Question question = questions.get(keyIndex);
            itemList += (itemList.isEmpty() ? "" :  "\n") +
                    "<" + keyIndex + ">" +
                    ( keyIndex == questionsOrder.get(currentIndex) ? "~" : " ") +
                    question.toString();
        }
        return "Quiz: " +
                " currentIndex=" + currentIndex +
                " questionsOrder=" + questionsOrder +
                " questions=\n" + itemList;
    }
}
