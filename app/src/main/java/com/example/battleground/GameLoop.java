package com.example.battleground;

import android.content.Intent;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameLoop extends Thread{
    public static final double MAX_UPS = 60.0;
    private static final double UPS_PERIOD = 1E+3/MAX_UPS;

    private Game game;
    private boolean isRunning;
    private boolean isOver;

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

    public GameLoop(Game game, SurfaceHolder surfaceHolder) {
        this.game = game;
        this.surfaceHolder = surfaceHolder;
    }

    public void startLoop() {
        isRunning = true;
        isOver = false;
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
                    game.update();
                    updates++;

                    game.draw(canvas);
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
//          Stops game to not exceed UPS
            elapsedTime = System.currentTimeMillis() - startTime;
            sleepTime = (long) (updates*UPS_PERIOD - elapsedTime);
            if(sleepTime > 0){
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//          Skip frames to keep up UPS
            while (sleepTime < 0 && updates < MAX_UPS - 1){
                game.update();
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
        if(isOver){
            Intent intent = new Intent(game.getContext(), GameOver.class);
            intent.putExtra("score", game.getScore());
            game.getContext().startActivity(intent);
        }
    }

    public void stopLoop(){
        isRunning = false;
        try {
            join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void setIsOver(boolean b){
        this.isOver = b;
    }

    public void setIsRunning(boolean running) {
        isRunning = running;
    }

    public double getFPS() {
        return FPS;
    }

    public double getUPS() {
        return UPS;
    }
}
