package com.example.pickleball_android;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.pickleball_android.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * MainActivity handles the UI and user interactions for the Pickleball Scoreboard.
 */
public class MainActivity extends AppCompatActivity {

    private MatchViewModel vmMatch;

    private ActivityMainBinding binding;
    private TextView txtScore;
    private Button btnFault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onFullScreen();
        initViews();
        setupViewModel();
    }

    /**
     * Finds and initializes view references from the binding.
     */
    private void initViews() {
        txtScore = binding.kitchenLayout.findViewById(R.id.txtScore);
        btnFault = binding.kitchenLayout.findViewById(R.id.btn_fault);

        // Initial serve state
        // binding.imgServingBlueTop.setVisibility(View.VISIBLE);
    }

    /**
     * Initializes the ViewModel and sets up observers for data changes.
     */
    private void setupViewModel() {
        vmMatch = new ViewModelProvider(this).get(MatchViewModel.class);

        vmMatch.getTracks().observe(this, tracks -> {
            if (vmMatch.getGameIsOver().getValue()) {
                return;
            }
            MatchTrack lastTrack = vmMatch.getLastTrack();

            int count = tracks.size();

            if (lastTrack == null) return;

            Boolean isBlueTeam = lastTrack.getCurrentServingTeam() == MatchTrack.CURRENT_SERVING_TEAM.TEAM_BLUE;

            checkForWinner(isBlueTeam, lastTrack);

            updateButtonLabels(lastTrack.getServer());

            if (count < 2) {
                resetMatchToInitialState();
                return;
            }

            updatePlayerName(lastTrack.getPlayerName());
            updateScoreText(lastTrack);
            moveBallPosition(lastTrack.getBallPosition());
        });

    }

    public void updatePlayerName(PlayerName playerName) {
        binding.txtPlayerBlueTop.setText(playerName.getBlueTop());
        binding.txtPlayerBlueBottom.setText(playerName.getBlueBottom());
        binding.txtPlayerRedTop.setText(playerName.getRedTop());
        binding.txtPlayerRedBottom.setText(playerName.getRedBottom());
    }

    public void moveBallPosition(MatchTrack.BALL_POSITION ballPosition) {
        binding.imgServingBlueTop.setVisibility(ballPosition == MatchTrack.BALL_POSITION.BLUE_TOP ? View.VISIBLE : View.INVISIBLE);
        binding.imgServingBlueBottom.setVisibility(ballPosition == MatchTrack.BALL_POSITION.BLUE_BOTTOM ? View.VISIBLE : View.INVISIBLE);
        binding.imgServingRedTop.setVisibility(ballPosition == MatchTrack.BALL_POSITION.RED_TOP ? View.VISIBLE : View.INVISIBLE);
        binding.imgServingRedBottom.setVisibility(ballPosition == MatchTrack.BALL_POSITION.RED_BOTTOM ? View.VISIBLE : View.INVISIBLE);
    }

    private void updateScoreText(MatchTrack track) {
        int blue = track.getBlueScore();
        int red = track.getRedScore();
        int server = track.getServer().getValue();
        boolean isBlueServing = track.getCurrentServingTeam() == MatchTrack.CURRENT_SERVING_TEAM.TEAM_BLUE;

        // In Pickleball, the serving team's score is mentioned first
        String formattedScore = isBlueServing
                ? String.format(Locale.US, "%d-%d-%d", blue, red, server)
                : String.format(Locale.US, "%d-%d-%d", red, blue, server);

        txtScore.setText(formattedScore);
    }

    private void updateButtonLabels(MatchTrack.SERVER server) {
        btnFault.setText(server == MatchTrack.SERVER.ONE ? "FAULT" : "SIDEOUT");
    }

    private void checkForWinner(boolean isBlueTeam, MatchTrack track) {

        int oppositeScore = isBlueTeam ? track.getRedScore() : track.getBlueScore();
        int currentScore = isBlueTeam ? track.getBlueScore() : track.getRedScore();
        if (currentScore >= 11 && (currentScore - oppositeScore) >= 2) {
            vmMatch.setGameIsOver(true);
            showWinnerDialog((isBlueTeam ? "Blue" : "Red") + " Team Wins! Final Score: " + currentScore + " - " + oppositeScore + " - " + track.getServer().getValue());
        }
    }

    private void resetMatchToInitialState() {
        vmMatch.setMatchTrackToInitialState();

        if (vmMatch.getGameIsOver().getValue()) {
            vmMatch.setTracks(vmMatch.getInitTracks());
            vmMatch.setGameIsOver(false);
        }

        txtScore.setText("0-0-2");

        binding.imgServingBlueTop.setVisibility(View.VISIBLE);
        binding.imgServingBlueBottom.setVisibility(View.INVISIBLE);
        binding.imgServingRedTop.setVisibility(View.INVISIBLE);
        binding.imgServingRedBottom.setVisibility(View.INVISIBLE);

        binding.txtPlayerBlueTop.setText("Player 1");
        binding.txtPlayerBlueBottom.setText("Player 2");
        binding.txtPlayerRedTop.setText("Player 3");
        binding.txtPlayerRedBottom.setText("Player 4");

        System.out.println("reset");
    }

    public void onBtnScoreListener(View v) {
        List<MatchTrack> tracks = new ArrayList<>(vmMatch.getTracks().getValue());
        MatchTrack track = vmMatch.getLastTrackInstance();

        boolean isBlueServing = track.getCurrentServingTeam() == MatchTrack.CURRENT_SERVING_TEAM.TEAM_BLUE;

        PlayerName playerName = vmMatch.getPlayerNameInstance(track.getPlayerName());

        if (isBlueServing) {
            track.setBlueScore(track.getBlueScore() + 1);
            track.setBallPosition(track.getBallPosition() == MatchTrack.BALL_POSITION.BLUE_TOP
                    ? MatchTrack.BALL_POSITION.BLUE_BOTTOM : MatchTrack.BALL_POSITION.BLUE_TOP);

            String temp = playerName.getBlueTop();
            playerName.setBlueTop(playerName.getBlueBottom());
            playerName.setBlueBottom(temp);
        } else {
            track.setRedScore(track.getRedScore() + 1);
            String temp = playerName.getRedTop();
            playerName.setRedTop(playerName.getRedBottom());
            playerName.setRedBottom(temp);

            track.setBallPosition(track.getBallPosition() == MatchTrack.BALL_POSITION.RED_TOP ? MatchTrack.BALL_POSITION.RED_BOTTOM : MatchTrack.BALL_POSITION.RED_TOP);
        }
        track.setPlayerName(playerName);
        track.setIsAtFaultOrSideOut(false);

        tracks.add(track);
        vmMatch.setTracks(tracks);
        for (MatchTrack c : tracks) {
            System.out.println(c.textPrint(c));
        }
    }

    /**
     * XML OnClick listener for the Fault button.
     */
    public void onBtnFaultListener(View v) {
        List<MatchTrack> tracks = vmMatch.getTracks().getValue();
        MatchTrack track = vmMatch.getLastTrackInstance();

        MatchTrack.CURRENT_SERVING_TEAM current = track.getCurrentServingTeam();
        Boolean isBlueServing = current == MatchTrack.CURRENT_SERVING_TEAM.TEAM_BLUE;
        if (track.getServer() == MatchTrack.SERVER.TWO) {
            track.setServer(MatchTrack.SERVER.ONE);
            track.setCurrentServingTeam(isBlueServing
                    ? MatchTrack.CURRENT_SERVING_TEAM.TEAM_RED
                    : MatchTrack.CURRENT_SERVING_TEAM.TEAM_BLUE);
            track.setBallPosition(isBlueServing ? MatchTrack.BALL_POSITION.RED_BOTTOM : MatchTrack.BALL_POSITION.BLUE_TOP);
        } else {
            track.setServer(MatchTrack.SERVER.TWO);
            if (isBlueServing) {
                track.setBallPosition(track.getBallPosition() == MatchTrack.BALL_POSITION.BLUE_TOP ? MatchTrack.BALL_POSITION.BLUE_BOTTOM : MatchTrack.BALL_POSITION.BLUE_TOP);

            } else {
                track.setBallPosition(track.getBallPosition() == MatchTrack.BALL_POSITION.RED_TOP ? MatchTrack.BALL_POSITION.RED_BOTTOM : MatchTrack.BALL_POSITION.RED_TOP);
            }
        }
        track.setIsAtFaultOrSideOut(true);
        tracks.add(track);
        vmMatch.setTracks(tracks);

    }

    /**
     * Enables EdgeToEdge and hides system bars.
     */
    private void onFullScreen() {
        EdgeToEdge.enable(this);
        WindowInsetsControllerCompat controller = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (controller != null) {
            controller.hide(WindowInsetsCompat.Type.systemBars());
            controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, 0, bars.right, 0);
            return insets;
        });
    }

    public void onBtnListenerUndo(View view) {
        vmMatch.undoTrack();

    }

    private void showWinnerDialog(String message) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage(message)
                .setPositiveButton("NEW GAME", (d, w) -> {
                    resetMatchToInitialState();
                    d.dismiss();
                })
                .setCancelable(false)
                .create();

        dialog.show();
        applyFullScreenFlags(dialog);
    }

    private void applyFullScreenFlags(@NonNull AlertDialog dialog) {
        if (dialog.getWindow() != null) {
            WindowInsetsControllerCompat controller = ViewCompat.getWindowInsetsController(dialog.getWindow().getDecorView());
            if (controller != null) {
                controller.hide(WindowInsetsCompat.Type.systemBars());
                controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        }
    }
}
