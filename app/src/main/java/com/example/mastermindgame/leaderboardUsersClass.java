package com.example.mastermindgame;

public class leaderboardUsersClass {
    String UserProfilePicture,UserName;
    long UserScore;

    public leaderboardUsersClass(String userProfilePicture, String userName, long userScore) {
        UserProfilePicture = userProfilePicture;
        UserName = userName;
        UserScore = userScore;
    }

    public String getUserProfilePicture() {
        return UserProfilePicture;
    }

    public void setUserProfilePicture(String userProfilePicture) {
        UserProfilePicture = userProfilePicture;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public long getUserScore() {
        return UserScore;
    }

    public void setUserScore(long userScore) {
        UserScore = userScore;
    }
}
