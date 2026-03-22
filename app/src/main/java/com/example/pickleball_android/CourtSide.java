package com.example.pickleball_android;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.BindingAdapter;

public class CourtSide extends ConstraintLayout {
    private TextView txtPlayer;
    private ImageView imgServing;
    private int teamSide = -1; // 0 for red, 1 for blue

    public CourtSide(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.court_side, this);
        txtPlayer = findViewById(R.id.txtPlayer);
        imgServing = findViewById(R.id.img_serving);

        if (attrs == null) {
            return;
        }

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CourtSide);
        String playerName = a.getString(R.styleable.CourtSide_playerName);
//        teamSide = a.getInt(R.styleable.CourtSide_currentServingTeam, -1);
//
//        setPlayerName(playerName != null ? playerName : "");
        a.recycle();

    }

    public void setPlayerName(String text) {
        if (txtPlayer != null) {
            txtPlayer.setText(text);
        }
    }

    @BindingAdapter("matchScore")
    public static void setMatchScore(CourtSide view, MatchScore score) {
        System.out.println("score:" + score);
        if (score.getCurrentServingTeam() == MatchScore.CURRENT_SERVING_TEAM.TEAM_BLUE) {
            if (score.getServer() == MatchScore.SERVER.TWO) {
                view.findViewById(R.id.court_side_blue_top).findViewById(R.id.img_serving).setVisibility(View.VISIBLE);
            }

        }
//        if (score == null || view.imgServing == null || view.teamSide == -1) return;
//
//        boolean isRedServing = score.getCurrentServingTeam() == MatchScore.CURRENT_SERVING_TEAM.TEAM_RED;
//        boolean isBlueServing = score.getCurrentServingTeam() == MatchScore.CURRENT_SERVING_TEAM.TEAM_BLUE;
//
//        if (view.teamSide == 0) { // Red Team
//            view.imgServing.setVisibility(isRedServing ? View.VISIBLE : View.INVISIBLE);
//        } else if (view.teamSide == 1) { // Blue Team
//            view.imgServing.setVisibility(isBlueServing ? View.VISIBLE : View.INVISIBLE);
//        }
    }
}