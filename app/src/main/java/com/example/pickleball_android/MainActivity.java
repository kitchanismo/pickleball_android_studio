package com.example.pickleball_android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private GameViewModel viewModel;

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

        viewModel = new ViewModelProvider(this).get(GameViewModel.class);

        Button btnFault = findViewById(R.id.btn_fault);

        // Find the CourtSide views
        TextView txtPlayerTop = findViewById(R.id.court_side_red_top).findViewById(R.id.txtPlayer);
        TextView txtPlayerBottom = findViewById(R.id.court_side_red_bottom).findViewById(R.id.txtPlayer);

        btnFault.setOnClickListener(v -> {
            String playerNameRedTop = txtPlayerTop.getText().toString();
            String playerNameRedBottom = txtPlayerBottom.getText().toString();
            txtPlayerTop.setText(playerNameRedBottom);
            txtPlayerBottom.setText(playerNameRedTop);
        });

        // Observe LiveData and update both TextViews
        viewModel.playerName.observe(this, playerName -> {

        });
    }
}