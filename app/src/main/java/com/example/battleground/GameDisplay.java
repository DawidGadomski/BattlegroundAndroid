package com.example.battleground;

import com.example.battleground.Objects.GameObject;

public class GameDisplay {
    private int width;
    private int height;
    private GameObject player;
    private double dispCenterX;
    private double dispCenterY;
    private double offsetX;
    private double offsetY;
    private double gameCenterX;
    private double gameCenterY;

    public GameDisplay(int width, int height, GameObject player){
        this.width = width;
        this.height = height;
        this.player = player;

        dispCenterX = width / 2.0;
        dispCenterY = height / 2.0;

        update();
    }

    public void update(){
        gameCenterX = player.getPosX();
        gameCenterY = player.getPosY();

        offsetX = dispCenterX - gameCenterX;
        offsetY = dispCenterY - gameCenterY;
    }

    public double getCordX(double x){
        return x +offsetX;
    }

    public double getCordY(double y){
        return y + offsetY;
    }
}


