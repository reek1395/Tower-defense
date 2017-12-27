import java.awt.*;
import java.util.ArrayList;

public class Grid {

    Tower[][] grid;
    boolean[][] updated;

    boolean[][] attack;
    int[][] monsterLocX;
    int[][] monsterLocY;

    int[][] path;

    int startX, startY, endX, endY;
    int sizeX, sizeY;

    ArrayList<Monster> mobs;
    int waveCount;
    int nextMob;
    int currentHp;

    int lives;

    int testX, testY;


    int gold;
    float goldScaler;
    float goldPerKill;
    boolean nextRound;
    boolean wait;

    int temp, dir, tempIndex;
    Graphics G;

    public Grid (int sx, int sy, Graphics s)
    {
        G=s;

        lives = 20;
        gold = 40;
        goldScaler = 0.2f;
        goldPerKill = 2.0f;

        nextRound = false;
        wait = true;

        grid = new Tower[sy][sx];
        updated = new boolean[sy][sx];
        path = new int[sy][sx];
        monsterLocX = new int [sy][sx];
        monsterLocY = new int [sy][sx];
        attack = new boolean[sy][sx];

        mobs = new ArrayList<Monster>();

        currentHp = 120;

        sizeX = sx;
        sizeY = sy;

        startX = 0;
        startY = sy/2;

        endX = sx - 1;
        endY = sy/2;

        createGrid();

        //test();
        newWave(currentHp);

        update();

        //printGrid();

    }

    void ready()
    {
        wait = false;
        if(mobs.size()==0)
            nextRound = true;
    }

    void newWave(int x)
    {
        waveCount = 20;
        nextMob = 50;
        currentHp = x;
    }

    void test()
    {
        grid[6][14] = new Wall(false);
        grid[6][13] = new Wall(false);
        grid[6][12] = new Wall(false);
        grid[7][13] = new Wall(false);
        grid[8][14] = new Wall(false);
        grid[9][13] = new Wall(false);
    }

    boolean buildable(int x, int y)
    {
        //System.out.println(grid[y][x] instanceof Empty);
        return grid[y][x] instanceof Empty;
    }

    void addTower(Tower t, int x, int y)
    {
        grid[y][x] = t;
        update();
    }

    Tower getTower(int x, int y)
    {
        return grid[y][x];
    }

    void move(int delay)
    {
        if(wait){

            if(lives <= 0)
            {
                for(int i=0; i<mobs.size(); i+=0)
                {
                    mobs.remove(i);
                }
                lives = 20;
                gold = 40;
                goldScaler = 0.2f;
                goldPerKill = 2.0f;
                currentHp = 120;

                createGrid();
                newWave(currentHp);
            }
        }
        else if (waveCount > 0)
        {
            nextMob--;
            if(nextMob == 0)
            {
                nextMob = 50;
                waveCount--;
                mobs.add(new Monster((startX*50)+25, (startY*50)+25, 4, currentHp));
            }
        }else if(mobs.size() == 0 && nextRound)
        {
            nextRound = false;
            goldPerKill += goldPerKill * goldScaler;
            currentHp += currentHp*.50;
            //System.out.println(currentHp);
            newWave(currentHp);
        }

        for (int i =0; i<mobs.size(); i++)
        {
            if (mobs.get(i).move())
            {
                mobs.get(i).setDirection(getDirection(mobs.get(i)));
                if(mobs.get(i).getX() == (endX*50) +25 && mobs.get(i).getY() == (endY*50) +25 )
                {
                    mobs.remove(i);
                    lives--;
                    if (lives <= 0)
                    {
                        wait = true;
                        break;
                    }
                    i--;
                }
            }
        }

        for (int i = 0; i<sizeY; i++)
        {
            for (int z =0; z<sizeX; z++)
            {
                if (grid[i][z] instanceof Tower)
                {
                    grid[i][z].increaseCount();
                    if(grid[i][z].getCount() >= grid[i][z].getDelay())
                    {
                        for (int q = 0; q < mobs.size(); q++)
                        {
                            if (checkRange(z, i, grid[i][z].getRange(), mobs.get(q)) && grid[i][z].getCount() >= grid[i][z].getDelay())
                            {
                                attack[i][z] = true;
                                monsterLocX[i][z] = mobs.get(q).getX();
                                monsterLocY[i][z] = mobs.get(q).getY();
                                if (!grid[i][z].attack(mobs.get(q)))
                                {
                                    mobs.remove(q);
                                    q--;
                                    gold += (int)goldPerKill;
                                    break;
                                }


                            }
                        }
                    }
                }
            }
        }

        //draw(G);
    }

    boolean checkRange(int x, int y, int r,  Monster m)
    {
        // tower center x*50 +25, y*50 +25  r/2
        if(m.getX() > ((x*50)+25) - r/2  && m.getX() < ((x*50)+25) + r/2)
            if (m.getY() > ((y*50)+25) - r/2 && m.getY() < ((y*50)+25) + r/2)
                return true;

        return false;

    }

    int getDirection(Monster m)
    {
        testX = m.getX();
        testY = m.getY();

        testX -= 25;
        testY -= 25;

        testX /= 50;
        testY /= 50;

        return findLowestNeighbor(testX, testY);

    }

