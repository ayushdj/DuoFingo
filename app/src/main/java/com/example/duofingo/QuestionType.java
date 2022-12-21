package com.example.duofingo;

public enum QuestionType {
    WEEKLY("WEEKLY"),
    MONTHLY("MONTHLY"),
    CHAPTER("CHAPTER");

    private final String type;

    QuestionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
