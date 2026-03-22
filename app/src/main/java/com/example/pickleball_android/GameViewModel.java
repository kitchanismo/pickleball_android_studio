package com.example.pickleball_android;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GameViewModel extends ViewModel {
    public final MutableLiveData<String> playerName = new MutableLiveData<>();

    public void setPlayerName(String text) {
        this.playerName.setValue(text);
    }

    public String getPlayerName() {
        return this.playerName.getValue();
    }

}

