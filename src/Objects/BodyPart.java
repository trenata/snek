package Objects;

import java.awt.*;

public class BodyPart {
    private int x, y, size;

    public BodyPart(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x * size + 2, y * size + 2, size - 4, size - 4);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
