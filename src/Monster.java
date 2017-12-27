import java.awt.*;

public class Monster extends Unit {

    private int x, y;
    private int direction;
    public int health;
    int maxHealth;
    boolean alive;
    float percent;
    // 1 y-- ,2 x-- ,3 y++, 4 x++

    int steps;

    final int speed = 1;

    Monster(int X, int Y, int d, int h)
    {
        x=X;
        y=Y;
        direction = d;
        health = h;
        maxHealth = h;
        steps = 50;
        alive = true;
    }

    void setDirection(int d)
    {
        direction = d;
        steps = 50;
    }

    boolean takeDamage(int d)
    {
        health -= d;
        return health > 0;
    }

    boolean move()
    {
        steps--;

        switch (direction){

            case 0:
                break;
            case 1: y--;
            break;
            case 2: x--;
            break;
            case 3: y++;
            break;
            case 4: x++;
            break;

        }

        return steps == 0;

    }

    int getX()
    {
        return x;
    }

    int getY()
    {
        return y;
    }

    void drawHealth(Graphics g)
    {
        percent = ((float)health/(float)maxHealth);
        //System.out.println(percent);
        //System.out.println("Health: " + health + "   Max Health: " + maxHealth);
        g.setColor(Color.gray);
        g.drawRect(x-10,y-15, 20, 5);

        g.setColor(Color.red);

        g.fillRect(x-10, y-15, (int)(20*percent), 5);


    }


}
