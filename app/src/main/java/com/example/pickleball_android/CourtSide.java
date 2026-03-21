package com.example.pickleball_android;
import android.content.Context;
import android.util.AttributeSet;
import androidx.constraintlayout.widget.ConstraintLayout;

public class CourtSide extends ConstraintLayout {
    public CourtSide(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.court_side, this);
    }
}
