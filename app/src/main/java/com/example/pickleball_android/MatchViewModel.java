package com.example.pickleball_android;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MatchViewModel extends ViewModel {
    public final MutableLiveData<String> playerName = new MutableLiveData<>();
    public final MutableLiveData<MatchScore> matchScore = new MutableLiveData<>(new MatchScore());

    public void setMatchScore(MatchScore matchScore) {
        this.matchScore.setValue(matchScore);
    }

    public MatchScore getMatchScore() {
        return this.matchScore.getValue();
    }

    public void setPlayerName(String text) {
        this.playerName.setValue(text);
    }

    public String getPlayerName() {
        return this.playerName.getValue();
    }

}

