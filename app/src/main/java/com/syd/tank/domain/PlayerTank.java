package com.syd.tank.domain;

import com.syd.tank.base.BaseTank;

import java.util.Vector;

public class PlayerTank extends BaseTank {
    int life = 10;   //命数
    int score = 0;   //分数


    public PlayerTank(int x, int y, int score, Vector<ComputerTank> toucheds, int boundX, int boundY, int size) {
        super(x, y, 4, 7, true, false, true, toucheds, boundX, boundY, size);
        this.score = score;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public void buckleLife() {
        this.life--;
    }
}