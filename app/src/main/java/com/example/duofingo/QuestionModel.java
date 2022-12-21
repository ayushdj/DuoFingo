package com.example.duofingo;

public class QuestionModel {

    public String chapterID;
    public String correctAnswer;
    public String question;
    public String difficulty;
    public String tag;
    public String topicID;
    public String[] answers;

    public void setChapterID(String chapterID) {
        this.chapterID = chapterID;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setTopicID(String topicID) {
        this.topicID = topicID;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public QuestionModel() {
    }
}
