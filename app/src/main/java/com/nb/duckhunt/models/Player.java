package com.nb.duckhunt.models;

public class Player {
    private String id;
    private String nickname;
    private int ducksHunted;

    public Player() {
    }

    public Player(String nickname, int ducksHunted) {
        this.nickname = nickname;
        this.ducksHunted = ducksHunted;
    }

    public Player(String id, String nickname, int ducksHunted) {
        this.id = id;
        this.nickname = nickname;
        this.ducksHunted = ducksHunted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
