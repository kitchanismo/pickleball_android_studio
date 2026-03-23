package com.example.pickleball_android;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.pickleball_android.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private MatchViewModel viewModel;

    private ActivityMainBinding binding;

    private void onFullScreen() {
        EdgeToEdge.enable(this);

        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController != null) {
            // Hide both status bar and navigation bar
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
            // Make them reappear with a swipe
            windowInsetsController.setSystemBarsBehavior(
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            );
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Remove top and bottom padding to let content fill the entire screen
            v.setPadding(systemBars.left, 0, systemBars.right, 0);
            return insets;
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onFullScreen();

        viewModel = new ViewModelProvider(this).get(MatchViewModel.class);
        TextView txtScore = binding.kitchenLayout.findViewById(R.id.txtScore);

        binding.imgServingBlueTop.setVisibility(View.VISIBLE);

        viewModel.getCurrentServingTeam().observe(this, currentServingTeam -> {
            if (viewModel.getGameIsOver().getValue()) {
                return;
            }
            Boolean isBlue = viewModel.getCurrentServingTeam().getValue() == MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE;
            //  viewModel.setCurrentServingTeam(isBlue ? MatchViewModel.CURRENT_SERVING_TEAM.TEAM_RED : MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE);
            if (!isBlue) {
                binding.imgServingRedBottom.setVisibility(View.VISIBLE);
                binding.imgServingBlueTop.setVisibility(View.INVISIBLE);
                binding.imgServingBlueBottom.setVisibility(View.INVISIBLE);
                String scoreText = String.join("-", viewModel.getRedScore().getValue() + "", viewModel.getBlueScore().getValue() + "", viewModel.getServer().getValue().getValue() + "");
                txtScore.setText(scoreText);
            } else {
                binding.imgServingBlueTop.setVisibility(View.VISIBLE);
                binding.imgServingRedTop.setVisibility(View.INVISIBLE);
                binding.imgServingRedBottom.setVisibility(View.INVISIBLE);
                String scoreText = String.join("-", viewModel.getBlueScore().getValue() + "", viewModel.getRedScore().getValue() + "", viewModel.getServer().getValue().getValue() + "");
                txtScore.setText(scoreText);
            }

            System.out.println("hit" + currentServingTeam);
        });

        viewModel.getServer().observe(this, server -> {
            if (viewModel.getGameIsOver().getValue()) {
                return;
            }
            Button btnFault = binding.kitchenLayout.findViewById(R.id.btn_fault);
            btnFault.setText(server == MatchViewModel.SERVER.ONE ? "FAULT" : "SIDEOUT");
            Boolean isBlue = viewModel.getCurrentServingTeam().getValue() == MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE;
            if (isBlue) {
                String scoreText = String.join("-", viewModel.getBlueScore().getValue() + "", viewModel.getRedScore().getValue() + "", viewModel.getServer().getValue().getValue() + "");
                txtScore.setText(scoreText);
            } else {
                String scoreText = String.join("-", viewModel.getRedScore().getValue() + "", viewModel.getBlueScore().getValue() + "", viewModel.getServer().getValue().getValue() + "");
                txtScore.setText(scoreText);
            }

        });

        viewModel.getGameIsOver().observe(this, isGameOver -> {
            if (!viewModel.getGameIsOver().getValue()) {
                return;
            }
            viewModel.setCurrentServingTeam(MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE);
            viewModel.setServer(MatchViewModel.SERVER.TWO);
            viewModel.setBlueScore(0);
            viewModel.setRedScore(0);
            String scoreText = String.join("-", "0", "0", "2");
            binding.imgServingBlueTop.setVisibility(View.VISIBLE);
            binding.imgServingBlueBottom.setVisibility(View.INVISIBLE);
            binding.imgServingRedTop.setVisibility(View.INVISIBLE);
            binding.imgServingRedBottom.setVisibility(View.INVISIBLE);
            binding.txtPlayerBlueTop.setText("Player 1");
            binding.txtPlayerBlueBottom.setText("Player 2");
            binding.txtPlayerRedBottom.setText("Player 4");
            binding.txtPlayerRedTop.setText("Player 3");
            txtScore.setText(scoreText);
        });

        viewModel.getBlueScore().observe(this, blueScore -> {
            if (viewModel.getGameIsOver().getValue()) {
                return;
            }

            Integer winBlue = viewModel.getBlueScore().getValue() - viewModel.getRedScore().getValue();
            if (viewModel.getBlueScore().getValue() >= 11 && winBlue >= 2) {
                System.out.println("Blue Wins");
                viewModel.setGameIsOver(true);
                showWinnerDialog("Blue Team Wins!");

                return;
            }

            String scoreText = String.join("-", blueScore + "", viewModel.getRedScore().getValue() + "", viewModel.getServer().getValue().getValue() + "");
            txtScore.setText(scoreText);

            if (binding.imgServingBlueBottom.getVisibility() == View.VISIBLE) {
                binding.imgServingBlueBottom.setVisibility(View.INVISIBLE);
            } else {
                binding.imgServingBlueBottom.setVisibility(View.VISIBLE);
            }
            if (binding.imgServingBlueTop.getVisibility() == View.VISIBLE) {
                binding.imgServingBlueTop.setVisibility(View.INVISIBLE);
            } else {
                binding.imgServingBlueTop.setVisibility(View.VISIBLE);
            }


        });

        viewModel.getRedScore().observe(this, redScore -> {
            if (viewModel.getGameIsOver().getValue()) {
                return;
            }

            Integer winRed = viewModel.getRedScore().getValue() - viewModel.getBlueScore().getValue();
            if (viewModel.getRedScore().getValue() >= 11 && winRed >= 2) {
                System.out.println("Red Wins");
                viewModel.setGameIsOver(true);
                showWinnerDialog("Red Team Wins!");

                return;
            }

            if (binding.imgServingRedBottom.getVisibility() == View.VISIBLE) {
                binding.imgServingRedBottom.setVisibility(View.INVISIBLE);
            } else {
                binding.imgServingRedBottom.setVisibility(View.VISIBLE);
            }
            if (binding.imgServingRedTop.getVisibility() == View.VISIBLE) {
                binding.imgServingRedTop.setVisibility(View.INVISIBLE);
            } else {
                binding.imgServingRedTop.setVisibility(View.VISIBLE);
            }

            String scoreText = String.join("-", redScore + "", viewModel.getBlueScore().getValue() + "", viewModel.getServer().getValue().getValue() + "");
            txtScore.setText(scoreText);

        });
    }

    private void showWinnerDialog(String winnerMessage) {
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage(winnerMessage)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    viewModel.setGameIsOver(false);
                })
                .setCancelable(false)
                .show();
    }

    public void onBtnScoreListener(View v) {
        //scoring
        if (viewModel.getCurrentServingTeam().getValue() == MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE) {
            viewModel.setBlueScore(viewModel.getBlueScore().getValue() + 1);
            String txtPlayerBlueTopValue = binding.txtPlayerBlueTop.getText().toString();
            String txtPlayerBlueBottomValue = binding.txtPlayerBlueBottom.getText().toString();
            binding.txtPlayerBlueTop.setText(txtPlayerBlueBottomValue);
            binding.txtPlayerBlueBottom.setText(txtPlayerBlueTopValue);
        } else {
            viewModel.setRedScore(viewModel.getRedScore().getValue() + 1);
            String txtPlayerRedTopValue = binding.txtPlayerRedTop.getText().toString();
            String txtPlayerRedBottomValue = binding.txtPlayerRedBottom.getText().toString();
            binding.txtPlayerRedTop.setText(txtPlayerRedBottomValue);
            binding.txtPlayerRedBottom.setText(txtPlayerRedTopValue);
        }
    }

    public void onBtnFaultListener(View v) {
        if (viewModel.getServer().getValue() == MatchViewModel.SERVER.TWO) {
            viewModel.setServer(MatchViewModel.SERVER.ONE);
            System.out.println("fault" + viewModel.getServer().getValue());
            viewModel.setCurrentServingTeam(viewModel.getCurrentServingTeam().getValue() == MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE ? MatchViewModel.CURRENT_SERVING_TEAM.TEAM_RED : MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE);
            return;
        }

        if (viewModel.getCurrentServingTeam().getValue() == MatchViewModel.CURRENT_SERVING_TEAM.TEAM_BLUE) {
            if (binding.imgServingBlueBottom.getVisibility() == View.VISIBLE) {
                binding.imgServingBlueBottom.setVisibility(View.INVISIBLE);
            } else {
                binding.imgServingBlueBottom.setVisibility(View.VISIBLE);
            }
            if (binding.imgServingBlueTop.getVisibility() == View.VISIBLE) {
                binding.imgServingBlueTop.setVisibility(View.INVISIBLE);
            } else {
                binding.imgServingBlueTop.setVisibility(View.VISIBLE);
            }

        } else {
            if (binding.imgServingRedBottom.getVisibility() == View.VISIBLE) {
                binding.imgServingRedBottom.setVisibility(View.INVISIBLE);
            } else {
                binding.imgServingRedBottom.setVisibility(View.VISIBLE);
            }
            if (binding.imgServingRedTop.getVisibility() == View.VISIBLE) {
                binding.imgServingRedTop.setVisibility(View.INVISIBLE);
            } else {
                binding.imgServingRedTop.setVisibility(View.VISIBLE);
            }
        }
        viewModel.setServer(MatchViewModel.SERVER.TWO);

    }

}
