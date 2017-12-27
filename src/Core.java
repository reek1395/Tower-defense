import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.io.*;

public class Core extends JFrame {

    Grid map;

    Timer t;
    ActionListener updater;

    MouseAdapter m;
    KeyAdapter k;

    public JPanel panel;
    public Graphics s;

    boolean paid;

    final int delay = 20;

    //int gold;

    final int arrowCost = 25;
    final int wallCost = 10;
    final float sellPer = 0.7f;

    int sizeX, sizeY;

    //Wall menuWall;
    //Arrow menuArrow;


    int activeTower;
    // 0 empty,  1 wall, 2 arrow, 3....


    // DELETE ME
    //Random rand;

    public Core(int sx, int sy)
    {
        //gold = 40;
        paid = false;
        activeTower = 1;
        sizeX = sx;
        sizeY = sy;


        //menuWall = new Wall();
        //menuArrow = new Arrow();

        //rand = new Random();

        panel = new JPanel(){
            protected void paintComponent(Graphics g)
            {
                s=g;
                map.draw(g);

                drawMenu(g);


                /*if(!paid)
                {
                    g.setColor(Color.red);
                    g.setFont(new Font("TimesRoman", Font.PLAIN, 18));
                    g.drawString("Press G to spend the money" , (sx*25) -100, 30);
                }*/

            }
        };

        map = new Grid(sx, sy, s);



        updater = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                update();
            }
        };

        t = new Timer(delay, updater);
        t.start();

        m = new MouseAdapter()
        {
            public void mouseReleased(MouseEvent e){

                if(e.getY()-31 < (sy*50))
                {
                    switch (activeTower) {
                        case 0:
                            if(map.getTower((e.getX() - 7) / 50, (e.getY() - 31) / 50) instanceof Arrow)
                            {
                                map.setGold(map.getGold() + (int)(arrowCost * sellPer));
                                map.addTower(new Empty(), (e.getX() - 7) / 50, (e.getY() - 31) / 50);
                            }
                            else if(map.getTower((e.getX() - 7) / 50, (e.getY() - 31) / 50) instanceof Wall && map.getTower((e.getX() - 7) / 50, (e.getY() - 31) / 50).getSellable())
                            {
                                //System.out.println( map.getTower((e.getX() - 7) / 50, (e.getY() - 31) / 50).getSellable());
                                map.setGold(map.getGold() + (int)(wallCost * sellPer));
                                map.addTower(new Empty(), (e.getX() - 7) / 50, (e.getY() - 31) / 50);
                            }
                            /*else if()
                            {
                                map.addTower(new Empty(), (e.getX() - 7) / 50, (e.getY() - 31) / 50);
                            }*/
                            //map.addTower(new Empty(), (e.getX() - 7) / 50, (e.getY() - 31) / 50);
                            break;

                        case 1:
                            if (map.buildable((e.getX() - 7) / 50, (e.getY() - 31) / 50) && map.getGold() >= wallCost)
                            {
                                map.setGold(map.getGold() - wallCost);
                                map.addTower(new Wall(true), (e.getX() - 7) / 50, (e.getY() - 31) / 50);
                                //System.out.println( map.getTower((e.getX() - 7) / 50, (e.getY() - 31) / 50).getSellable());
                            }
                            break;
                        case 2:
                            if (map.buildable((e.getX() - 7) / 50, (e.getY() - 31) / 50) && map.getGold() >= arrowCost)
                            {
                                map.setGold(map.getGold() - arrowCost);
                                map.addTower(new Arrow(20, 200, 10, paid), (e.getX() - 7) / 50, (e.getY() - 31) / 50);
                            }
                            break;
                    }
                }
                //map.addTower(new Arrow(20,200, 10), (e.getX() - 7)/50, (e.getY() - 31)/50);
                //System.out.println(e.getX() +"  "+ e.getY());
                if(e.getY() >= 705 && e.getY() <= 745)
                {
                    if(e.getX() >= 28 && e.getX() <= 66)
                        activeTower = 0;
                    if(e.getX() >= 78 && e.getX() <= 116)
                        activeTower = 1;
                    if(e.getX() >= 128 && e.getX() <=166)
                        activeTower = 2;
                    if(e.getX() >= 838 && e.getX() <= 908)
                        map.ready();

                }

            }

        };

        k = new KeyAdapter()
        {
            public void keyPressed(KeyEvent e){

            }

            public void keyReleased(KeyEvent e){

                switch( e.getKeyCode()){

                    case KeyEvent.VK_1:
                        activeTower = 1;
                        break;
                    case KeyEvent.VK_2:
                        activeTower = 2;
                        break;
                    case KeyEvent.VK_0:
                        activeTower = 0;
                        break;
                    case KeyEvent.VK_G:
                        paid = true;
                        map.setGold(map.getGold()+1000);
                        break;
                }
            }

            public void keyTyped(KeyEvent e){

            }

        };
        

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addMouseListener(m);
        addKeyListener(k);
        setSize((sx*50)+17,(sy*50)+140); //y +40
        panel.setSize(sx*50,sy*50);
        setLocation(50,50);
        add(panel);
        panel.setVisible(true);
        setVisible(true);

        //map.draw(s);

    }

    void update()
    {
        map.move(delay);
        repaint();

    }

    void drawMenu(Graphics g)
    {
        g.setColor(Color.darkGray);
        g.fillRect(0, (sizeY*50), sizeX*50, 100 );

        //menuWall.draw( 2, sizeY + 1, g);

        //background squares
        g.setColor(Color.black);
        if(activeTower == 1)
            g.setColor(Color.cyan);

        g.fillRect(70, (sizeY*50)+25,40,40);

        g.setColor(Color.black);
        if(activeTower == 2)
            g.setColor(Color.cyan);

        g.fillRect(120, (sizeY*50)+25, 40, 40);

        g.setColor(Color.black);
        if(activeTower == 0)
            g.setColor(Color.cyan);
            g.fillRect(20, (sizeY*50)+25, 40, 40);


        //Wall
        g.setColor(Color.gray);
        g.fillRect( 75, (sizeY*50) + 30, 30, 30);

        //Arrow
        g.setColor(Color.orange);
        g.fillOval( 125, (sizeY*50) + 30, 30 , 30);

        g.setColor(Color.yellow);
        g.drawString("$ "+wallCost, 75, (sizeY*50)+80 );
        g.drawString("$ " + arrowCost,125, (sizeY*50)+80);
        g.drawString("Sell" , 25, (sizeY*50)+80);

        g.drawString("1", 85, (sizeY*50)+20);
        g.drawString("0", 35, (sizeY*50)+20);
        g.drawString("2", 135, (sizeY*50)+20);


        //start button
        g.setColor(Color.black);
        g.fillRect((sizeX*50)-120, (sizeY*50)+25,70,40);
        g.setColor(Color.white);
        g.drawRect((sizeX*50)-120, (sizeY*50)+25,70,40);
        g.setColor(Color.yellow);
        g.drawString("Start round",(sizeX*50)-115, (sizeY*50)+50);

    }


}
