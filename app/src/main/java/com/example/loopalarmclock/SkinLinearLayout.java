package com.example.loopalarmclock;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class SkinLinearLayout extends LinearLayout {
    private Context context;
    public SkinLinearLayout(Context context) {
        super(context);
        this.context=context;
    }

    public SkinLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

}
