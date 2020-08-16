package com.example.battleground.Objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.battleground.R;

public class HealthBar {
    private GameBeing gameBeing;

    private int width;
    private int height;
    private int margin;

    private Paint borderPaint;
    private int borderColor;
    private Paint healthPaint;
    private int healthColor;


    public HealthBar(Context context, GameBeing gameBeing){
        this.gameBeing = gameBeing;

        this.width = 50;
        this.height = 10;
        this.margin = 2;
        this.borderPaint = new Paint();
        this.healthPaint = new Paint();
        this.borderColor = ContextCompat.getColor(context, R.color.healthBorderColor);
        this.healthColor = ContextCompat.getColor(context, R.color.healthColor);

        this.borderPaint.setColor(borderColor);
        this.healthPaint.setColor(healthColor);

    }

    public void draw(Canvas canvas) {
        float x = (float) gameBeing.getPosX();
        float y = (float) gameBeing.getPosY();
        float dist = 30;
        float healthPct = (float) gameBeing.getHealth()/gameBeing.getMaxHealth();

        float leftBorder, rightBorder, topBorder, bottomBorder;
        leftBorder = x - width/2;
        rightBorder = x + width/2;
        bottomBorder = y - dist;
        topBorder = bottomBorder - height;

        float leftHealthBorder, rightHealthBorder, topHealthBorder, bottomHealthBorder;
        float healthWidth, healthHeight;
        healthWidth = width - 2*margin;
        healthHeight = height - 2*margin;

        leftHealthBorder = leftBorder + margin;
        rightHealthBorder = leftHealthBorder + healthWidth*healthPct;
        bottomHealthBorder = bottomBorder - margin;
        topHealthBorder = bottomHealthBorder - healthHeight;

        canvas.drawRect(leftBorder, topBorder, rightBorder, bottomBorder, borderPaint);

        canvas.drawRect(leftHealthBorder,topHealthBorder, rightHealthBorder, bottomHealthBorder, healthPaint);
    }
}
