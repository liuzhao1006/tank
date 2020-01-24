package com.syd.tank.domain;

public class Shell implements Runnable {

    int x;
    int y;
    int speed;
    boolean isLive;
    int size;
    boolean vertical;
    boolean right;
    boolean up;
    int boundX;
    int boundY;
    int viewSize;

    public Shell(int x, int y, int speed, boolean isLive, int size,
                 boolean isVertical, boolean isRight, boolean isUp, int boundX, int boundY, int viewSize) {
        this.x = x;
        this.y = y;
        this.speed = speed / 3;//
        this.isLive = isLive;
        this.size = size;
        vertical = isVertical;
        right = isRight;
        up = isUp;
        this.boundX = boundX;
        this.boundY = boundY;
        this.viewSize = viewSize;
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean isLive) {
        this.isLive = isLive;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void run() {

        while (true) {
            //炮弹判断是否应该死亡
            if (isLive == false || x < 0 || x > boundX || y < 0 || y > boundY) {
                isLive = false;
                break;
            }

            //炮弹判断是否应该死亡
            if (isLive == false || x < 0 || x > boundX || y < 0 || y > boundY) {
                isLive = false;
                break;
            }
            try {
                Thread.sleep(70);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
            if (vertical) {
                if (up)
                    y -= speed * viewSize;                //朝上炮管  放大倍数viewSize
                else
                    y += speed * viewSize;                //朝下炮管
            } else {
                if (right)
                    x += speed * viewSize;            //朝右炮管
                else
                    x -= speed * viewSize;            //朝左炮管
            }

        }

    }
}
