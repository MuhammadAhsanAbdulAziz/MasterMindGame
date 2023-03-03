package com.example.mastermindgame;

public class UsersClass {
    String userName,userPicture,userEmail,userPassword,userId;
    long easyScore,MediumScore,hardScore;

    public UsersClass(String userName, String userPicture, String userEmail,String userId) {
        this.userName = userName;
        this.userPicture = userPicture;
        this.userEmail = userEmail;
        this.userId = userId;
//        this.easyScore = easyScore;
//        MediumScore = mediumScore;
//        this.hardScore = hardScore;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public long getEasyScore() {
        return easyScore;
    }

    public void setEasyScore(long easyScore) {
        this.easyScore = easyScore;
    }

    public long getMediumScore() {
        return MediumScore;
    }

    public void setMediumScore(long mediumScore) {
        MediumScore = mediumScore;
    }

    public long getHardScore() {
        return hardScore;
    }

    public void setHardScore(long hardScore) {
        this.hardScore = hardScore;
    }
}
