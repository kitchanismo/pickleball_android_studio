package com.example.pickleball_android;

public class MatchScore {

    public enum CURRENT_SERVING_TEAM {
        TEAM_BLUE,
        TEAM_RED
    }

    public enum SERVER {
        ONE,
        TWO
    }

    public MatchScore() {
        this.currentServingTeam = CURRENT_SERVING_TEAM.TEAM_BLUE;
        this.server = SERVER.TWO;
    }

    private CURRENT_SERVING_TEAM currentServingTeam;
    private SERVER server;

    public CURRENT_SERVING_TEAM getCurrentServingTeam() {
        return this.currentServingTeam;
    }

    public void setCurrentServingTeam(CURRENT_SERVING_TEAM currentServingTeam) {
        this.currentServingTeam = currentServingTeam;
    }

    public SERVER getServer() {
        return this.server;
    }

    public void setServer(SERVER server) {
        this.server = server;
    }

}
