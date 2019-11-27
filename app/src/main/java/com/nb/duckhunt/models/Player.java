package com.nb.duckhunt.models;

public class Player {
    private String nickname;
    private int ducksHunted;

    public Player() {
    }

    public Player(String nickname, int ducksHunted) {
        this.nickname = nickname;
        this.ducksHunted = ducksHunted;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getDucksHunted() {
        return ducksHunted;
    }

    public void setDucksHunted(int ducksHunted) {
        this.ducksHunted = ducksHunted;
    }
}
