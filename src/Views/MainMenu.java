package Views;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JPanel {
    private JButton easyGame;
    private JButton mediumGame;
    private JButton hardGame;
    private JButton scoreBoard;
    private JButton howToPlay;
    private JButton quit;

    private BufferedImage background;

    public MainMenu(int screensize) {
        Icon easy = new ImageIcon("Pictures/easy.png");
        Icon medium = new ImageIcon("Pictures/medium.png");
        Icon hard = new ImageIcon("Pictures/hard.png");
        Icon score = new ImageIcon("Pictures/scoreboard.png");
        Icon howto = new ImageIcon("Pictures/how to play.png");
        Icon quiti = new ImageIcon("Pictures/quit.png");

        easyGame = new JButton("Start easy game");
        easyGame.setIcon(easy);
        easyGame.setBounds(screensize / 2 - 100, screensize / 4 - 20, 220, 100);
        easyGame.setContentAreaFilled(false);
        easyGame.setBorderPainted(false);

        mediumGame = new JButton("Start medium game");
        mediumGame.setIcon(medium);
        mediumGame.setBounds(screensize / 2 - 100, 3 * screensize / 8 - 25, 220, 100);
        mediumGame.setContentAreaFilled(false);
        mediumGame.setBorderPainted(false);

        hardGame = new JButton("Start hard game");
        hardGame.setIcon(hard);
        hardGame.setBounds(screensize / 2 - 100, screensize / 2 - 30, 220, 100);
        hardGame.setContentAreaFilled(false);
        hardGame.setBorderPainted(false);

        scoreBoard = new JButton("Views.Scoreboard");
        scoreBoard.setIcon(score);
        scoreBoard.setBounds(screensize / 2 - 100, 5 * screensize / 8 - 35, 220, 100);
        scoreBoard.setContentAreaFilled(false);
        scoreBoard.setBorderPainted(false);

        howToPlay = new JButton("How to Play");
        howToPlay.setIcon(howto);
        howToPlay.setBounds(screensize / 2 - 100, 3 * screensize / 4 - 40, 220, 100);
        howToPlay.setContentAreaFilled(false);
        howToPlay.setBorderPainted(false);

        quit = new JButton("Quit");
        quit.setIcon(quiti);
        quit.setBounds(screensize / 2 - 100, 7 * screensize / 8 - 45, 220, 100);
        quit.setContentAreaFilled(false);
        quit.setBorderPainted(false);

        add(easyGame, BorderLayout.CENTER);
        add(mediumGame, BorderLayout.CENTER);
        add(hardGame, BorderLayout.CENTER);
        add(scoreBoard, BorderLayout.CENTER);
        add(howToPlay, BorderLayout.CENTER);
        add(quit, BorderLayout.CENTER);


        try {
            background = ImageIO.read((new File("Pictures/main menu background.png")));
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

    public JButton getEasyGame() {
        return easyGame;
    }

    public JButton getMediumGame() {
        return mediumGame;
    }

    public JButton getHardGame() {
        return hardGame;
    }

    public JButton getScoreBoard() {
        return scoreBoard;
    }

    public JButton getHowToPlay() {
        return howToPlay;
    }

    public JButton getQuit() {
        return quit;
    }
}
