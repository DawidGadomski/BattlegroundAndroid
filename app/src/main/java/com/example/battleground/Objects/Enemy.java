package com.example.battleground.Objects;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.battleground.GameLoop;
import com.example.battleground.R;

public class Enemy extends GameBeing{
    private static final double SPEED_PER_SECOND = 300.0; // speed pixel per second
    private static final double MAX_SPEED = SPEED_PER_SECOND / GameLoop.MAX_UPS;
    private static final double SPAWNS_PER_MIN = 20;
    private static final double SPAWNS_PER_SEC = SPAWNS_PER_MIN/60.0;
    private static final double ENEMY_SPAWN = GameLoop.MAX_UPS/SPAWNS_PER_SEC;
    private static double nextSpawn;

    private Player player;

    public Enemy(Context context, Player player, double posX, double posY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.enemy), posX, posY, radius);
        this.player = player;
        nextSpawn = ENEMY_SPAWN;
    }

    public Enemy(Context context, Player player) {
        super(context, ContextCompat.getColor(context, R.color.enemy),
                Math.random()*1000, Math.random()*1000, 20);
        this.player = player;
        nextSpawn = ENEMY_SPAWN;

    }

    public static boolean isSpawn() {
        if(nextSpawn <= 0){
            nextSpawn += ENEMY_SPAWN;
            return true;
        }
        nextSpawn--;

        return false;
    }

    @Override
    public void update() {
        double distToPlayerX = player.getPosX() - posX;
        double distToPlayerY = player.getPosY() - posY;

        double distToPlayer = GameObject.getDistBetweenObj(this, player);

        double directX = distToPlayerX/distToPlayer;
        double directY = distToPlayerY/distToPlayer;

        if(distToPlayer > 0) {
            velX = directX*MAX_SPEED;
            velY = directY*MAX_SPEED;
        }else {
            velX = 0;
            velY = 0;
        }

        posX += velX;
        posY +=velY;
    }


}
