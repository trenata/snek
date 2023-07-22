package Objects;

import java.awt.*;

public class Apple {
    private int x, y, size;

    public Apple(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public  void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x * size, y * size, size, size);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

}