    int findLowestNeighbor(int x, int y)
    {
        //test y--

        temp = 1000;
        dir = 0;

        if(y-1 > 0)
        {
            if(grid[y-1][x] instanceof Empty)
            {
                temp = path[y-1][x];
                dir = 1;
            }

        }
        //test x--
        if (x-1 >0)
        {
            if(grid[y][x-1] instanceof Empty && path[y][x-1] < temp)
            {
                temp = path[y][x-1];
                dir = 2;
            }
        }
        //test y++
        if (y+1 <sizeY)
        {
            if(grid[y+1][x] instanceof Empty && path[y+1][x] < temp)
            {
                temp = path[y+1][x];
                dir = 3;
            }
        }
        //test x++
        if (x+1 < sizeX)
        {
            if(grid[y][x+1] instanceof Empty && path[y][x+1] < temp)
            {
                temp = path[y][x+1];
                dir = 4;
            }
        }

        //return
        return dir;
    }

    void update()
    {
        for(int i = 0; i<sizeY; i++)
        {
            for (int z = 0; z<sizeX; z++)
            {
                updated[i][z] = false;
            }
        }

        path[endY][endX] = 1;
        updated[endY][endX] = true;

        update(endX-1, endY, 2);
    }

    void update(int x, int y, int v)
    {

        path[y][x] = v;
        updated[y][x] = true;

        if(y-1 >= 0)
        {
            if (!updated[y - 1][x] || path[y - 1][x] > v + 1)
            {
                if(grid[y-1][x] instanceof Empty)
                {
                    update(x, y - 1, v + 1);
                }else
                {
                    path[y-1][x] = 10000;
                }

            }
        }

        if(y+1 < sizeY)
        {
            if(!updated[y +1][x] || path[y + 1][x] > v + 1)
            {
                if(grid[y+1][x] instanceof Empty)
                {
                    update(x, y + 1, v + 1);
                }else
                {
                    path[y+1][x] = 10000;
                }
            }
        }

        if(x-1 >= 0)
        {
            if(!updated[y][x-1] || path[y][x-1] > v + 1)
            {
                if(grid[y][x-1] instanceof Empty)
                {
                    update(x-1, y, v+1);
                }else
                {
                    path[y][x-1] = 10000;
                }
            }
        }

        if(x+1 <sizeX)
        {
            if(!updated[y][x+1] || path[y][x+1] > v + 1)
            {
                if(grid[y][x+1] instanceof Empty)
                {
                    update(x+1, y, v+1);
                }else
                {
                    path[y][x+1] = 10000;
                }
            }
        }


    }

    void createGrid()
    {
        for (int i = 0; i<sizeY; i++)
        {
            for (int z = 0; z<sizeX; z++)
            {
                if (i == 0 || i == sizeY-1)
                {
                    grid[i][z] = new Wall(false);
                }else if (z == 0 || z == sizeX-1)
                {
                    if (i == startY)
                    {
                        grid[i][z] = new Empty();
                    }
                    else
                    {
                        grid[i][z] = new Wall(false);
                    }
                }else
                {
                    grid[i][z] = new Empty();
                }

                attack[i][z] = false;
                monsterLocX[i][z] = 0;
                monsterLocY[i][z] = 0;
            }
        }
    }

    void draw(Graphics g)
    {
        g.setColor(Color.black);
        g.fillRect(0,0,sizeX*50, sizeY*50);



        g.setColor(Color.green);
        g.fillRect((startX*50)+1, (startY*50)+1, 48, 48);
        g.setColor(Color.red);
        g.fillRect((endX*50)+1, (endY*50)+1, 48, 48);

        drawGrid(g);

        drawEntity(g);

        g.setColor(Color.black);
        g.fillRect((sizeX*50) -145, 10, 70,35);
        g.setColor(Color.yellow);
        g.drawString("$ "+gold, (sizeX*50)-140, 25);
        g.setColor(Color.red);
        g.drawString("Lives: " +lives, (sizeX*50)-140, 39);
        //System.out.println(sizeX);
    }

    void setGold(int x)
    {
        gold = x;
    }

    int getGold()
    {
        return gold;
    }

    void drawEntity(Graphics g)
    {

        for(int i=0; i<sizeY; i++)
        {
            for(int z=0; z<sizeX; z++)
            {
                if(grid[i][z] instanceof Tower)
                {
                    grid[i][z].draw(z,i,g);
                }

                if(attack[i][z])
                {
                    grid[i][z].drawAttack(z, i, monsterLocX[i][z], monsterLocY[i][z], g);
                    attack[i][z] = false;
                }

            }
        }

        for(int i =0; i<mobs.size(); i++)
        {
            g.setColor(Color.blue);
            g.fillRect(mobs.get(i).getX()-5, mobs.get(i).getY()-5, 10, 10);
            mobs.get(i).drawHealth(g);
        }

    }

    void drawGrid(Graphics g)
    {
        g.setColor(Color.white);
        for (int i = 0; i < sizeY; i++)
        {
            for (int z = 0; z < sizeX; z++)
            {
                g.drawRect(z*50,i*50,50,50);
            }
        }
    }


    //temp
    void printGrid()
    {
        for (int i=0; i<sizeY; i++)
        {
            for (int z=0; z<sizeX; z++)
            {
                if (grid[i][z] instanceof Wall)
                    System.out.print("  x");
                else if(grid[i][z] instanceof Empty)
                {
                    if(path[i][z] < 10)
                    {
                        System.out.print("  " + path[i][z]);
                    }
                    else
                    {
                        System.out.print(" " + path[i][z]);
                    }
                }
            }
            System.out.println();
        }
    }
}
