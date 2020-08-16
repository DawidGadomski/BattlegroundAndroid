package com.example.battleground.Objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;

import androidx.core.content.ContextCompat;

import com.example.battleground.GameLoop;
import com.example.battleground.Joystick;
import com.example.battleground.R;

public class Player extends GameBeing{
    public static final double SPEED_PER_SECOND = 150.0; // speed pixel per second
    public static final double MAX_SPEED = SPEED_PER_SECOND / GameLoop.MAX_UPS;

    private Joystick joystick;
    private HealthBar healthBar;

    private double angle = 0;





    public Player(Context context, Joystick joystick, double posX, double posY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.player), posX, posY, radius);

        this.joystick = joystick;
        this.health = 100;
        this.maxHealth = 100;
        this.DMG = 50;

        healthBar = new HealthBar(context, this);

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);

        deadBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.splat_red);
        matrix = new Matrix();
        rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public void draw(Canvas canvas){
        super.draw(canvas);

        matrix.setRotate((float)angle);
        rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        canvas.drawBitmap(rotatedBitmap, (float)posX, (float)posY, paint);
        healthBar.draw(canvas);
    }

    public void update() {
        velX = joystick.getActuatorX()*MAX_SPEED;
        velY = joystick.getActuatorY()*MAX_SPEED;
        posX += velX;
        posY += velY;

        if(velX != 0 || velY != 0){
            double dist = getDistBetweenPoints(0, 0, velX, velY);
            directX = velX / dist;
            directY = velY / dist;

            angle = Math.atan2(directY, directX) * (180/Math.PI);




        }




        if(health <= 0){
            bitmap = deadBitmap;
        }
    }

    public void setPosition(double x, double y) {
        this.posX = x;
        this.posY = y;
    }

    public double getDirectX(){
        return directX;
    }

    public double getDirectY(){
        return directY;
    }


}
