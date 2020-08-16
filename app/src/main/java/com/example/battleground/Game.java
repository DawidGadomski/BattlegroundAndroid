package com.example.battleground;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.battleground.Objects.Enemy;
import com.example.battleground.Objects.GameBeing;
import com.example.battleground.Objects.GameObject;
import com.example.battleground.Objects.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private Joystick joystick;
    private Player player;
//    private Enemy enemy;
    private List<Enemy> enemyList;
    private List<Bullet> bulletsList;
    private GameLoop gameLoop;
    private GameOver gameOver;
    private DisplayMetrics displayMetrics;
    private GameDisplay gameDisplay;
    private SurfaceHolder surfaceHolder;
    private int joystickPointerID = 0;
    private int cooldown = 0;
    private Background background;


    public Game(Context context){
        super(context);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        background = new Background(getContext());

        enemyList = new ArrayList<Enemy>();
        bulletsList = new ArrayList<Bullet>();


        joystick = new Joystick(getContext(), 300, 300, 40, 70);
        player = new Player(getContext(), joystick, 150, 300, 20);


        displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        gameDisplay = new GameDisplay(displayMetrics.widthPixels, displayMetrics.heightPixels, player);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        if(gameLoop.getState().equals(Thread.State.TERMINATED)){
            SurfaceHolder surfaceHolder1 = getHolder();
            surfaceHolder.addCallback(this);
            gameLoop = new GameLoop(this, surfaceHolder);
        }
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.translate((float)-player.getPosX(), (float)-player.getPosY());
        background.draw(canvas);

        canvas.translate((float) player.getPosX(), (float) player.getPosY());
        drawUPS(canvas);
        drawFPS(canvas);
        player.draw(canvas);
        joystick.draw(canvas);
        for(Enemy e : enemyList){
            e.draw(canvas);
        }
        for(Bullet b : bulletsList){
            b.draw(canvas);
        }

        if(player.getHealth() <= 0){

        }
    }

    public void drawUPS(Canvas canvas){
        String UPS = Double.toString(gameLoop.getUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.colorWhite);
        paint.setColor(color);
        paint.setTextSize(20);
        canvas.drawText("FPS: " + UPS, 100, 40, paint);
    }

    public void drawFPS(Canvas canvas){
        String FPS = Double.toString(gameLoop.getFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.colorWhite);
        paint.setColor(color);
        paint.setTextSize(20);
        canvas.drawText("FPS: " + FPS, 100, 100, paint);
    }

    public void pause(){
        gameLoop.stopLoop();
    }

    public void update() {
        if(player.getHealth() <= 0){
            return;
        }

        joystick.update();
        player.update();
        if(Enemy.isSpawn()){
            enemyList.add(new Enemy(getContext(), player));
        }



        for(Enemy e : enemyList){
            e.update();
        }

        while (cooldown > 0){
            bulletsList.add(new Bullet(getContext(), player));
            cooldown --;
        }
        for(Bullet b : bulletsList){
            b.update();
        }
        Iterator<Enemy> enemyIterator = enemyList.iterator();
        while(enemyIterator.hasNext()){
            GameBeing enemy = enemyIterator.next();
            if(GameObject.isColide(enemy, player)){
                enemyIterator.remove();
                player.takeDMG(enemy.getDMG());
                continue;
            }
            Iterator<Bullet> bulletIterator = bulletsList.iterator();
            while(bulletIterator.hasNext()){
                if(GameObject.isColide(bulletIterator.next(), enemy)){
                    bulletIterator.remove();
                    enemyIterator.remove();
                    break;

                }
            }
        }

//        gameDisplay.update();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if ( joystick.getIsPressed() ){
                    cooldown++;
                }
                else if(joystick.isPressed((double) event.getX(), (double) event.getY())){
                    joystickPointerID = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                } else{
                    cooldown++;
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                if(joystick.getIsPressed()){
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if(joystickPointerID == event.getPointerId(event.getActionIndex())){
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                }

                return true;
        }


        return super.onTouchEvent(event);
    }
}
