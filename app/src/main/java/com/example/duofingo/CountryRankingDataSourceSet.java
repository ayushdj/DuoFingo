package com.example.duofingo;

public class CountryRankingDataSourceSet {
    private final String userName;
    private final String countryRank;

    public CountryRankingDataSourceSet(String userName, String countryRank) {
        this.userName = userName;
        this.countryRank = countryRank;
    }

    public String getUserName() {
        return userName;
    }

    public String getRank() {
        return countryRank;
    }
}
