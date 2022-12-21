package com.example.duofingo;

public class UserModel {
    public String userName;
    public String password;
    public String fullName;
    public String email;
    public String state;
    public String country;
    public String city;
    public String profilePictureID;
    public int userScore;

    public String getFullName() {
        return fullName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProfilePictureID(String profilePictureID) {
        this.profilePictureID = profilePictureID;
    }

    public void setUserScore(int userScore) {
        this.userScore = userScore;
    }

    public UserModel() {
    }

    public UserModel(String userName, String password, String fullName, String email, String state,
                     String country, String city, int userScore, String profilePictureID) {
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.state = state;
        this.country = country;
        this.city = city;
        this.userScore = userScore;
        this.profilePictureID = profilePictureID;
    }
}
