package com.syd.tank.domain;

public class Boom {
    int x;
    int y;
    int time;
    boolean isLive;
    boolean isVertical;

    public Boom(int x, int y, boolean isVertical) {
        super();
        this.x = x;
        this.y = y;
        this.time = 48;
        this.isLive = true;
        this.isVertical = isVertical;
    }

    public boolean isVertical() {
        return isVertical;
    }

    public void setVertical(boolean isVertical) {
        this.isVertical = isVertical;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean isLive) {
        this.isLive = isLive;
    }


    public void timeGo() {
        if (time > 0)
            time -= 5;
        else isLive = false;
    }
}
