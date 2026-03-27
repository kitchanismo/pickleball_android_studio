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

    public enum PLAYER_NAME {
        PLAYER_1("Player 1"),
        PLAYER_2("Player 2"),
        PLAYER_3("Player 3"),
        PLAYER_4("Player 4");
        private final String value;

        PLAYER_NAME(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private CURRENT_SERVING_TEAM currentServingTeam;
    private SERVER server;
    private Integer blueScore;
    private Integer redScore;
    private BALL_POSITION ballPosition;

    private PlayerName playerName;

    public MatchCall(CURRENT_SERVING_TEAM currentServingTeam, SERVER server, Integer blueScore, Integer redScore, BALL_POSITION ballPosition, PlayerName playerName) {
        this.currentServingTeam = currentServingTeam;
        this.server = server;
        this.blueScore = blueScore;
        this.redScore = redScore;
        this.ballPosition = ballPosition;
        this.playerName = playerName;
    }

    public PlayerName getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(PlayerName playerName) {
        this.playerName = playerName;
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
