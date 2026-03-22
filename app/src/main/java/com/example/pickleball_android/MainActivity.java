package com.example.pickleball_android;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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
        setContentView(R.layout.activity_main);

        onFullScreen();

        viewModel = new ViewModelProvider(this).get(MatchViewModel.class);
        TextView txtScore = findViewById(R.id.kitchen_layout).findViewById(R.id.txtScore);

        viewModel.getBlueScore().observe(this, blueScore -> {
            String score = String.join("-", blueScore + "", viewModel.getRedScore().getValue() + "", viewModel.getServer().getValue().getValue() + "");
            txtScore.setText(score);
        });


    }

    public void onBtnScoreListener(View v) {
        //scoring
        viewModel.setBlueScore(viewModel.getBlueScore().getValue() + 1);

    }
}