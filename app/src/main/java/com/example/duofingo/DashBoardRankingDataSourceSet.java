package com.example.duofingo;

public class DashBoardRankingDataSourceSet {
    private final String userName;
    private final String countryRank;
    private final String globalRank;

    public DashBoardRankingDataSourceSet(String userName, String countryRank, String globalRank) {
        this.userName = userName;
        this.countryRank = countryRank;
        this.globalRank = globalRank;
    }


    public String getUserName() {
        return userName;
    }

    public String getRank() {
        return countryRank;
    }

    public String getCountry() {
        return globalRank;
    }
}
