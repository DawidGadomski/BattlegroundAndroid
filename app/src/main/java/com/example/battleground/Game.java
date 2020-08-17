package com.example.battleground;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.battleground.Objects.Bullet;
import com.example.battleground.Objects.Enemy;
import com.example.battleground.Objects.GameBeing;
import com.example.battleground.Objects.GameObject;
import com.example.battleground.Objects.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * This class is responsible for updates and render all objects to the screen
 */
public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private GameLoop gameLoop;
    private GameOver gameOver;
    private DisplayMetrics displayMetrics;
    private GameDisplay gameDisplay;
    private SurfaceHolder surfaceHolder;

    private Background background;

    private Joystick joystick;
    private int joystickPointerID = 0;
    private Player player;
    private int cooldown = 0;

    private List<Enemy> enemyList;
    private List<Bullet> bulletsList;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private int highscore;

    private Paint fogPaint;
    private int fogColor;

    private AudioAttributes audioAttributes;
    private SoundPool soundPool;
    private int shootSound;
    private int zombieSound;
    private int takeDMGSound;
    private int startSound;
    private int splatSound;

    public Game(Context context){
        super(context);

        displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        background = new Background(getContext());

        pref = getContext().getSharedPreferences("game", MODE_PRIVATE);
        editor = pref.edit();

        enemyList = new ArrayList<Enemy>();
        bulletsList = new ArrayList<Bullet>();

        joystick = new Joystick(getContext(), 100, displayMetrics.heightPixels - 100, 20, 50);
        player = new Player(getContext(), joystick, 150, 300, 20);

        gameDisplay = new GameDisplay(displayMetrics.widthPixels, displayMetrics.heightPixels, player);

        fogPaint = new Paint();
        fogColor = ContextCompat.getColor(context, R.color.fogColor);
        fogPaint.setColor(fogColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        shootSound = soundPool.load(getContext(), R.raw.pistol, 1);
        zombieSound = soundPool.load(getContext(), R.raw.zombie, 1);
        takeDMGSound = soundPool.load(getContext(), R.raw.take_dmg, 1);
        startSound = soundPool.load(getContext(), R.raw.level_start, 1);
        splatSound = soundPool.load(getContext(), R.raw.splat, 1);

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
        if(player.getHealth() <= 0){
            gameLoop.setIsOver(true);
            gameLoop.setIsRunning(false);
            if(pref.getInt("highscore", -1) < player.getScore()){
                editor.putInt("highscore",player.getScore());
                editor.commit();
            }
            return;
        }

        canvas.translate((float)-player.getPosX(), (float)-player.getPosY());
        background.draw(canvas);

        canvas.translate((float) player.getPosX(), (float) player.getPosY());

        player.draw(canvas, gameDisplay);
        joystick.draw(canvas);

        for(Enemy e : enemyList){
            e.draw(canvas, gameDisplay);
        }
        for(Bullet b : bulletsList){
            b.draw(canvas, gameDisplay);
        }

        canvas.drawRect(0, 0, displayMetrics.widthPixels,
                displayMetrics.heightPixels, fogPaint);

        drawUPS(canvas);
        drawFPS(canvas);


    }

    public void update() {
        joystick.update();
        player.update(displayMetrics.widthPixels, displayMetrics.heightPixels);

        if(Enemy.isSpawn()){
            soundPool.play(zombieSound, 1, 1, 0, 0, 1);
            enemyList.add(new Enemy(getContext(), player));
        }

        for(Enemy e : enemyList){
            e.update();
        }

        while (cooldown > 0){
            soundPool.play(shootSound, 1, 1, 0, 0, 1);
            bulletsList.add(new Bullet(getContext(), player));
            cooldown --;
        }

        for(Bullet b : bulletsList){
            b.update();
        }

        // Collision detection of player, bullets and enemies
        Iterator<Enemy> enemyIterator = enemyList.iterator();
        while(enemyIterator.hasNext()){
            GameBeing enemy = enemyIterator.next();
            if(GameObject.isColide(enemy, player)){
                enemyIterator.remove();
                soundPool.play(takeDMGSound, 1, 1, 0, 0, 1);
                player.takeDMG(enemy.getDMG());
                player.getPoints();
                continue;
            }

            Iterator<Bullet> bulletIterator = bulletsList.iterator();
            while(bulletIterator.hasNext()){
                if(GameObject.isColide(bulletIterator.next(), enemy)){
                    bulletIterator.remove();
                    enemyIterator.remove();
                    soundPool.play(splatSound, 1, 1, 0, 0, 1);
                    player.getPoints();
                    break;
                }
            }
        }
        // keep player in center of screen
        gameDisplay.update();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if ( joystick.getIsPressed() ){
                    // joystick was pressed before = shoot bullet
                    cooldown++;
                }
                else if(joystick.isPressed((double) event.getX(), (double) event.getY())){
                    // joystick is pressed = isPressed = true and get id of pointer
                    joystickPointerID = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                } else{
                    // joystick was and is not pressed = shoot bullet
                    cooldown++;
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                if(joystick.getIsPressed()){
                    // joystick is pressed and moved
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if(joystickPointerID == event.getPointerId(event.getActionIndex())){
                    // joystick pointer was let go off = isPressed = false and reset
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void pause(){
        gameLoop.stopLoop();
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

    public int getScore(){
        return player.getScore();
    }
}
