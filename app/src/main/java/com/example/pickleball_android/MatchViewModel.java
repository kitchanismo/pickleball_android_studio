package com.example.pickleball_android;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MatchViewModel extends ViewModel {

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

    private SkippableLiveData<CURRENT_SERVING_TEAM> currentServingTeam = new SkippableLiveData<>(CURRENT_SERVING_TEAM.TEAM_BLUE);
    private SkippableLiveData<SERVER> server = new SkippableLiveData<>(SERVER.TWO);
    private SkippableLiveData<Integer> blueScore = new SkippableLiveData<>(0);
    private SkippableLiveData<Integer> redScore = new SkippableLiveData<>(0);

    public LiveData<CURRENT_SERVING_TEAM> getCurrentServingTeam() {
        return this.currentServingTeam;
    }

    public void setCurrentServingTeam(CURRENT_SERVING_TEAM currentServingTeam) {
        this.currentServingTeam.setValue(currentServingTeam);
    }

    public LiveData<SERVER> getServer() {
        return this.server;
    }

    public void setServer(SERVER server) {
        this.server.setValue(server);
    }

    public LiveData<Integer> getBlueScore() {
        return this.blueScore;
    }

    public void setBlueScore(int blueScore) {
        this.blueScore.setValue(blueScore);
    }

    public LiveData<Integer> getRedScore() {
        return this.redScore;
    }

    public void setRedScore(int redScore) {
        this.redScore.setValue(redScore);
    }


}

