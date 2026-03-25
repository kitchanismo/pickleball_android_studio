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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.pickleball_android.databinding.ActivityMainBinding;

import java.util.Locale;

/**
 * MainActivity handles the UI and user interactions for the Pickleball Scoreboard.
 */
public class MainActivity extends AppCompatActivity {

    private MatchViewModel viewModel;
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
        binding.imgServingBlueTop.setVisibility(View.VISIBLE);
    }

    /**
     * Initializes the ViewModel and sets up observers for data changes.
     */
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(MatchViewModel.class);

        // UI refresh triggers for serving team and server number changes
        viewModel.getCurrentServingTeam().observe(this, team -> refreshUICurrentTeam(team));
        viewModel.getServer().observe(this, server -> refreshUI());

        // Score logic triggers
        viewModel.getBlueScore().observe(this, score -> handleScoreUpdate(true, score));
        viewModel.getRedScore().observe(this, score -> handleScoreUpdate(false, score));

        // Game over observer
        viewModel.getGameIsOver().observe(this, isGameOver -> {
            if (Boolean.TRUE.equals(isGameOver)) {
                resetMatchToInitialState();
            }
        });
    }

    /**
     * Refreshes the score display and button labels if the game is not over.
     */
    private void refreshUI() {
        if (isGameOver()) return;
        updateScoreText();
        updateButtonLabels();
    }

    private void refreshUICurrentTeam(MatchViewModel.CURRENT_SERVING_TEAM team) {
        if (isGameOver()) return;
        updateScoreText();
        updateButtonLabels();
        if (team == MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE) {
            binding.imgServingBlueTop.setVisibility(View.VISIBLE);
            binding.imgServingBlueBottom.setVisibility(View.INVISIBLE);
            binding.imgServingRedTop.setVisibility(View.INVISIBLE);
            binding.imgServingRedBottom.setVisibility(View.INVISIBLE);
        } else {
            binding.imgServingBlueTop.setVisibility(View.INVISIBLE);
            binding.imgServingBlueBottom.setVisibility(View.INVISIBLE);
            binding.imgServingRedTop.setVisibility(View.INVISIBLE);
            binding.imgServingRedBottom.setVisibility(View.VISIBLE);
        }
        
    }

    /**
     * Updates the score text view based on who is currently serving.
     */
    private void updateScoreText() {
        int blue = getVal(viewModel.getBlueScore());
        int red = getVal(viewModel.getRedScore());
        int server = viewModel.getServer().getValue().getValue();
        boolean isBlueServing = viewModel.getCurrentServingTeam().getValue() == MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE;

        // In Pickleball, the serving team's score is mentioned first
        String formattedScore = isBlueServing
                ? String.format(Locale.US, "%d-%d-%d", blue, red, server)
                : String.format(Locale.US, "%d-%d-%d", red, blue, server);

        txtScore.setText(formattedScore);
    }

    /**
     * Updates the text of the fault button to reflect if it will trigger a side-out.
     */
    private void updateButtonLabels() {
        MatchViewModel.SERVER server = viewModel.getServer().getValue();
        btnFault.setText(server == MatchViewModel.SERVER.ONE ? "FAULT" : "SIDEOUT");
    }

    /**
     * Handles logic when a team's score is updated.
     */
    private void handleScoreUpdate(boolean isBlueTeam, int newScore) {
        if (isGameOver()) return;

        checkForWinner(isBlueTeam, newScore);
        toggleServingIndicator(isBlueTeam);
        updateScoreText();
    }

    /**
     * Checks if the current score meets win conditions (at least 11 points and win by 2).
     */
    private void checkForWinner(boolean isBlueTeam, int currentScore) {
        int oppositeScore = isBlueTeam ? getVal(viewModel.getRedScore()) : getVal(viewModel.getBlueScore());
        if (currentScore >= 11 && (currentScore - oppositeScore) >= 2) {
            viewModel.setGameIsOver(true);
            showWinnerDialog((isBlueTeam ? "Blue" : "Red") + " Team Wins!");
        }
    }

    /**
     * Toggles the serving indicator (paddle image) between the two players on a team.
     */
    private void toggleServingIndicator(boolean isBlueTeam) {
        if (isBlueTeam) {
            rotateVisibility(binding.imgServingBlueTop, binding.imgServingBlueBottom);
        } else {
            rotateVisibility(binding.imgServingRedTop, binding.imgServingRedBottom);
        }
    }

    private void rotateVisibility(View v1, View v2) {
        boolean isV1Visible = v1.getVisibility() == View.VISIBLE;
        v1.setVisibility(isV1Visible ? View.INVISIBLE : View.VISIBLE);
        v2.setVisibility(isV1Visible ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * Resets the match to the starting state (0-0-2).
     */
    private void resetMatchToInitialState() {
        viewModel.setCurrentServingTeam(MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE);
        viewModel.setServer(MatchViewModel.SERVER.TWO);
        viewModel.setBlueScore(0);
        viewModel.setRedScore(0);

        txtScore.setText("0-0-2");

        binding.imgServingBlueTop.setVisibility(View.VISIBLE);
        binding.imgServingBlueBottom.setVisibility(View.INVISIBLE);
        binding.imgServingRedTop.setVisibility(View.INVISIBLE);
        binding.imgServingRedBottom.setVisibility(View.INVISIBLE);

        binding.txtPlayerBlueTop.setText("Player 1");
        binding.txtPlayerBlueBottom.setText("Player 2");
        binding.txtPlayerRedTop.setText("Player 3");
        binding.txtPlayerRedBottom.setText("Player 4");
    }

    /**
     * Displays an AlertDialog when a team wins.
     */
    private void showWinnerDialog(String message) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage(message)
                .setPositiveButton("OK", (d, w) -> {
                    d.dismiss();
                    viewModel.setGameIsOver(false);
                })
                .setCancelable(false)
                .create();

        dialog.show();
        applyFullScreenFlags(dialog);
    }

    /**
     * Ensures the dialog doesn't bring back the navigation bar.
     */
    private void applyFullScreenFlags(@NonNull AlertDialog dialog) {
        if (dialog.getWindow() != null) {
            WindowInsetsControllerCompat controller = ViewCompat.getWindowInsetsController(dialog.getWindow().getDecorView());
            if (controller != null) {
                controller.hide(WindowInsetsCompat.Type.systemBars());
                controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        }
    }

    /**
     * XML OnClick listener for the Score button.
     */
    public void onBtnScoreListener(View v) {
        boolean isBlueServing = viewModel.getCurrentServingTeam().getValue() == MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE;
        if (isBlueServing) {
            viewModel.setBlueScore(getVal(viewModel.getBlueScore()) + 1);
            swapPlayerLabels(binding.txtPlayerBlueTop, binding.txtPlayerBlueBottom);
        } else {
            viewModel.setRedScore(getVal(viewModel.getRedScore()) + 1);
            swapPlayerLabels(binding.txtPlayerRedTop, binding.txtPlayerRedBottom);
        }
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
        if (viewModel.getServer().getValue() == MatchViewModel.SERVER.TWO) {
            viewModel.setServer(MatchViewModel.SERVER.ONE);
            MatchViewModel.CURRENT_SERVING_TEAM current = viewModel.getCurrentServingTeam().getValue();
            viewModel.setCurrentServingTeam(current == MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE
                    ? MatchViewModel.CURRENT_SERVING_TEAM.TEAM_RED
                    : MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE);
        } else {
            toggleServingIndicator(viewModel.getCurrentServingTeam().getValue() == MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE);
            viewModel.setServer(MatchViewModel.SERVER.TWO);
        }
    }

    private boolean isGameOver() {
        return Boolean.TRUE.equals(viewModel.getGameIsOver().getValue());
    }

    private int getVal(LiveData<Integer> liveData) {
        Integer value = liveData.getValue();
        return value != null ? value : 0;
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
}
