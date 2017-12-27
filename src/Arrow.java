import java.awt.*;

public class Arrow extends Tower{


    int tx, ty;
    boolean iPaidForThis;
    int[] xh;
    int[] yh;
    Arrow ( int a , int r, int d, boolean b)
    {
        iPaidForThis = b;
        count = 0;
        delay = d;
        damage = a;
        range = r;
        sellable = true;

    }

    Arrow(){

    }

    boolean attack(Monster m)
    {
        count = 0;
        return m.takeDamage(damage);
    }

    int getCount()
    {
        return count;
    }

    void increaseCount()
    {
        count++;
    }

    int getDelay()
    {
        return delay;
    }

    void draw(int x, int y, Graphics g)
    {
        g.setColor(Color.orange);
        g.fillOval((x*50)+10, (y*50)+10, 30, 30);

        tx = (x*50) +25;
        ty = (y*50) +25;
        if(iPaidForThis)
        {
            xh =new int[]{ tx-20, tx, tx+20};
            yh =new int[]{ ty-4, ty-24, ty-4};

            g.setColor(Color.magenta);
            Polygon p = new Polygon(xh, yh, 3);
            g.fillPolygon(p);

            g.setColor(Color.black);

            g.fillOval(tx-6 ,ty-2 , 3,3);
            g.fillOval(tx+4 ,ty-2 , 3,3);

            g.drawLine(tx-7, ty+6, tx-5, ty+8);
            g.drawLine(tx-5, ty+8, tx, ty+9);
            g.drawLine(tx+5, ty+8, tx, ty+9);
            g.drawLine(tx+7, ty+6, tx+5,ty+8);

        }



        //g.setColor(Color.red);
        //g.drawOval(((x*50)+25)-(range/2), ((y*50)+25)-(range/2), range, range);
        //g.drawRect(((x*50)+25)-(range/2), ((y*50)+25)-(range/2), range, range);
    }

    void drawAttack(int x, int y, int mx, int my, Graphics g)
    {

        g.setColor(Color.pink);
        g.drawLine((x*50)+25, (y*50)+25, mx, my);

    }




}
