package com.example.pickleball_android;

public class CourtSide {
    public enum BALL_POSITION {
        BLUE_TOP,
        BLUE_BOTTOM,
        RED_TOP,
        RED_BOTTOM
    }

    String playerName;

    BALL_POSITION position;

    public CourtSide(String playerName, BALL_POSITION position) {
        this.playerName = playerName;
        this.position = position;

    }
}
