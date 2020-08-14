package com.example.battleground.Objects;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.battleground.GameLoop;
import com.example.battleground.Joystick;
import com.example.battleground.R;

public class Player extends GameBeing{
    public static final double SPEED_PER_SECOND = 400.0; // speed pixel per second
    public static final double MAX_SPEED = SPEED_PER_SECOND / GameLoop.MAX_UPS;

    private Joystick joystick;


    public Player(Context context, Joystick joystick, double posX, double posY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.player), posX, posY, radius);

        this.joystick = joystick;
    }

    public void update() {
        velX = joystick.getActuatorX()*MAX_SPEED;
        velY = joystick.getActuatorY()*MAX_SPEED;
        posX += velX;
        posY += velY;
    }

    public void setPosition(double x, double y) {
        this.posX = x;
        this.posY = y;
    }


}
