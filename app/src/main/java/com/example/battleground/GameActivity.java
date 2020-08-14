package com.example.battleground;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class GameActivity extends SurfaceView implements SurfaceHolder.Callback {
    private Player player;
    private GameLoop gameLoop;
    private SurfaceHolder surfaceHolder;



    public GameActivity(Context context){
        super(context);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        player = new Player(getContext(), 150, 300, 20);

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
        player.update();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                player.setPosition((double) event.getX(), (double) event.getY());
                return true;

            case MotionEvent.ACTION_MOVE:
                player.setPosition((double) event.getX(), (double) event.getY());
                return true;
        }


        return super.onTouchEvent(event);
    }
}
