import java.awt.*;

public class Wall extends Tower {

    boolean sellable;

    public Wall(boolean s)
    {
        sellable = s;
    }
    Wall(){

    }

    void draw(int x, int y, Graphics g)
    {
        g.setColor(Color.gray);
        g.fillRect((x*50)+1, (y*50) + 1, 48, 48);
    }

    boolean getSellable()
    {
        return sellable;
    }
}
