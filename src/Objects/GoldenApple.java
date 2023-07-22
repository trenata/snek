package Objects;

import java.awt.*;

public class GoldenApple {
    private int x, y, size;

    public GoldenApple(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(x * size, y * size, size, size);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
