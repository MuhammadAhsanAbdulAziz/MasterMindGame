package com.example.mastermindgame;

public class playerInfoClass {
    String users_name,users_uri;
    long easyScore,mediumScore,hardScore;

    public playerInfoClass(String users_name, String users_uri, long easyScore, long mediumScore, long hardScore) {
        this.users_name = users_name;
        this.users_uri = users_uri;
        this.easyScore = easyScore;
        this.mediumScore = mediumScore;
        this.hardScore = hardScore;
    }

    public String getUsers_name() {
        return users_name;
    }

    public void setUsers_name(String users_name) {
        this.users_name = users_name;
    }

    public String getUsers_uri() {
        return users_uri;
    }

    public void setUsers_uri(String users_uri) {
        this.users_uri = users_uri;
    }

    public long getEasyScore() {
        return easyScore;
    }

    public void setEasyScore(long easyScore) {
        this.easyScore = easyScore;
    }

    public long getMediumScore() {
        return mediumScore;
    }

    public void setMediumScore(long mediumScore) {
        this.mediumScore = mediumScore;
    }

    public long getHardScore() {
        return hardScore;
    }

    public void setHardScore(long hardScore) {
        this.hardScore = hardScore;
    }
}

