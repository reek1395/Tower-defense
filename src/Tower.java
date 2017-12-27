import java.awt.*;

public class Tower extends Unit {

    boolean sellable;
    int damage;
    int range;
    int delay;
    int count;

    Tower ()
    {

    }
    Tower ( boolean s)
    {
        sellable = s;
    }

    Tower ( int a , int r, int d)
    {
        damage = a;
        range = r;
        delay = d;
        count = 0;
        sellable = true;
    }

    int getCount()
    {
        return count;
    }

    void increaseCount()
    {
        count++;
    }

    boolean attack(Monster m)
    {
        return true;

    }

    void drawAttack(int x, int y, int mx, int my, Graphics g)
    {

    }

    int getDelay()
    {
        return delay;
    }

    void draw(int x, int y, Graphics g)
    {

    }

    int getRange()
    {
        return range;
    }
}
