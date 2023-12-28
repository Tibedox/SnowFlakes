package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;

public class SnowFlake {
    float x, y;
    float width, height;
    float vx, vy;
    float angle, speedRotation;

    public SnowFlake() {
        respawn();
    }

    void fly() {
        x += vx;
        y += vy;
        angle += speedRotation;
        if(y < 0 - height) {
            respawn();
        }
    }

    void respawn() {
        width = height = MathUtils.random(10, 50);
        x = MathUtils.random(0, MyGame.SCR_WIDTH);
        y = MathUtils.random(MyGame.SCR_HEIGHT, MyGame.SCR_HEIGHT*2);
        vy = MathUtils.random(-3, -1);
        vx = MathUtils.random(-1f, 1f);
        speedRotation = MathUtils.random(-0.5f, 0.5f);
    }

    boolean hit(float touchX, float touchY){
        return x<touchX & touchX<x+width & y<touchY & touchY<y+height;
    }
}
