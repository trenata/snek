package Views;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HowToPlay extends JPanel {

    private BufferedImage background;

    public HowToPlay(int screensize) {

        try {
            background = ImageIO.read((new File("Pictures/how to play background.png")));
        } catch (IOException e) {
            System.out.println(e);
        }

        setLayout(null);
        setBounds(0, 0, screensize, screensize);
        setVisible(true);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
    }
}
