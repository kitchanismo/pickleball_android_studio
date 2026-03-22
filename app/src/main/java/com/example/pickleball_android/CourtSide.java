package com.example.pickleball_android;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class CourtSide extends ConstraintLayout {
    private TextView txtPlayer;

    public CourtSide(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.court_side, this);
        txtPlayer = findViewById(R.id.txtPlayer);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CourtSide);
            String playerName = a.getString(R.styleable.CourtSide_playerName);
            if (playerName != null) {
                setPlayerName(playerName);
            }
            a.recycle();
        }
    }

    public void setPlayerName(String text) {
        if (txtPlayer != null) {
            txtPlayer.setText(text);
        }
    }
}