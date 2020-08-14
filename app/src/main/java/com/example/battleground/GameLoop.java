package com.example.battleground;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameLoop extends Thread{
    public static final double MAX_UPS = 60.0;
    private static final double UPS_PERIOD = 1E+3/MAX_UPS;

    private GameActivity gameActivity;
    private boolean isRunning;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

//    Time and Cycle
    private int updates;
    private int frames;

    private long startTime;
    private long elapsedTime;
    private long sleepTime;

    private double UPS;
    private double FPS;

    public GameLoop(GameActivity gameActivity, SurfaceHolder surfaceHolder) {
        this.gameActivity = gameActivity;
        this.surfaceHolder = surfaceHolder;
    }

    public double getFPS() {
        return FPS;
    }

    public double getUPS() {
        return UPS;
    }

    public void startLoop() {
        isRunning = true;
        start();
    }

    @Override
    public void run() {
        super.run();
        canvas = null;
        frames = 0;
        updates = 0;

//      Game Loop
        while (isRunning){
            try{
                canvas = surfaceHolder.lockCanvas();

                synchronized (surfaceHolder){ //zapobiega wielokrotnemu użyciu update i draw
                    gameActivity.update();
                    updates++;

                    gameActivity.draw(canvas);
                }
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }   finally {
                if(canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        frames++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            elapsedTime = System.currentTimeMillis() - startTime;
            sleepTime = (long) (updates*UPS_PERIOD - elapsedTime);
            if(sleepTime > 0){
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (sleepTime < 0 && updates < MAX_UPS - 1){
                gameActivity.update();
                updates++;
                elapsedTime = System.currentTimeMillis() - startTime;
                sleepTime = (long) (updates*UPS_PERIOD - elapsedTime);
            }



//          Calculations UPS and FPS
            elapsedTime = System.currentTimeMillis() - startTime;
            if(elapsedTime >= 1000){
                UPS = updates / (1E-3 * elapsedTime);
                FPS =  frames / (1E-3 * elapsedTime);
                updates = 0;
                frames = 0;
                startTime = System.currentTimeMillis();

            }

        }
    }


}
