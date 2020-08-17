package com.example.battleground.Objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.battleground.GameDisplay;
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

    float leftBorder, rightBorder, topBorder, bottomBorder;
    float leftHealthBorder, rightHealthBorder, topHealthBorder, bottomHealthBorder;
    float healthWidth, healthHeight;

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

    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        float x = (float) gameBeing.getPosX();
        float y = (float) gameBeing.getPosY();
        float dist = 30;
        float healthPct = (float) gameBeing.getHealth()/gameBeing.getMaxHealth();

        leftBorder = x - width/2f;
        rightBorder = x + width/2f;
        bottomBorder = y - dist;
        topBorder = bottomBorder - height;

        healthWidth = width - 2*margin;
        healthHeight = height - 2*margin;

        leftHealthBorder = leftBorder + margin;
        rightHealthBorder = leftHealthBorder + healthWidth*healthPct;
        bottomHealthBorder = bottomBorder - margin;
        topHealthBorder = bottomHealthBorder - healthHeight;

        canvas.drawRect((float)gameDisplay.getCordX(leftBorder),
                (float)gameDisplay.getCordY(topBorder),
                (float)gameDisplay.getCordX(rightBorder),
                (float)gameDisplay.getCordY(bottomBorder), borderPaint);

        canvas.drawRect((float)gameDisplay.getCordX(leftHealthBorder),
                (float)gameDisplay.getCordY(topHealthBorder),
                (float)gameDisplay.getCordX(rightHealthBorder),
                (float)gameDisplay.getCordY(bottomHealthBorder),
                healthPaint);
    }
}
