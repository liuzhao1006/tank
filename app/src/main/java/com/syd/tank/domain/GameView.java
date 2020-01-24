package com.syd.tank.domain;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.syd.tank.R;
import com.syd.tank.base.BaseTank;

import java.util.Vector;

public class GameView extends View implements Runnable {

    public static final String TAG = "GameView";


    private final byte COM_NUMBER = 5;                      //敌人坦克默认最大数量
    private final int MAJOR_MOVE = 10;
    //定义手势检测器实例
    private GestureDetector detector;
    private boolean isFirst = true;
    private int mHeight;                                    //本view的高度
    private int mWidth;                                     //本view的宽度

    private int size = 2;                                    //设置物体大小
    private byte nowComNumber;                               //敌人坦克数量

    private Bitmap[] pageBooms = null;                       //图片数组

    private PlayerTank myTank = null;                        //玩家坦克
    private Vector<ComputerTank> comTanks = null;            //敌人坦克集
    private Vector<Thread> comTankThreads = null;            //敌人坦克线程集  （因为考虑到线性所以用了Vector)
    private Vector<Boom> booms = null;                       //爆炸对象集
    private int [] boomBitmapId = {R.mipmap.icon_one,
            R.mipmap.icon_two,R.mipmap.icon_three,
            R.mipmap.icon_four,R.mipmap.icon_five,
            R.mipmap.icon_six,R.mipmap.icon_seven,
            R.mipmap.icon_eight};

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //初始化
        nowComNumber = COM_NUMBER;
        //自己的方法，加载图像一项目文件为根目录
        pageBooms = new Bitmap[7];
        for (int i = 0; i < pageBooms.length; i++) {
            pageBooms[i] = BitmapFactory.decodeResource(getResources(), boomBitmapId[i]);
            pageBooms[i] = Bitmap.createScaledBitmap(pageBooms[i], 60 * size, 80 * size, true);

        }
        initGestureDetector(context);
    }


    public GameView(Context context) {
        super(context);


        //初始化
        nowComNumber = COM_NUMBER;
        //自己的方法，加载图像一项目文件为根目录
        pageBooms = new Bitmap[7];
        for (int i = 0; i < pageBooms.length; i++) {
            pageBooms[i] = BitmapFactory.decodeResource(getResources(), boomBitmapId[i]);
            pageBooms[i] = Bitmap.createScaledBitmap(pageBooms[i], 60 * size, 80 * size, true);

        }

        initGestureDetector(context);


    }


    //初始化手指滑动
    private void initGestureDetector(Context context) {
        //创建手势检测器 手指单下为起点 手指离开为终点

        detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            boolean doRun = false;
            int turn = 0;  // 0 上      1 下         2  左       3  右        -1 停

            Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (true) {
                        try {
                            sleep(80);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (!myTank.isLive()) {
                            turn = -1;
                        }

                        switch (turn) {
                            case 0:
                                myTank.goUp();
                                break;

                            case 1:
                                myTank.goDown();
                                break;

                            case 2:
                                myTank.goLeft();
                                break;

                            case 3:
                                myTank.goRight();
                                break;


                            default:
                                try {
                                    sleep(30);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                }
            };

            //在按下被调用
            @Override
            public boolean onDown(MotionEvent motionEvent) {
//                turn = -1;
                return false;
            }

            //在按住时被调用
            @Override
            public void onShowPress(MotionEvent motionEvent) {

                turn = -1;


            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            //滑动的时候被调用
            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }


            //长按时候被调用
            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            //在抛掷动作时被调用
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                int dx = (int) (e2.getX() - e1.getX()); //计算滑动的距离
                if (Math.abs(dx) > MAJOR_MOVE && Math.abs(velocityX) > Math.abs(velocityY)) { //降噪处理，必须有较大的动作才识别
                    //加入没有开启线程则开启线程
                    if (!doRun) {
                        thread.start();
                        doRun = true;
                    }

                    if (velocityX > 0) {
//向右边
                        turn = 3;
                    } else {
//向左边
                        turn = 2;
                    }
                    return true;
                } else {

                    if (velocityY > 0) {
//向下边
                        turn = 1;
                    } else {
//向上边
                        turn = 0;
                    }
                    return true;
                }
            }
        });
    }

    /**
     * 初始化tank位置 等等属性
     */
    private void initTank() {


        comTanks = new Vector<ComputerTank>();

        comTankThreads = new Vector<Thread>();
        myTank = new PlayerTank(mWidth / 2 - 12 * size, mHeight - 50 * size, 1, comTanks, mWidth, mHeight, size);
        booms = new Vector<Boom>();
        for (int i = 0; i < nowComNumber; i++) {
            ComputerTank comOne = new ComputerTank(mWidth / nowComNumber * i, 0,
                    (int) (Math.random() * 10 / 3 + 1), comTanks, mWidth, mHeight, size);
            comTanks.add(comOne);
            Thread adThread = new Thread(comOne);
            comTankThreads.add(adThread);               //进行同步线程同步
            adThread.start();
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {


        //获取view的高度和宽度
        mHeight = getHeight();
        mWidth = getWidth();


        //未初始化 则初始化
        if (isFirst) {
            initTank();
            isFirst = false;

        }


        Paint paint = new Paint();
        ComputerTank comOne = null;
        Shell comShell = null;
        Shell myShell = null;
        Vector<Shell> myShells = myTank.getTankShells();
        if (myShells != null) {
            for (int i = 0; i < myShells.size(); i++)                 //画我的坦克炮弹
            {
                myShell = myShells.get(i);
                paintShell(myShell, myTank, canvas, paint);
            }
        }

        //玩家坦克
        paintTanks(myTank, canvas, paint, true);

        for (int i = 0; i < comTanks.size(); i++) {


            comOne = comTanks.get(i);
            Vector<Shell> comShells = comOne.getTankShells();
            if (comShells != null) {
                for (int j = 0; j < comShells.size(); j++)                 //画敌人的坦克炮弹
                {
                    comShell = comShells.get(j);
                    paintShell(comShell, comOne, canvas, paint);
                }
            }
            //敌人坦克
            paintTanks(comOne, canvas, paint, false);

        }
        painBoom(booms, canvas, paint);                                                //画爆炸
    }


    //画爆炸
    public void painBoom(Vector<Boom> booms, Canvas canvas, Paint paint) {
        Boom boom = null;
        if (booms != null) {
            for (int i = 0; i < booms.size(); i++) {
                boom = booms.get(i);

                if (boom != null) {
                    if (boom.isVertical()) {               //动画帧数
                        canvas.drawBitmap(pageBooms[boom.getTime() / 7], boom.getX(), boom.getY(), paint);
//                        g.drawImage(pageBooms[boom.getTime()/7], boom.getX(),boom.getY(),
//                                51, 54, this);
                    } else {
                        canvas.drawBitmap(pageBooms[boom.getTime() / 7], boom.getX(), boom.getY(), paint);
//                        g.drawImage(pageBooms[boom.getTime()/7], boom.getX(),boom.getY(),
//                                54, 51, this);
                    }

                    boom.timeGo();
                    if (boom.isLive() == false) {
                        booms.remove(boom);
                        boom = null;
                    }

                }
            }
        }
    }


    //画坦克
    public void paintTanks(BaseTank tank, Canvas canvas, Paint paint, boolean isPlayer) {


        int x = tank.getX();
        int y = tank.getY();
        int hp = tank.getHp();
        boolean isVertical = tank.isVertical();
        boolean isUp = tank.isUp();
        boolean isRight = tank.isRight();

        boolean isLive = tank.isLive();

        if (isPlayer) {
            paint.setColor(Color.BLUE);                      /*g.setColor(Color.yellow);*/
        } else {
            switch (hp) {
                //不同类型不同颜色    灰1血     白2血    红3血   黄4血
                case 1:
                    paint.setColor(Color.GRAY);      /* g.setColor(Color.red);break;  */
                    break;

                case 2:
                    paint.setColor(Color.LTGRAY);    /*g.setColor(Color.cyan);break;  */
                    break;

                case 3:
                    paint.setColor(Color.WHITE);      /*g.setColor(Color.white);break; */
                    break;

                case 4:
                    paint.setColor(Color.YELLOW);       /*g.setColor(Color.gray);break;  */
                    break;

                default:
                    break;
            }
        }
        if (isLive) {
            if (isVertical) {
                canvas.drawRect(x, y, x + 8 * size, y + 36 * size, paint);
//                g.fill3DRect(x, y, 8,36 ,true);       //左履带

                canvas.drawRect(x + 24 * size, y, x + 32 * size, y + 36 * size, paint);
//                g.fill3DRect(x+26, y, 8,36 ,true);        //右履带
                for (int i = 0; i < 6; i++)             //履带条纹
                {
                    canvas.drawRect(x, y + 6 * i * size, x + 7 * size, y + 6 * i * size, paint);
//                    g.drawLine(x, y+6*i, x+7, y+6*i);

                    canvas.drawRect(x, y + 6 * i * size, x + 7 * size, y + 6 * i * size, paint);
//                    g.drawLine(x+26, y+6*i, x+33, y+6*i);
                }

                canvas.drawRect(x + 5 * size, y + 3 * size, x + 29 * size, y + 33 * size, paint);
//                g.fill3DRect(x+5, y+3, 24,30 ,true);          //车体
                if (isUp) {
                    canvas.drawRect(x + 14 * size, y - 18 * size, x + 18 * size, y + 17 * size, paint);
//                    g.fillRect(x+15, y-18, 4,35 );                //朝上炮管
                } else {
                    canvas.drawRect(x + 14 * size, y + 18 * size, x + 18 * size, y + 53 * size, paint);
//                    g.fillRect(x+15, y+18, 4,35 );                //朝下炮管
                }
            } else {
                canvas.drawRect(x - 1 * size, y + 1 * size, x + 35 * size, y + 9 * size, paint);
//                g.fill3DRect(x-1, y+1 ,  36 ,8,true);         //上履带

                canvas.drawRect(x - 1 * size, y + 27 * size, x + 35 * size, y + 35 * size, paint);
//                g.fill3DRect(x-1, y+27, 36 ,8,true);          //下履带
                for (int i = 0; i < 6; i++)                     //履带条纹
                {
                    //                  g.drawLine();
                    //                  g.drawLine();
                }

                canvas.drawRect(x + 2 * size, y + 6 * size, x + 32 * size, y + 30 * size, paint);
//                g.fill3DRect(x+2, y+6,30 , 24,true);          //车体
                if (isRight) {
                    canvas.drawRect(x + 18 * size, y + 16 * size, x + 53 * size, y + 20 * size, paint);
//                    g.fillRect(x+18, y+16, 35,4);             //朝右炮管
                } else {
                    canvas.drawRect(x - 18 * size, y + 16 * size, x + 17 * size, y + 20 * size, paint);
//                    g.fillRect(x-18, y+16, 35,4 );                //朝左炮管
                }
            }
            canvas.drawRect(x + 7 * size, y + 8 * size, x + 27 * size, y + 28 * size, paint);
//            g.fillOval(x+7, y+8, 20, 20);             //盖子
        }
    }


    //画炮弹
    public void paintShell(Shell shell, BaseTank tank, Canvas canvas, Paint paint) {
        Vector<Shell> shells = tank.getTankShells();

        paint.setColor(Color.YELLOW);
//            g.setColor(Color.yellow);   //玩家子弹和敌人子弹


        if (shell.isLive()) {
            canvas.drawRect(shell.getX(), shell.getY(), shell.getX() + 4 * size, shell.getY() + 4 * size, paint);
//            g.fillRect(shell.getX(), shell.getY(), 4,4 );
        } else {
            int index = shells.indexOf(shell);

            Thread one = tank.getTankThreads().get(index);
            tank.getTankThreads().remove(one);
            shells.remove(index);          //删除线程对象
            shell = null;
            one = null;
        }


    }


    //判断敌人的坦克是否玩家被击中
    public void judgeTankKill(PlayerTank myTank,
                              Vector<ComputerTank> adms) {
        ComputerTank comOne = null;
        Shell myShell = null;
        Vector<Shell> shells = myTank.getTankShells();
        int coX, coY;   //(我方坦克被击中还未做) 已做
        int myShX, myShY;

        for (int i = 0; i < adms.size(); i++) {
            comOne = adms.get(i);
            coX = comOne.getX();
            coY = comOne.getY();

            if (shells != null) {
                for (int j = 0; j < shells.size(); j++) {
                    myShell = shells.get(j);
                    if (myShell != null) {
                        myShX = myShell.getX();
                        myShY = myShell.getY();

                        if (myTank.isVertical())        //坦克死亡时是否竖直  size是方大倍数
                        {
                            if (myShX + 4 * size > coX && myShX < coX + 34 * size && myShY + 4 * size > coY && myShY < coY + 36 * size) {
                                tankHpReduce(comOne, myShell, adms);

                            }
                        } else {
                            if (myShX + 4 * size > coX && myShY < coY + 34 * size && myShY + 4 * size > coY && myShX < coX + 36 * size) {
                                tankHpReduce(comOne, myShell, adms);
                            }
                        }
                    }
                }
            }
        }
    }


    //作用敌方坦克如果血量减1 如果血量为零则发生爆炸
    private void tankHpReduce(ComputerTank comOne, Shell myShell, Vector<ComputerTank> adms) {
        Vector<Shell> shells = myTank.getTankShells();
        comOne.buckleBlood();
        myShell.setLive(false);
        if (!comOne.isLive()) //判断敌人是否活着
        {
            //产生爆炸
            booms.add(new Boom(comOne.getX() - 2 * size,
                    comOne.getY() - 2 * size, comOne.isVertical()));
            //移去敌人坦克
            int index = adms.indexOf(comOne);
            Thread one = comTankThreads.get(index);
            comTankThreads.remove(one);       //移去线程敌人坦克对象
            one = null;
            adms.remove(comOne);
            comOne = null;
            //移去我的炮弹
            shells.remove(myShell);
            myShell = null;
            comOne = new ComputerTank(mWidth / 2, 0,       //产生新的敌人
                    (int) (Math.random() * 10 / 3 + 1), comTanks, mWidth, mHeight, size);
            adms.add(comOne);
            Thread adThread = new Thread(comOne);
            comTankThreads.add(adThread);
            adThread.start();                        //启动敌人自动走路线程

        }
    }

    //判断玩家的坦克是否被击中
    public void judgeMyTankKill(PlayerTank myTank,
                                Vector<ComputerTank> adm) {

        ComputerTank comOne = null;
        Shell comShell = null;
        int myX = myTank.getX();
        int myY = myTank.getY();   //(我方坦克被击中还未做)
        int comShX, comShY;
        if (myTank.isLive()) {
            for (int i = 0; i < adm.size(); i++) {
                comOne = adm.get(i);
                Vector<Shell> shells = comOne.getTankShells();
                if (shells != null) {
                    for (int j = 0; j < shells.size(); j++) {
                        comShell = shells.get(j);
                        if (comShell != null) {
                            comShX = comShell.getX();
                            comShY = comShell.getY();

                            if (myTank.isVertical())        //坦克死亡时是否竖直
                            {
                                if (comShX + 4 * size > myX && comShX < myX + 34 * size && comShY + 4 * size > myY && comShY < myY + 36 * size) {
                                    tankHpReduce(comShell, shells);

                                }
                            } else {
                                if (comShX + 4 * size > myX && comShY < myY + 34 * size && comShY + 4 * size > myY && comShX < myX + 36 * size) {
                                    tankHpReduce(comShell, shells);


                                }
                            }
                        }
                    }
                }
            }
        }
    }


    //作用我方坦克如果血量减1 如果血量为零则发生爆炸
    private void tankHpReduce(Shell comShell, Vector<Shell> shells) {
        myTank.buckleBlood();
        comShell.setLive(false);
        //产生爆炸
        booms.add(new Boom(myTank.getX() - 20 * size,
                myTank.getY() - 20 * size, myTank.isVertical()));
        //重置玩家坦克,
        myTank.setX(mWidth / 2 - 12 * size);
        myTank.setY(mHeight - 50 * size);

        //移去炮弹
        shells.remove(comShell);
        comShell = null;
    }


    public void judgeMyTankNew()   //判断游戏结束是否重新开始
    {
        if (!myTank.isLive()) {

            //暂时定为重新开始
            myTank = new PlayerTank(mWidth / 2 - 12 * size, mHeight - 50 * size,
                    1, comTanks, mWidth, mHeight, size);

        }
    }


    public void judgeMyTanktouched()   //判断玩家坦克是否碰到敌人坦克
    {
        if (myTank.judgeTankTouch()) {
            myTank.buckleBlood();   //产生爆炸等
            booms.add(new Boom(myTank.getX() - 2 * size,
                    myTank.getY() - 2 * size, myTank.isVertical()));
            //重置玩家坦克,
            myTank.setX(mWidth / 2 - 12 * size);
            myTank.setY(mHeight - 50 * size);

        }
    }


    //实现刷新页面的效果   主要判断功能都要放在这里 （是否从新开始，判断是都击中）
    @Override
    public void run() {
        int i = 0;
        while (true) {
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //第一次初始化 因为一定要经过 测量才能 获得高度和宽度
            if (!isFirst) {
//        //玩家子弹创建

                if (25 == i) {
                    if (null != myTank) {
                        myTank.shutShell();
                    }

                    i = 0;
                } else {
                    i++;
                }


                judgeMyTankNew();                                 //当命数用完是否重新开始游戏
                judgeTankKill(myTank, comTanks);                  //敌人是否击中
                judgeMyTankKill(myTank, comTanks);                //玩家坦克是都被击中
                judgeMyTanktouched();                             //玩家坦克是都被敌人坦克触碰到
            }


            postInvalidate();                                 //刷新页面
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return true;
    }
}
