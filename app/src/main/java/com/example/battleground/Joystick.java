package com.example.battleground;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public class Joystick {
    private int posX;
    private int posY;
    private int boundPosX;
    private int boundPosY;

    private int joystickRadius;
    private int boundRadius;

    private Paint joystickPaint;
    private int joystickColor;
    private Paint boundsPaint;
    private int boundsColor;

    private double distanceFromCenter;
    private boolean isPressed;
    private double actuatorX;
    private double actuatorY;

    public Joystick(Context context, int posX, int posY, int joystickRadius, int boundRadius) {
        this.posX = posX;
        this.posY = posY;
        boundPosX = posX;
        boundPosY = posY;

        this.joystickRadius = joystickRadius;
        this.boundRadius = boundRadius;

        joystickPaint = new Paint();
        joystickColor = ContextCompat.getColor(context, R.color.joystickColor);
        joystickPaint.setColor(joystickColor);
        joystickPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        boundsPaint = new Paint();
        boundsColor = ContextCompat.getColor(context, R.color.boundsColor);
        boundsPaint.setColor(boundsColor);
        boundsPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(boundPosX, boundPosY, boundRadius, boundsPaint);
        canvas.drawCircle(posX, posY, joystickRadius, joystickPaint);


    }

    public void update() {
        updateJoystick();
    }

    private void updateJoystick() {
        posX = (int) (boundPosX + actuatorX*boundRadius);
        posY = (int) (boundPosY + actuatorY*boundRadius);
    }

    public boolean isPressed(double x, double y) {
        distanceFromCenter = Math.sqrt(Math.pow(boundPosX - x, 2) + Math.pow(boundPosY - y, 2));
        return distanceFromCenter < boundRadius;
    }


    public void setActuator(double x, double y) {

        double dist = distanceFromCenter = Math.sqrt(Math.pow(x - boundPosX, 2) + Math.pow(y - boundPosY, 2));

        if(dist < boundRadius) {
            actuatorX = (x - boundPosX) / boundRadius;
            actuatorY = (y - boundPosY) / boundRadius;
        } else {
            actuatorX = (x - boundPosX) / dist;
            actuatorY = (y - boundPosY) / dist;
        }
    }

    public void resetActuator() {
        actuatorY = 0.0;
        actuatorX = 0.0;
    }

    public void setIsPressed(boolean b) {
        this.isPressed = b;
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public double getActuatorX() {
        return actuatorX;
    }

    public double getActuatorY() {
        return actuatorY;
    }

    public void moveJoystick(int posX, int posY){
        this.posX = posX;
        this.posY = posY;
    }
}
