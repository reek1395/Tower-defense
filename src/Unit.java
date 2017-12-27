import java.awt.*;

public class Unit {

    boolean sellable;
    int damage;
    int range;
    int delay;
    int count;

    public Unit()
    {

    }
    int getCount()
    {
        return count;
    }

    void increaseCount()
    {
        count++;
    }

    void draw(int x, int y, Graphics g)
    {

    }

    int getRange()
    {
        return 1;
    }

    boolean attack(Monster m)
    {
        return true;

    }

    int getDelay()
    {
        return delay;
    }

    void drawAttack(int x, int y, int mx, int my, Graphics g)
    {

    }

    boolean getSellable()
    {
        return sellable;
    }
}
