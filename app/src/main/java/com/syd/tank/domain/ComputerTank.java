package com.syd.tank.domain;

import com.syd.tank.base.BaseTank;

import java.util.Vector;

public class ComputerTank extends BaseTank implements Runnable {
    int type = 0;               //坦克类型
    int stay = 100;             //坦克持续一个动作至少走100个像素
    int any = 0;                //随机数

    public ComputerTank(int x, int y, int type, Vector<ComputerTank> toucheds, int boundX, int boundY, int size) {
        super(x, y, type, 1, true, false, false, toucheds, boundX, boundY, size);
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    //让电脑随机自动跑，每个动作持续stay个像素
    public void run() {
        while (true) {
            if (super.isLive() == false) {
                break;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (stay <= 0) {
                any = (int) (Math.random() * 10 % 4);
                stay = 60;
            } else if (super.getX() > super.boundX)                             //设置边界x（也就是右边界） 680
            {
                any = 1;
            } else if (super.getX() <= super.getSpeed())                           //设置x左边界  0
            {
                any = 0;
            } else if (super.getY() > super.boundY)                           //设置Y的边界  （也就是下边界）490
            {
                any = 2;
            } else if (super.getY() <= super.getSpeed())                          //设置Y上边界 0
            {
                any = 3;
            }
            if (any == 0) {
                goRight();                      //向右跑若触碰到坦克 则往反方向跑
                if (judgeTankTouch())            //下面也一样
                {                               //遇到问题先相信自己逻辑没问题，下去看看是否是低级错误
                    goLeft();                   //在去判断是否是逻辑问题经验当逻辑没有问题  日了狗了白浪费30分钟 错误居然是IF后面加了个“；”
                    any = 1;
                }

            } else if (any == 1) {
                goLeft();
                if (judgeTankTouch()) {
                    goRight();
                    any = 0;
                }
            } else if (any == 2) {
                goUp();
                if (judgeTankTouch()) {
                    goDown();
                    any = 3;
                }
            } else if (any == 3) {
                goDown();
                if (judgeTankTouch()) {
                    goUp();
                    any = 2;
                }
            }
            stay--;
            if ((int) (Math.random() * 100 % 30) == 1)    //随机打炮弹
            {
                this.shutShell();
            }

        }

    }
}
