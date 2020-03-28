package com.example.topquiz.model;

public class User {

    private String mFirstName;
    private int mScore;

    public User(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public User(String mFirstName, int mScore) {
        this.mFirstName = mFirstName;
        this.mScore = mScore;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public int getmScore() {
        return mScore;
    }

    public void setmScore(int mScore) {
        this.mScore = mScore;
    }

    @Override
    public String toString() {
        return "User{" +
                "mFirstName='" + mFirstName + '\'' +
                ", mScore=" + mScore +
                '}';
    }
}
