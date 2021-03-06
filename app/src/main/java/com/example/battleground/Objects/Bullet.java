package com.example.battleground.Objects;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.battleground.GameDisplay;
import com.example.battleground.GameLoop;
import com.example.battleground.Objects.GameObject;
import com.example.battleground.Objects.Player;
import com.example.battleground.R;

public class Bullet extends GameObject {
    private static final double BULLET_MAX_SPEED = 400.0/ GameLoop.MAX_UPS;
    protected Paint paint;
    protected int color;
    private Player player;

    public Bullet(Context context, Player player) {
        super(player.getPosX(), player.getPosY(), 15);

        paint = new Paint();
        color = ContextCompat.getColor(context, R.color.bulletColor);
        paint.setColor(color);

        this.player = player;

        velX = player.getDirectX() * BULLET_MAX_SPEED;
        velY = player.getDirectY() * BULLET_MAX_SPEED;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet);
    }

    @Override
    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        canvas.drawBitmap(bitmap, (float)gameDisplay.getCordX(posX - bitmap.getWidth()/2f),
                (float)gameDisplay.getCordY(posY - bitmap.getHeight()/2f), paint);
    }

    public void update() {
        posX += velX;
        posY += velY;
    }
}
