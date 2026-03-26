package com.example.pickleball_android;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

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
    private MutableLiveData<SERVER> server = new MutableLiveData<>(SERVER.TWO);
    private SkippableLiveData<Integer> blueScore = new SkippableLiveData<>(0);
    private SkippableLiveData<Integer> redScore = new SkippableLiveData<>(0);

    private SkippableLiveData<Boolean> isGameOver = new SkippableLiveData<>(false);

    private MutableLiveData<List<MatchCall>> calls = new MutableLiveData<>(new ArrayList<MatchCall>(List.of(new MatchCall(CURRENT_SERVING_TEAM.TEAM_BLUE, SERVER.TWO, 0, 0, false))));

    private MutableLiveData<Boolean> atSideOut = new MutableLiveData<>(false);

    public MatchViewModel() {
//        List<MatchCall> calls = new ArrayList<>(this.calls.getValue());
//        this.calls.getValue().add(new MatchCall(CURRENT_SERVING_TEAM.TEAM_BLUE, SERVER.TWO, 0, 0));
//        this.setCalls(calls);
    }

    public LiveData<Boolean> getAtSideOut() {
        return atSideOut;
    }

    public void setAtSideOut(Boolean atSideOut) {
        this.atSideOut.setValue(atSideOut);
    }

    public LiveData<List<MatchCall>> getCalls() {
        return calls;
    }

    public void setCalls(List<MatchCall> calls) {
        this.calls.setValue(calls);
    }

    public LiveData<Boolean> getGameIsOver() {
        return this.isGameOver;
    }

    public void setGameIsOver(boolean isGameOver) {
        this.isGameOver.setValue(isGameOver);
    }

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

    public MatchCall getMatchCallInatance() {
        return new MatchCall(currentServingTeam.getValue(), server.getValue(), blueScore.getValue(), redScore.getValue(), atSideOut.getValue());
    }


}

