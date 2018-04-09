package com.example.tienthanh.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.widget.ToggleButton;

public class CheckBox_Roboto_Medium extends ToggleButton {
    public CheckBox_Roboto_Medium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CheckBox_Roboto_Medium(Context context) {
        super(context);
        init();
    }

    public CheckBox_Roboto_Medium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Black.ttf");
            setTypeface(tf);
        }
    }
}
