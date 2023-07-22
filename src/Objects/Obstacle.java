package Objects;

import java.awt.*;

public class Obstacle {
    private int x, y, cellsize, size;

    public Obstacle(int x, int y, int cellsize, int size) {
        this.x = x;
        this.y = y;
        this.cellsize = cellsize;
        this.size = size;
    }

    public void draw(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x * cellsize, y * cellsize, size, size);
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
