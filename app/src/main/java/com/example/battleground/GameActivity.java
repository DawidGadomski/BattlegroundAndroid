package com.example.battleground;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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

public class GameActivity extends SurfaceView implements SurfaceHolder.Callback {
    private final Joystick joystick;
    private Player player;
//    private Enemy enemy;
    private List<Enemy> enemyList;
    private GameLoop gameLoop;
    private SurfaceHolder surfaceHolder;



    public GameActivity(Context context){
        super(context);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        enemyList = new ArrayList<Enemy>();


        joystick = new Joystick(getContext(), 300, 300, 40, 70);
        player = new Player(getContext(), joystick, 150, 300, 20);
//        enemy = new Enemy(getContext(), player, 250, 250, 20);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
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
        drawUPS(canvas);
        drawFPS(canvas);
        player.draw(canvas);
        joystick.draw(canvas);
        for(Enemy e : enemyList){
            e.draw(canvas);
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

    public void update() {
        joystick.update();
        player.update();
        if(Enemy.isSpawn()){
            enemyList.add(new Enemy(getContext(), player));
        }
        for(Enemy e : enemyList){
            e.update();
        }
        Iterator<Enemy> enemyIterator = enemyList.iterator();
        while(enemyIterator.hasNext()){
            if(GameObject.isColide(enemyIterator.next(), player)){
                enemyIterator.remove();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(joystick.isPressed((double) event.getX(), (double) event.getY())){
                    joystick.setIsPressed(true);
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                if(joystick.getIsPressed()){
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
                joystick.setIsPressed(false);
                joystick.resetActuator();
                return true;
        }


        return super.onTouchEvent(event);
    }
}
