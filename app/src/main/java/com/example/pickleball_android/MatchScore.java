package com.example.pickleball_android;

public class MatchScore {

    public enum CURRENT_SERVING_TEAM {
        TEAM_BLUE,
        TEAM_RED
    }

    public MatchScore() {
        this.currentServingTeam = CURRENT_SERVING_TEAM.TEAM_BLUE;
    }

    private CURRENT_SERVING_TEAM currentServingTeam;

    public CURRENT_SERVING_TEAM getCurrentServingTeam() {
        return this.currentServingTeam;
    }

    public void setCurrentServingTeam(CURRENT_SERVING_TEAM currentServingTeam) {
        this.currentServingTeam = currentServingTeam;
    }

}
