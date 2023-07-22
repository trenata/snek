package Objects;

import java.awt.*;

public class Venom {
    private int x, y, size;

    public Venom(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(17, 77, 19));
        g.fillRect(x * size, y * size, size, size);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
