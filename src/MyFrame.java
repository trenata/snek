import Games.*;
import Score.*;
import Sounds.*;
import Views.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MyFrame extends JFrame implements ActionListener {
    private static final int screensize = 800;
    private static final int cellsize = 25;
    private Timer t = new Timer(10, this);
    private final JFileChooser filec;
    private Scores scores = new Scores();
    private Game g;

    private MyFrame() {
        setLayout(new CardLayout());
        setTitle("Snake");

        // --- MUSIC ---
        String musicpath = "Sounds/Eric Skiff - A Night Of Dizzy Spells.WAV";
        Music music = new Music();
        music.playMusic(musicpath);


        ArrayList<NameScore> arraysc;
        try {
            File file = new File("Scoreboard/scoreboard.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));

            // --- STREAM ---
            arraysc = (ArrayList<NameScore>) br.lines().map(str -> str.split(": ")).map(st -> new NameScore(st[0], Integer.parseInt(st[1])))
                    .collect(Collectors.toList());

            for (int i = 0; i < arraysc.size(); i++)
                scores.add(arraysc.get(i).getName(), arraysc.get(i).getScore());
            repaint();

        } catch (IOException e) {
            System.out.println(e);
        }

        CardLayout layout = new CardLayout();
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(layout);
        add(cardPanel);

        JPanel mainMenu = new JPanel();
        mainMenu.setLayout(null);
        MainMenu mm = new MainMenu(screensize);
        mainMenu.add(mm);
        cardPanel.add(mainMenu, "mainMenu");


        JPanel game1 = new JPanel();
        JPanel game2 = new JPanel();
        JPanel game3 = new JPanel();

        Game1 g1 = new Game1(screensize, cellsize, scores);
        game1.add(g1);
        cardPanel.add(game1, "game1");

        Game2 g2 = new Game2(screensize, cellsize, scores);
        game2.add(g2);
        cardPanel.add(game2, "game2");

        Game3 g3 = new Game3(screensize, cellsize + 5, scores);
        game3.add(g3);
        cardPanel.add(game3, "game3");

        mm.getEasyGame().addActionListener(e -> {
                    g = g1;
                    g1.beginning();
                    layout.show(cardPanel, "game1");
                }
        );

        mm.getMediumGame().addActionListener(e -> {
                    g = g2;
                    g2.beginning();
                    layout.show(cardPanel, "game2");
                }
        );

        mm.getHardGame().addActionListener(e -> {
                    g = g3;
                    g3.beginning();
                    layout.show(cardPanel, "game3");
                }
        );


        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(null);
        Scoreboard sb = new Scoreboard(scores, screensize);
        scorePanel.add(sb);
        cardPanel.add(scorePanel, "scores");

        mm.getScoreBoard().addActionListener(e -> layout.show(cardPanel, "scores"));


        JPanel howtoplayPanel = new JPanel();
        howtoplayPanel.setLayout(null);
        HowToPlay hp = new HowToPlay(screensize);
        howtoplayPanel.add(hp);
        cardPanel.add(howtoplayPanel, "howtoplay");

        mm.getHowToPlay().addActionListener(e -> layout.show(cardPanel, "howtoplay"));


        mm.getQuit().addActionListener(e -> System.exit(0));


        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_RIGHT && !g.isLeft()) {
                    g.setUp(false);
                    g.setDown(false);
                    g.setRight(true);
                }

                if (key == KeyEvent.VK_LEFT && !g.isRight()) {
                    g.setUp(false);
                    g.setDown(false);
                    g.setLeft(true);
                }

                if (key == KeyEvent.VK_DOWN && !g.isUp()) {
                    g.setRight(false);
                    g.setLeft(false);
                    g.setDown(true);
                }

                if (key == KeyEvent.VK_UP && !g.isDown()) {
                    g.setRight(false);
                    g.setLeft(false);
                    g.setUp(true);
                }

                if (key == KeyEvent.VK_SPACE) {
                    g.stop();
                    g.beginning();
                }

                if (key == KeyEvent.VK_ESCAPE) {
                    try {
                        g.stop();
                    } catch (NullPointerException er) {
                        System.out.println("No game");
                    }
                    layout.show(cardPanel, "mainMenu");
                }

                if (key == KeyEvent.VK_S) {
                    g.saveScore();
                }
            }
        });

        t.start();

        // --- FILE ---
        filec = new JFileChooser();

        sb.getSave().addActionListener(e -> {
            filec.setDialogTitle("Save file");

            int userSelection = filec.showSaveDialog(MyFrame.this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = filec.getSelectedFile();

                try {
                    BufferedWriter myWriter = new BufferedWriter(new FileWriter(fileToSave, true));

                    myWriter.write("Snek scoreboard ^-^");
                    myWriter.newLine();
                    myWriter.newLine();
                    for (int i = 0; i < scores.getLength(); i++) {
                        myWriter.append((i + 1) + ". " + scores.getBoard().get(i).getName() + ": " + scores.getBoard().get(i).getScore());
                        myWriter.newLine();
                    }

                    myWriter.close();
                } catch (IOException er) {
                    System.out.println("An error occurred.");
                    er.printStackTrace();
                }
            }
        });


        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(screensize + 10, screensize + 35);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public static void main(String[] args) {
        new MyFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        requestFocus();
    }

}
