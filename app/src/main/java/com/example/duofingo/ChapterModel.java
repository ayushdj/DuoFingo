package com.example.duofingo;

public class ChapterModel {
    public String[] body;
    public String chapterID;
    public String conclusion;
    public String heading;
    public String imagePath;
    public String topicID;

    public ChapterModel() {
    }

    public void setBody(String[] body) {
        this.body = body;
    }

    public void setChapterID(String chapterID) {
        this.chapterID = chapterID;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setTopicID(String topicID) {
        this.topicID = topicID;
    }
}
