package com.example.pickleball_android;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MatchViewModel extends ViewModel {

    private SkippableLiveData<Boolean> isGameOver = new SkippableLiveData<>(false);

    private MatchTrack initTrack = new MatchTrack(MatchTrack.CURRENT_SERVING_TEAM.TEAM_BLUE, MatchTrack.SERVER.TWO, 0, 0, MatchTrack.BALL_POSITION.BLUE_TOP, new PlayerName(), false);

    private MutableLiveData<MatchTrack> call = new MutableLiveData<>(initTrack);

    private List<MatchTrack> initTracks = new ArrayList<MatchTrack>(List.of(call.getValue()));

    private MutableLiveData<List<MatchTrack>> tracks = new MutableLiveData<>(initTracks);

    public List<MatchTrack> getInitTracks() {
        return new ArrayList<>(initTracks);
    }

    public LiveData<List<MatchTrack>> getTracks() {
        return tracks;
    }

    public void setTracks(List<MatchTrack> tracks) {
        this.tracks.setValue(tracks);
    }

    public LiveData<Boolean> getGameIsOver() {
        return this.isGameOver;
    }

    public void setGameIsOver(boolean isGameOver) {
        this.isGameOver.setValue(isGameOver);
    }

    public void setMatchTrackToInitialState() {
        this.call.setValue(new MatchTrack(MatchTrack.CURRENT_SERVING_TEAM.TEAM_BLUE, MatchTrack.SERVER.TWO, 0, 0, MatchTrack.BALL_POSITION.BLUE_TOP, new PlayerName(), false));
    }

    public MatchTrack getLastTrackInstance() {
        MatchTrack lastTrack = getLastTrack();
        return new MatchTrack(lastTrack.getCurrentServingTeam(), lastTrack.getServer(), lastTrack.getBlueScore(), lastTrack.getRedScore(), lastTrack.getBallPosition(), lastTrack.getPlayerName(), lastTrack.getIsAtFaultOrSideOut());
    }

    public PlayerName getPlayerNameInstance(PlayerName playerName) {
        return new PlayerName(playerName.getBlueTop(), playerName.getBlueBottom(), playerName.getRedTop(), playerName.getRedBottom());
    }

    public void undoTrack() {
        List<MatchTrack> _tracks = new ArrayList<>(this.tracks.getValue());
        if (_tracks.size() < 2) return;
        _tracks.remove(_tracks.size() - 1);
        this.setTracks(_tracks);
    }

    public MatchTrack getLastTrack() {
        return this.tracks.getValue().stream()
                .reduce((first, second) -> second)
                .orElse(null);
    }
}
