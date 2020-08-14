package com.example.battleground.Objects;

import android.graphics.Canvas;

import androidx.core.graphics.drawable.WrappedDrawable;

public abstract class GameObject {
    protected double posX;
    protected double posY;
    protected double velX;
    protected double velY;
    protected double radius;

    public GameObject(double posX, double posY, double radius) {
        this.posX = posX;
        this.posY = posY;
        this.radius = radius;
    }

    protected static double getDistBetweenObj(GameObject obj1, GameObject obj2) {
        return Math.sqrt(Math.pow(obj2.getPosX() - obj1.getPosX(), 2) + Math.pow(obj2.getPosY() - obj1.getPosY(), 2));
    }

    public static boolean isColide(GameObject obj1, GameObject obj2) {
        double dist = getDistBetweenObj(obj1, obj2);
        double colisionDist = obj1.radius + obj2.radius;
        if(dist < colisionDist) {
            return true;
        }

        return false;
    }

    public abstract void draw(Canvas canvas);

    public abstract void update();

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }



}
