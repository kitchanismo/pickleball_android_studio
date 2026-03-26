package com.example.pickleball_android;

public class MatchCall {
    private MatchViewModel.CURRENT_SERVING_TEAM currentServingTeam;
    private MatchViewModel.SERVER server;
    private Integer blueScore;
    private Integer redScore;

    private Boolean atSideOut = false;

    public MatchCall(MatchViewModel.CURRENT_SERVING_TEAM currentServingTeam, MatchViewModel.SERVER server, Integer blueScore, Integer redScore, Boolean atSideOut) {
        this.currentServingTeam = currentServingTeam;
        this.server = server;
        this.blueScore = blueScore;
        this.redScore = redScore;
        this.atSideOut = atSideOut;
    }

    public MatchViewModel.CURRENT_SERVING_TEAM getCurrentServingTeam() {
        return currentServingTeam;
    }

    public MatchViewModel.SERVER getServer() {
        return server;
    }

    public Integer getBlueScore() {
        return blueScore;
    }

    public Integer getRedScore() {
        return redScore;
    }

    public void setCurrentServingTeam(MatchViewModel.CURRENT_SERVING_TEAM currentServingTeam) {
        this.currentServingTeam = currentServingTeam;
    }

    public void setServer(MatchViewModel.SERVER server) {
        this.server = server;
    }

    public void setBlueScore(Integer blueScore) {
        this.blueScore = blueScore;
    }

    public void setRedScore(Integer redScore) {
        this.redScore = redScore;
    }

    public Boolean getAtSideOut() {
        return atSideOut;
    }

    public void setAtSideOut(Boolean atSideOut) {
        this.atSideOut = atSideOut;
    }

    public String textPrint(MatchCall call) {
        return "Current Serving Team: " + call.currentServingTeam + ", Server: " + call.server + ", Blue Score: " + call.blueScore + ", Red Score: " + call.redScore + "";
    }
}
