package com.example.battleground.Objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import androidx.core.content.ContextCompat;

import com.example.battleground.GameDisplay;
import com.example.battleground.GameLoop;
import com.example.battleground.R;

public class Enemy extends GameBeing{
    private static final double SPEED_PER_SECOND = 100.0; // speed pixel per second
    private static final double MAX_SPEED = SPEED_PER_SECOND / GameLoop.MAX_UPS;
    private static final double SPAWNS_PER_MIN = 20;
    private static final double SPAWNS_PER_SEC = SPAWNS_PER_MIN/60.0;
    private static final double ENEMY_SPAWN = GameLoop.MAX_UPS/SPAWNS_PER_SEC;
    private static double nextSpawn;

    private HealthBar healthBar;

    private Player player;

    public Enemy(Context context, Player player, double posX, double posY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.enemy), posX, posY, radius);
        this.player = player;
        nextSpawn = ENEMY_SPAWN;
        this.DMG = 10;
        this.health = 30;
        this.maxHealth = 30;

        healthBar = new HealthBar(context, this);

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable
        .zombie);

        matrix = new Matrix();
        matrix.setRotate((float)angle, (float)bitmap.getWidth()/2, (float)bitmap.getHeight()/2);
        rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public Enemy(Context context, Player player) {
        super(context, ContextCompat.getColor(context, R.color.enemy),
                Math.random()*1000, Math.random()*1000, 20);
        this.player = player;
        nextSpawn = ENEMY_SPAWN;
        this.DMG = 10;
        this.health = 30;
        this.maxHealth = 30;

        healthBar = new HealthBar(context, player);

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable
                .zombie);
        deadBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable
                .splat_green);

        matrix = new Matrix();
        matrix.setRotate((float)angle, (float)bitmap.getWidth()/2, (float)bitmap.getHeight()/2);
        rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

    }

    public static boolean isSpawn() {
        if(nextSpawn <= 0){
            nextSpawn += ENEMY_SPAWN;
            return true;
        }
        nextSpawn--;

        return false;
    }



    public void draw(Canvas canvas, GameDisplay gameDisplay){
        super.draw(canvas, gameDisplay);
        canvas.drawBitmap(rotatedBitmap, (float)gameDisplay.getCordX(posX), (float)gameDisplay.getCordY(posY), paint);
        healthBar.draw(canvas, gameDisplay);
    }


    @Override
    public void update() {
        double distToPlayerX = player.getPosX() - posX;
        double distToPlayerY = player.getPosY() - posY;

        double distToPlayer = GameObject.getDistBetweenObj(this, player);

        double directX = distToPlayerX/distToPlayer;
        double directY = distToPlayerY/distToPlayer;

        angle = Math.atan2(directY, directX) * (180/Math.PI);
        matrix.setRotate((float)angle, (float)bitmap.getWidth()/2, (float)bitmap.getHeight()/2);
        rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        if(distToPlayer > 0) {
            velX = directX*MAX_SPEED;
            velY = directY*MAX_SPEED;
        }else {
            velX = 0;
            velY = 0;
        }

        posX += velX;
        posY +=velY;

        if(health <= 0){
            bitmap = deadBitmap;
        }
    }



}
