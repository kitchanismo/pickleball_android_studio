package com.example.pickleball_android;

public class PlayerName {
    private String blueTop = "Player 1";
    private String blueBottom = "Player 2";
    private String redTop = "Player 3";
    private String redBottom = "Player 4";

    public PlayerName(String blueTop, String blueBottom, String redTop, String redBottom) {
        this.blueTop = blueTop;
        this.blueBottom = blueBottom;
        this.redTop = redTop;
        this.redBottom = redBottom;
    }

    public PlayerName() {
        
    }

    public String getBlueTop() {
        return blueTop;
    }

    public String getBlueBottom() {
        return blueBottom;
    }

    public String getRedTop() {
        return redTop;
    }

    public String getRedBottom() {
        return redBottom;
    }

    public void setBlueTop(String blueTop) {
        this.blueTop = blueTop;
    }

    public void setBlueBottom(String blueBottom) {
        this.blueBottom = blueBottom;
    }

    public void setRedTop(String redTop) {
        this.redTop = redTop;
    }

    public void setRedBottom(String redBottom) {
        this.redBottom = redBottom;
    }
}
