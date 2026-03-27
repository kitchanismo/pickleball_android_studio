package com.example.pickleball_android;

public class MatchCall {

    public enum CURRENT_SERVING_TEAM {
        TEAM_BLUE,
        TEAM_RED
    }

    public enum SERVER {
        ONE(1),
        TWO(2);

        private final int value;

        SERVER(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum BALL_POSITION {
        BLUE_TOP,
        BLUE_BOTTOM,
        RED_TOP,
        RED_BOTTOM
    }

    private CURRENT_SERVING_TEAM currentServingTeam;
    private SERVER server;
    private Integer blueScore;
    private Integer redScore;
    private BALL_POSITION ballPosition;

    public MatchCall(CURRENT_SERVING_TEAM currentServingTeam, SERVER server, Integer blueScore, Integer redScore, BALL_POSITION ballPosition) {
        this.currentServingTeam = currentServingTeam;
        this.server = server;
        this.blueScore = blueScore;
        this.redScore = redScore;
        this.ballPosition = ballPosition;
    }

    public BALL_POSITION getBallPosition() {
        return ballPosition;
    }

    public void setBallPosition(BALL_POSITION ballPosition) {
        this.ballPosition = ballPosition;
    }

    public CURRENT_SERVING_TEAM getCurrentServingTeam() {
        return currentServingTeam;
    }

    public SERVER getServer() {
        return server;
    }

    public Integer getBlueScore() {
        return blueScore;
    }

    public Integer getRedScore() {
        return redScore;
    }

    public void setCurrentServingTeam(CURRENT_SERVING_TEAM currentServingTeam) {
        this.currentServingTeam = currentServingTeam;
    }

    public void setServer(SERVER server) {
        this.server = server;
    }

    public void setBlueScore(Integer blueScore) {
        this.blueScore = blueScore;
    }

    public void setRedScore(Integer redScore) {
        this.redScore = redScore;
    }

    public String textPrint(MatchCall call) {
        return "Current Serving Team: " + call.currentServingTeam + ", Server: " + call.server + ", Blue Score: " + call.blueScore + ", Red Score: " + call.redScore + "";
    }
}
