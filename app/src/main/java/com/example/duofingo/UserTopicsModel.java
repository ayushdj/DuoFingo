package com.example.duofingo;

import java.util.List;

public class UserTopicsModel {
    public Integer chapterID;
    public String topicName;
    public Integer total_chapters;
    public String userID;
    public List<String> completed;

    public void setChapterID(Integer chapterID) {
        this.chapterID = chapterID;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setNumChapters(Integer numChapters) {
        this.total_chapters = numChapters;
    }

    public void setUserName(String userName) {
        this.userID = userName;
    }

    public void setCompleted(List<String> completed) {this.completed = completed;}

}
