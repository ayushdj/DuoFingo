package com.example.duofingo;

public class ContinueReadingDataSource {
    private final String topicName;
    private final String chapterName;
    private final String currentChapter;
    private final String totalChapter;
//    private final Integer chapterId;
    private final String userName;

    public ContinueReadingDataSource(String chapterName, String topicName,
                                     String currentChapter, String totalChapter, String userName) {
        this.chapterName = chapterName;
        this.topicName = topicName;
        this.currentChapter = currentChapter;
        this.totalChapter = totalChapter;
        this.userName = userName;
    }


    public String getChapterName() {
        return chapterName;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getCurrentChapter() {
        return currentChapter;
    }

    public String getTotalChapter() {
        return totalChapter;
    }

    public String getUserName() {
        return userName;
    }

}
