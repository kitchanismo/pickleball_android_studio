package com.example.pickleball_android;

import android.content.Context;
import android.util.AttributeSet;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Kitchen extends ConstraintLayout {
    public Kitchen(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.kitchen, this);
    }
}
