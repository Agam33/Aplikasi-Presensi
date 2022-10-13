package com.orb.myapplication.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.orb.myapplication.R;


public class CsButton extends AppCompatButton {

    private Drawable enableBackground;
    private Drawable disableBackground;
    private int textColor = 0;
    private TypedArray typedArray;

    public CsButton(@NonNull Context context) {
        super(context);
        init();
    }

    public CsButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CsButton,
                0, 0
        );
        init();
    }

    public CsButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CsButton,
                0, 0
        );
        init();
    }

    public void init() {
        textColor = ContextCompat.getColor(getContext(), android.R.color.background_light);
        enableBackground = ContextCompat.getDrawable(getContext(), R.drawable.enable_btn);
        disableBackground = ContextCompat.getDrawable(getContext(), R.drawable.disable_btn);
        try {
            int enableColor = typedArray.getColor(R.styleable.CsButton_setEnableBackground,0);
            int disableColor = typedArray.getColor(R.styleable.CsButton_setDisableBackground, 0);
            setEnableBackground(enableColor);
            setDisableBackground(disableColor);
        } catch (Exception e) {
            e.printStackTrace();
            typedArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackground(
                isEnabled() ? enableBackground: disableBackground
        );
        setTextColor(textColor);
        setGravity(Gravity.CENTER);
        setElevation(4f);
    }

    public void setEnableBackground(int tintColor) {
        enableBackground.setTint(tintColor);
    }

    public void setDisableBackground(int tintColor) {
        disableBackground.setTint(tintColor);
    }

}

/*
    author: Riswan Agam
    email: riswanagam@gmail.com
 */

