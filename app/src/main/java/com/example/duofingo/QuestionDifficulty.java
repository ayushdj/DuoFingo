package com.example.duofingo;

public enum QuestionDifficulty {
    EASY("EASY", 1),
    MEDIUM("MEDIUM", 2),
    HARD("HARD", 3);


    private final String difficulty;

    public int getQuestionScore() {
        return questionScore;
    }

    private final int questionScore;

    QuestionDifficulty(String difficulty, int questionScore) {
        this.difficulty = difficulty;
        this.questionScore = questionScore;
    }
}
