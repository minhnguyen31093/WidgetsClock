package com.myst.kingdomheartsclock.ui;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.myst.kingdomheartsclock.R;

import java.util.Calendar;

/**
 * Created by minh on 2/22/18.
 */

public class CustomClock extends ConstraintLayout {

    private ImageView imgFrame, imgBg, imgDot, imgH, imgM, imgS, imgDot2, imgFg;

    public CustomClock(Context context) {
        super(context);
        init(context, null);
    }

    public CustomClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(getContext(), R.layout.custom_clock, this);

        imgFrame = findViewById(R.id.imgFrame);
        imgBg = findViewById(R.id.imgBg);
        imgDot = findViewById(R.id.imgDot);
        imgH = findViewById(R.id.imgH);
        imgM = findViewById(R.id.imgM);
        imgS = findViewById(R.id.imgS);
        imgDot2 = findViewById(R.id.imgDot2);
        imgFg = findViewById(R.id.imgFg);

        updateTime();
    }

    public void updateTime() {
        Calendar calendar = Calendar.getInstance();
        int newSecond = calendar.get(Calendar.SECOND);
        int newMinute = calendar.get(Calendar.MINUTE);
        int newHour = calendar.get(Calendar.HOUR);

        float s = newSecond * 360f / 60f;
        float m = (newMinute + newSecond / 60f) * 360f / 60f;
        float h = (newHour + (newMinute + newSecond / 60f) / 60f) * 360f / 12f;

        imgS.setRotation(s);
        imgM.setRotation(m);
        imgH.setRotation(h);
    }
}
