package com.example.battleground;

import com.example.battleground.Objects.GameObject;

public class GameDisplay {
    private int width;
    private int height;
    private GameObject gameObject;
    private double dispCenterX;
    private double dispCenterY;
    private double offsetX;
    private double offsetY;
    private double gameCenterX;
    private double gameCenterY;

    public GameDisplay(int width, int height, GameObject gameObject){
        this.width = width;
        this.height = height;
        this.gameObject = gameObject;

        dispCenterX = width / 2.0;
        dispCenterY = height / 2.0;

        update();
    }

    public void update(){
        gameCenterX = gameObject.getPosX();
        gameCenterY = gameObject.getPosY();

        offsetX = dispCenterX - gameCenterX;
        offsetY = dispCenterY - gameCenterY;
    }

    public double getCordX(double x){
//        if(x < 0){
//            return 0;
//        }

        return x +offsetX;
    }

    public double getCordY(double y){
//        if(y<0){
//            return 0;
//        }
        return y + offsetY;
    }
}


