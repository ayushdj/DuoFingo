package com.example.duofingo;

public enum UserType {
    NOVICE(6,3,1, 10),
    INTERMEDIATE(4,3,3, 60),
    EXPERT(2,3,5, 1000);

    private final int numEasyQuestions;
    private final int numMediumQuestions;
    private final int numHardQuestions;
    private final int maxUserScore;

    public int getNumEasyQuestions() {
        return numEasyQuestions;
    }

    public int getNumMediumQuestions() {
        return numMediumQuestions;
    }

    public int getNumHardQuestions() {
        return numHardQuestions;
    }

    UserType(int numEasyQuestions, int numMediumQuestions, int numHardQuestions, int maxUserScore) {
        this.numEasyQuestions = numEasyQuestions;
        this.numMediumQuestions = numMediumQuestions;
        this.numHardQuestions = numHardQuestions;
        this.maxUserScore = maxUserScore;
    }

    public int getMaxUserScore() {
        return maxUserScore;
    }
}
