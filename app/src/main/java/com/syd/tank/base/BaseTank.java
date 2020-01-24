package com.syd.tank.base;

import com.syd.tank.domain.ComputerTank;
import com.syd.tank.domain.Shell;

import java.util.Vector;

public class BaseTank {
    protected int boundX;                                 //屏幕宽度
    protected int boundY;                                  //屏幕高度
    private int size = 1;                       //坦克放大倍数
    private int x = 0;                          //坦克x坐标
    private int y = 0;                          //坦克y坐标
    private int hp = 0;                         //坦克血量
    private int speed = 0;                      //坦克速度
    private boolean isLive = true;              //是否活着
    private boolean isVertical = true;          //竖直状态
    private boolean isRight = false;           //朝右炮管
    private boolean isUp = true;                //朝上炮管
    private Vector<Shell> tankShells = null;   //炮弹组
    private Vector<Thread> tankThreads = null;      //炮弹线程对象组
    private Vector<ComputerTank> toucheds = null;           //传入敌人坦克组  用来判断是否与敌人坦克触碰 吗（包括敌人坦克判断是否与敌人坦克触碰）


    public void setLive(boolean live) {
        isLive = live;
    }

    public void setVertical(boolean vertical) {
        isVertical = vertical;
    }

    public void setRight(boolean right) {
        isRight = right;
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    public Vector<Shell> getTankShells() {
        return tankShells;
    }

    public void setTankShells(Vector<Shell> tankShells) {
        this.tankShells = tankShells;
    }

    public Vector<Thread> getTankThreads() {
        return tankThreads;
    }

    public void setTankThreads(Vector<Thread> tankThreads) {
        this.tankThreads = tankThreads;
    }

    public Vector<ComputerTank> getToucheds() {
        return toucheds;
    }

    public void setToucheds(Vector<ComputerTank> toucheds) {
        this.toucheds = toucheds;
    }

    public BaseTank(int x, int y, int hp, int speed, boolean isVertical,
                    boolean isRight, boolean isUp, Vector<ComputerTank> toucheds, int boundX, int boundY, int size) {
        super();
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.speed = speed * size;
        this.isVertical = isVertical;
        this.isRight = isRight;
        this.isUp = isUp;
        this.toucheds = toucheds;
        this.boundX = boundX;
        this.boundY = boundY;
        this.size = size;

    }


    public BaseTank() {
    }

    //扣血
    public void buckleBlood() {
        if (hp > 1) {
            hp--;
        } else {
            isLive = false;
        }

    }

    public boolean isLive() {
        return isLive;
    }


    public void setIsLive(boolean isLive) {
        this.isLive = isLive;
    }


    public boolean isVertical() {
        return isVertical;
    }

    public void setIsVertical(boolean isVertical) {
        this.isVertical = isVertical;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setIsRight(boolean isRight) {
        this.isRight = isRight;
    }

    public boolean isUp() {
        return isUp;
    }

    public void setIsUp(boolean isUp) {
        this.isUp = isUp;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    // 向右走
    public void goRight() {
        if (x < boundX - 36 * size) {
            x += speed;
            isVertical = false;
            isRight = true;
        }
    }

    // 向左走
    public void goLeft() {
        if (x > speed) {
            x -= speed;
            isVertical = false;
            isRight = false;
        }
    }

    // 向下走
    public void goDown() {
        if (y < boundY - 36 * size) {
            y += speed;
            isVertical = true;
            isUp = false;
        }
    }

    // 向上走
    public void goUp() {
        if (y > speed) {
            y -= speed;
            isVertical = true;
            isUp = true;
        }
    }

    //判断坦克头是否与其他坦克触碰
    public boolean judgeTankTouch() {

        if (toucheds != null && isLive) {


            for (int i = 0; i < toucheds.size(); i++) {
                BaseTank touched = toucheds.get(i);
                if (touched != this) {
                    int xOne, yOne, xTwo, yTwo, edx, edy;      //取A坦克头的2个点  只要这两个点都不在被B触碰坦克里 那么就知道A坦克没有被碰到B

                    edx = touched.getX();
                    edy = touched.getY();
                    if (isVertical) {
                        if (this.isUp()) {
                            xOne = x;
                            yOne = y;

                            xTwo = x + 34 * size;
                            yTwo = y;

                        } else {
                            xOne = x;
                            yOne = y + 36 * size;

                            xTwo = x + 34 * size;
                            yTwo = y + 36 * size;
                        }

                    } else {
                        if (isRight) {
                            xOne = x + 36 * size;
                            yOne = y;

                            xTwo = x + 36 * size;
                            yTwo = y + 34 * size;
                        } else {
                            xOne = x;
                            yOne = y;

                            xTwo = x;
                            yTwo = y + 34 * size;
                        }
                    }
                    if (touched.isVertical()) {
                        //取A坦克头的2个点  只要这两个点都不在被B触碰坦克里 那么就知道A坦克没有被碰到B
                        if ((xOne >= edx && xOne <= edx + 34 * size && yOne >= edy && yOne <= edy + 36 * size)
                                || (xTwo >= edx && xTwo <= edx + 34 * size && yTwo >= edy && yTwo <= edy + 36 * size)) {
                            return true;

                        }

                    } else {
                        //取A坦克头的2个点  只要这两个点都不在被B触碰坦克里 那么就知道A坦克没有被碰到B
                        if ((xOne >= edx && xOne <= edx + 36 * size && yOne >= edy && yOne <= edy + 34 * size)
                                || (xTwo >= edx && xTwo <= edx + 34 * size && yTwo >= edy && yTwo <= edy + 36 * size)) {
                            return true;
                        }

                    }
                }
            }
        }
        return false;     //若没有触碰则输出false
    }

    //发射炮弹
    public void shutShell() {
        if (tankShells == null) {
            tankShells = new Vector<Shell>();
            tankThreads = new Vector<Thread>(); //是炮弹对象与炮弹线程对象同步
        }
        if (isVertical) {
            if (isUp) {
                tankShells.add(new Shell(x + 15 * size, y - 18 * size, 15 * size,
                        true, 1 * size, isVertical, isRight, isUp, boundX, boundY, size));       //朝上炮管
            } else {
                tankShells.add(new Shell(x + 15 * size, y + 18 * size, 15 * size,
                        true, 1 * size, isVertical, isRight, isUp, boundX, boundY, size));       //朝下炮管
            }
        } else {
            if (isRight) {
                tankShells.add(new Shell(x + 52 * size, y + 16 * size, 15 * size, true, 1,
                        isVertical, isRight, isUp, boundX, boundY, size));
            }//朝右炮管
            else {
                tankShells.add(new Shell(x - 18 * size, y + 16 * size, 15 * size, true, 1,
                        isVertical, isRight, isUp, boundX, boundY, size));               //朝左炮管
            }
        }
        //创建炮弹
        Thread sheelThread = new Thread(tankShells.get(tankShells.size() - 1));
        tankThreads.add(sheelThread);
        sheelThread.start();
    }
}
