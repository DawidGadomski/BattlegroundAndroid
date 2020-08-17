package com.example.battleground.Objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;

import androidx.core.content.ContextCompat;

import com.example.battleground.Background;
import com.example.battleground.GameDisplay;
import com.example.battleground.GameLoop;
import com.example.battleground.Joystick;
import com.example.battleground.R;

public class Player extends GameBeing{
    public static final double SPEED_PER_SECOND = 150.0; // speed pixel per second
    public static final double MAX_SPEED = SPEED_PER_SECOND / GameLoop.MAX_UPS;

    private Joystick joystick;
    private HealthBar healthBar;
    private Background background;
//    private Bitmap lightBitmap;
//    private Bitmap scaledLightBitmap;

    private int score;

    public Player(Context context, Joystick joystick, double posX, double posY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.player), posX, posY, radius);

        this.joystick = joystick;
        this.health = 100;
        this.maxHealth = 100;
        this.DMG = 50;
        this.score = 0;

        healthBar = new HealthBar(context, this);

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);

        deadBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.splat_red);
        matrix = new Matrix();
        matrix.setRotate((float)angle, (float)bitmap.getWidth()/2, (float)bitmap.getHeight()/2);
        rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

//        lightBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.light_350_med);
//        scaledLightBitmap = Bitmap.createScaledBitmap(lightBitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        background = new Background(context);
    }

    public void draw(Canvas canvas, GameDisplay gameDisplay){
        super.draw(canvas, gameDisplay);

        canvas.drawBitmap(rotatedBitmap, (float)gameDisplay.getCordX(posX), (float)gameDisplay.getCordY(posY), null);
//        canvas.drawBitmap(scaledLightBitmap, (float)gameDisplay.getCordX(posX), (float)gameDisplay.getCordY(posY), null);
        healthBar.draw(canvas, gameDisplay);
        System.out.println("X: " + posX + " Y: " + posY);
    }

    public void update(int dispWidth, int dispHeight) {
        velX = joystick.getActuatorX()*MAX_SPEED;
        velY = joystick.getActuatorY()*MAX_SPEED;

        posX += velX;
        posY += velY;

        if(posX < 0){
            posX = 0;
        }
        if(posY < 0){
            posY = 0;
        }
        if(posX> background.getWidth() - dispWidth){
            posX = background.getWidth()- dispWidth;
        }


        if(posY> background.getHeight() - dispHeight){
            posY = background.getHeight()- dispHeight;
        }


        if(velX != 0 || velY != 0){
            double dist = getDistBetweenPoints(0, 0, velX, velY);
            directX = velX / dist;
            directY = velY / dist;

            angle = Math.atan2(directY, directX) * (180/Math.PI);
            matrix.setRotate((float)angle, (float)bitmap.getWidth()/2, (float)bitmap.getHeight()/2);
            rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        }

        if(health <= 0){
            bitmap = deadBitmap;
        }
    }



    public void getPoints(){
        this.score += 10;
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

    public int getScore() {
        return score;
    }
}
