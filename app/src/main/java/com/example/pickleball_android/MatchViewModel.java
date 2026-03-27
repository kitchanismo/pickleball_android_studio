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

//    private SkippableLiveData<CURRENT_SERVING_TEAM> currentServingTeam = new SkippableLiveData<>(CURRENT_SERVING_TEAM.TEAM_BLUE);
//    private MutableLiveData<SERVER> server = new MutableLiveData<>(SERVER.TWO);
//    private SkippableLiveData<Integer> blueScore = new SkippableLiveData<>(0);
//    private SkippableLiveData<Integer> redScore = new SkippableLiveData<>(0);

    private SkippableLiveData<Boolean> isGameOver = new SkippableLiveData<>(false);

    private MutableLiveData<MatchCall> call = new MutableLiveData<>(new MatchCall(CURRENT_SERVING_TEAM.TEAM_BLUE, SERVER.TWO, 0, 0));

    private List<MatchCall> initCalls = new ArrayList<MatchCall>(List.of(call.getValue()));

    private MutableLiveData<List<MatchCall>> calls = new MutableLiveData<>(initCalls);

    public LiveData<MatchCall> getCall() {
        return call;
    }

    public List<MatchCall> getInitCalls() {
        return new ArrayList<>(initCalls);
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

    public void setMatchCallToInitialState() {
        this.call.setValue(new MatchCall(CURRENT_SERVING_TEAM.TEAM_BLUE, SERVER.TWO, 0, 0));
    }

    public MatchCall getLastCall() {
        return this.calls.getValue().stream()
                .reduce((first, second) -> second)
                .orElse(null);
    }
}
