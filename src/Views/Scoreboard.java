package Views;

import Score.Scores;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Scoreboard extends JPanel {
    private int screensize;
    private Scores scores;
    private JButton save;
    private BufferedImage background;

    public Scoreboard(Scores scores, int screensize) {
        this.scores = scores;
        this.screensize = screensize;

        setLayout(null);
        setBounds(0, 0, screensize, screensize);

        try {
            background = ImageIO.read((new File("Pictures/scoreboard background.png")));
        } catch (IOException e) {
            System.out.println(e);
        }

        save = new JButton("Save");
        Icon s = new ImageIcon("Pictures/save.png");
        save.setIcon(s);
        save.setBounds(screensize / 2 - 100, screensize - 250, 200, 200);
        save.setContentAreaFilled(false);
        save.setBorderPainted(false);
        add(save);

        setOpaque(true);
        setVisible(true);
    }

    public JButton getSave() {
        return save;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, 0, 0, null);

        g.setFont(new Font("Times New Roman", Font.BOLD, 35));
        g.setColor(Color.WHITE);

        int newline = g.getFont().getSize() + 30;
        int y = 200;

        for (int i = 0; i < scores.getLength(); i++)
            g.drawString((i + 1) + ". " + scores.getBoard().get(i).getName() + " - " + scores.getBoard().get(i).getScore(), screensize / 3 + 50, y += newline);
    }

}
