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

        vmMatch.getCalls().observe(this, calls -> {
            if (vmMatch.getGameIsOver().getValue()) {
                return;
            }
            MatchCall lastCall = vmMatch.getLastCall();

            int count = calls.size();

            if (lastCall == null) return;

            Boolean isBlueTeam = lastCall.getCurrentServingTeam() == MatchCall.CURRENT_SERVING_TEAM.TEAM_BLUE;

            checkForWinner(isBlueTeam, lastCall);

            updateButtonLabels(lastCall.getServer());

            if (count < 2) {
                resetMatchToInitialState();
                System.out.println("hit");
                return;
            }

            // if (!lastCall.getIsAtFaultOrSideOut()) {
            updatePlayerName(lastCall.getPlayerName());
            //}

            System.out.println("observer:" + count);
            updateScoreText(lastCall);
            moveBallPosition(lastCall.getBallPosition());
        });

    }

    public void updatePlayerName(PlayerName playerName) {
        binding.txtPlayerBlueTop.setText(playerName.getBlueTop());
        binding.txtPlayerBlueBottom.setText(playerName.getBlueBottom());
        binding.txtPlayerRedTop.setText(playerName.getRedTop());
        binding.txtPlayerRedBottom.setText(playerName.getRedBottom());
    }

    public void moveBallPosition(MatchCall.BALL_POSITION ballPosition) {
        binding.imgServingBlueTop.setVisibility(ballPosition == MatchCall.BALL_POSITION.BLUE_TOP ? View.VISIBLE : View.INVISIBLE);
        binding.imgServingBlueBottom.setVisibility(ballPosition == MatchCall.BALL_POSITION.BLUE_BOTTOM ? View.VISIBLE : View.INVISIBLE);
        binding.imgServingRedTop.setVisibility(ballPosition == MatchCall.BALL_POSITION.RED_TOP ? View.VISIBLE : View.INVISIBLE);
        binding.imgServingRedBottom.setVisibility(ballPosition == MatchCall.BALL_POSITION.RED_BOTTOM ? View.VISIBLE : View.INVISIBLE);
    }

    private void updateScoreText(MatchCall call) {
        int blue = call.getBlueScore();
        int red = call.getRedScore();
        int server = call.getServer().getValue();
        boolean isBlueServing = call.getCurrentServingTeam() == MatchCall.CURRENT_SERVING_TEAM.TEAM_BLUE;

        // In Pickleball, the serving team's score is mentioned first
        String formattedScore = isBlueServing
                ? String.format(Locale.US, "%d-%d-%d", blue, red, server)
                : String.format(Locale.US, "%d-%d-%d", red, blue, server);

        txtScore.setText(formattedScore);
    }

    private void updateButtonLabels(MatchCall.SERVER server) {
        btnFault.setText(server == MatchCall.SERVER.ONE ? "FAULT" : "SIDEOUT");
    }

    private void checkForWinner(boolean isBlueTeam, MatchCall call) {

        int oppositeScore = isBlueTeam ? call.getRedScore() : call.getBlueScore();
        int currentScore = isBlueTeam ? call.getBlueScore() : call.getRedScore();
        if (currentScore >= 11 && (currentScore - oppositeScore) >= 2) {
            vmMatch.setGameIsOver(true);
            showWinnerDialog((isBlueTeam ? "Blue" : "Red") + " Team Wins! Final Score: " + currentScore + " - " + oppositeScore + " - " + call.getServer().getValue());
        }
    }

    private void resetMatchToInitialState() {
        vmMatch.setMatchCallToInitialState();

        if (vmMatch.getGameIsOver().getValue()) {
            vmMatch.setCalls(vmMatch.getInitCalls());
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
        List<MatchCall> calls = new ArrayList<>(vmMatch.getCalls().getValue());
        MatchCall call = vmMatch.getLastCallInstance();

        boolean isBlueServing = call.getCurrentServingTeam() == MatchCall.CURRENT_SERVING_TEAM.TEAM_BLUE;

        PlayerName playerName = vmMatch.getPlayerNameInstance(call.getPlayerName());

        if (isBlueServing) {
            call.setBlueScore(call.getBlueScore() + 1);
            call.setBallPosition(call.getBallPosition() == MatchCall.BALL_POSITION.BLUE_TOP
                    ? MatchCall.BALL_POSITION.BLUE_BOTTOM : MatchCall.BALL_POSITION.BLUE_TOP);

            String temp = playerName.getBlueTop();
            playerName.setBlueTop(playerName.getBlueBottom());
            playerName.setBlueBottom(temp);
        } else {
            call.setRedScore(call.getRedScore() + 1);
            String temp = playerName.getRedTop();
            playerName.setRedTop(playerName.getRedBottom());
            playerName.setRedBottom(temp);

            call.setBallPosition(call.getBallPosition() == MatchCall.BALL_POSITION.RED_TOP ? MatchCall.BALL_POSITION.RED_BOTTOM : MatchCall.BALL_POSITION.RED_TOP);
        }
        call.setPlayerName(playerName);
        call.setIsAtFaultOrSideOut(false);

        calls.add(call);
        vmMatch.setCalls(calls);
        for (MatchCall c : calls) {
            System.out.println(c.textPrint(c));
        }
    }

    /**
     * XML OnClick listener for the Fault button.
     */
    public void onBtnFaultListener(View v) {
        List<MatchCall> calls = vmMatch.getCalls().getValue();
        MatchCall call = vmMatch.getLastCallInstance();

        MatchCall.CURRENT_SERVING_TEAM current = call.getCurrentServingTeam();
        Boolean isBlueServing = current == MatchCall.CURRENT_SERVING_TEAM.TEAM_BLUE;
        if (call.getServer() == MatchCall.SERVER.TWO) {
            call.setServer(MatchCall.SERVER.ONE);
            call.setCurrentServingTeam(isBlueServing
                    ? MatchCall.CURRENT_SERVING_TEAM.TEAM_RED
                    : MatchCall.CURRENT_SERVING_TEAM.TEAM_BLUE);
            call.setBallPosition(isBlueServing ? MatchCall.BALL_POSITION.RED_BOTTOM : MatchCall.BALL_POSITION.BLUE_TOP);
        } else {
            call.setServer(MatchCall.SERVER.TWO);
            if (isBlueServing) {
                call.setBallPosition(call.getBallPosition() == MatchCall.BALL_POSITION.BLUE_TOP ? MatchCall.BALL_POSITION.BLUE_BOTTOM : MatchCall.BALL_POSITION.BLUE_TOP);

            } else {
                call.setBallPosition(call.getBallPosition() == MatchCall.BALL_POSITION.RED_TOP ? MatchCall.BALL_POSITION.RED_BOTTOM : MatchCall.BALL_POSITION.RED_TOP);
            }
        }
        call.setIsAtFaultOrSideOut(true);
        calls.add(call);
        vmMatch.setCalls(calls);

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
        vmMatch.undoCall();

    }

    private void showWinnerDialog(String message) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage(message)
                .setPositiveButton("OK", (d, w) -> {
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
