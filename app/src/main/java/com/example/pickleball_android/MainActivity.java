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
            MatchCall currentCall = calls.stream()
                    .reduce((first, second) -> second)
                    .orElse(null);
            int count = calls.size();

            if (currentCall == null) return;

            Boolean isBlueTeam = currentCall.getCurrentServingTeam() == MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE;

            checkForWinner(isBlueTeam, currentCall);

            if (count < 2) {
                resetMatchToInitialState();
                System.out.println("hit");
                return;
            }
            updateScoreText(currentCall);
            toggleServingIndicator(isBlueTeam);

        });

        vmMatch.getServer().observe(this, server -> updateButtonLabels());

    }

    private void updateScoreText(MatchCall call) {
        int blue = call.getBlueScore();
        int red = call.getRedScore();
        int server = call.getServer().getValue();
        boolean isBlueServing = call.getCurrentServingTeam() == MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE;

        // In Pickleball, the serving team's score is mentioned first
        String formattedScore = isBlueServing
                ? String.format(Locale.US, "%d-%d-%d", blue, red, server)
                : String.format(Locale.US, "%d-%d-%d", red, blue, server);

        txtScore.setText(formattedScore);
    }

    private void updateButtonLabels() {
        MatchViewModel.SERVER server = vmMatch.getServer().getValue();
        btnFault.setText(server == MatchViewModel.SERVER.ONE ? "FAULT" : "SIDEOUT");
    }

    private void checkForWinner(boolean isBlueTeam, MatchCall call) {

        int oppositeScore = isBlueTeam ? call.getRedScore() : call.getBlueScore();
        int currentScore = isBlueTeam ? call.getBlueScore() : call.getRedScore();
        if (currentScore >= 11 && (currentScore - oppositeScore) >= 2) {
            vmMatch.setGameIsOver(true);
            showWinnerDialog((isBlueTeam ? "Blue" : "Red") + " Team Wins! Final Score: " + currentScore + " - " + oppositeScore + " - " + vmMatch.getServer().getValue().getValue());
        }
    }

    private void toggleServingIndicator(boolean isBlueTeam) {
        if (isBlueTeam) {
            rotateVisibility(binding.imgServingBlueTop, binding.imgServingBlueBottom);
            binding.imgServingRedTop.setVisibility(View.INVISIBLE);
            binding.imgServingRedBottom.setVisibility(View.INVISIBLE);
        } else {
            rotateVisibility(binding.imgServingRedBottom, binding.imgServingRedTop);
            binding.imgServingBlueTop.setVisibility(View.INVISIBLE);
            binding.imgServingBlueBottom.setVisibility(View.INVISIBLE);
        }
    }

    private void rotateVisibility(View v1, View v2) {
        boolean isV1Visible = v1.getVisibility() == View.VISIBLE;
        v1.setVisibility(isV1Visible ? View.INVISIBLE : View.VISIBLE);
        v2.setVisibility(isV1Visible ? View.VISIBLE : View.INVISIBLE);
    }

    private void resetMatchToInitialState() {
        vmMatch.setCurrentServingTeam(MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE);
        vmMatch.setServer(MatchViewModel.SERVER.TWO);
        vmMatch.setBlueScore(0);
        vmMatch.setRedScore(0);

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
        MatchCall call = vmMatch.getMatchCallInatance();
        boolean isBlueServing = vmMatch.getCurrentServingTeam().getValue() == MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE;
        if (isBlueServing) {
            vmMatch.setBlueScore(vmMatch.getBlueScore().getValue() + 1);
            swapPlayerLabels(binding.txtPlayerBlueTop, binding.txtPlayerBlueBottom);
        } else {
            vmMatch.setRedScore(vmMatch.getRedScore().getValue() + 1);
            swapPlayerLabels(binding.txtPlayerRedTop, binding.txtPlayerRedBottom);
        }
        call.setBlueScore(vmMatch.getBlueScore().getValue());
        call.setRedScore(vmMatch.getRedScore().getValue());
        call.setServer(vmMatch.getServer().getValue());
        call.setCurrentServingTeam(vmMatch.getCurrentServingTeam().getValue());
        calls.add(call);

        System.out.println("size:" + calls.size());
        vmMatch.setCalls(calls);
//        for (MatchCall c : calls) {
//            System.out.println(c.textPrint(c));
//        }
    }

    private void swapPlayerLabels(TextView t1, TextView t2) {
        CharSequence temp = t1.getText();
        t1.setText(t2.getText());
        t2.setText(temp);
    }

    /**
     * XML OnClick listener for the Fault button.
     */
    public void onBtnFaultListener(View v) {
        List<MatchCall> calls = vmMatch.getCalls().getValue();
        MatchCall call = vmMatch.getMatchCallInatance();
        if (vmMatch.getServer().getValue() == MatchViewModel.SERVER.TWO) {
            vmMatch.setServer(MatchViewModel.SERVER.ONE);
            MatchViewModel.CURRENT_SERVING_TEAM current = vmMatch.getCurrentServingTeam().getValue();
            vmMatch.setCurrentServingTeam(current == MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE
                    ? MatchViewModel.CURRENT_SERVING_TEAM.TEAM_RED
                    : MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE);

        } else {
            // toggleServingIndicator(vmMatch.getCurrentServingTeam().getValue() == MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE);
            vmMatch.setServer(MatchViewModel.SERVER.TWO);
        }
        call.setBlueScore(vmMatch.getBlueScore().getValue());
        call.setRedScore(vmMatch.getRedScore().getValue());
        call.setServer(vmMatch.getServer().getValue());
        call.setCurrentServingTeam(vmMatch.getCurrentServingTeam().getValue());
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
