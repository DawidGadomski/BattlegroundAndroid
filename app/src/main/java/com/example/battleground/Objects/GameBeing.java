package com.example.battleground.Objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GameBeing extends GameObject {
    protected Paint paint;
    protected int playerColor;

    public GameBeing(Context context, int color, double posX, double posY, double radius) {
        super(posX, posY, radius);

        paint = new Paint();
        playerColor = color;
        paint.setColor(playerColor);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle((float) posX, (float) posY, (float) radius, paint);
    }

    @Override
    public void update() {

    }
}
