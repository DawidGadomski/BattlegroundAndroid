package com.example.battleground.Objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GameBeing extends GameObject {
    protected Paint paint;
    protected int playerColor;

    protected int health;
    protected int maxHealth;

    protected int DMG;

    protected Bitmap deadBitmap;

    public GameBeing(Context context, int color, double posX, double posY, double radius) {
        super(posX, posY, radius);

        paint = new Paint();
        playerColor = color;
        paint.setColor(playerColor);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, (float)posX, (float)posY, paint);
    }

    @Override
    public void update() {

    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void takeDMG(int DMG) {
        this.health -= DMG;
        if(health < 0 ){
            health = 0;
        }
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getDMG() {
        return DMG;
    }

    public void setDMG(int DMG) {
        this.DMG = DMG;
    }
}
