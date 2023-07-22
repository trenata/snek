package Games;

import Objects.*;
import Score.Scores;
import Sounds.Sound;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Game1 extends JPanel implements Runnable, Game {
    private int screensize;
    private int cellsize;
    private Scores scores;
    private Thread thread;
    private boolean running;
    private Random rand;
    private boolean gameover;

    private BufferedImage gameoverscreen;
    private JLabel scoreLabel = new JLabel();
    private int score;

    private int x, y, length;
    private boolean right, left, up, down;

    private int ga;
    private int v;

    private ArrayList<BodyPart> snake;
    private ArrayList<Apple> apples;
    private ArrayList<GoldenApple> goldenApples;
    private ArrayList<Venom> venoms;


    public Game1(final int screensize, final int cellsize, Scores scores) {
        this.screensize = screensize;
        this.cellsize = cellsize;
        this.scores = scores;

        setFocusable(true);
        requestFocusInWindow();
        setPreferredSize(new Dimension(screensize, screensize));

        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        scoreLabel.setOpaque(true);
        scoreLabel.setVisible(true);
        add(scoreLabel);
    }

    public void beginning() {
        gameover = false;

        right = true;
        left = false;
        up = false;
        down = false;

        x = 5;
        y = 5;
        length = 5;

        ga = 0;
        v = 0;

        score = 0;
        scoreLabel.setText("Score: 0");

        snake = new ArrayList<>();
        apples = new ArrayList<>();
        goldenApples = new ArrayList<>();
        venoms = new ArrayList<>();

        rand = new Random();

        start();

    }

    public void start() {
        running = true;
        thread = new Thread(this, "Loop");
        thread.start();
    }

    public void run() {
        while (running) {
            tick();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            repaint();
        }
    }

    public void tick() {
        // ----------- SNAKE -----------
        if (snake.size() == 0) {
            BodyPart b = new BodyPart(x, y, cellsize);
            snake.add(b);
        }
        // ------------ APPLE ----------------
        if (apples.size() == 0) {
            int xa = rand.nextInt(screensize / cellsize - 1);
            int ya = rand.nextInt(screensize / cellsize - 1);

            Apple apple = new Apple(xa, ya, cellsize);
            apples.add(apple);
        }

        for (int i = 0; i < apples.size(); i++) {
            if (x == apples.get(i).getX() && y == apples.get(i).getY()) {
                length++;
                apples.remove(i);
                i--;

                score++;
                scoreLabel.setText("Score: " + score);
            }
        }
        // ------------ GOLDEN APPLE -----------------
        if (goldenApples.size() == 0 && ga >= 100) { // 1400
            int xg = rand.nextInt(screensize / cellsize - 1);
            int yg = rand.nextInt(screensize / cellsize - 1);

            GoldenApple goldenApple = new GoldenApple(xg, yg, cellsize);
            goldenApples.add(goldenApple);
        }
        if (goldenApples.size() != 0 && ga >= 150) { // 1800
            goldenApples.remove(0);
            ga = 0;
        }

        for (int i = 0; i < goldenApples.size(); i++) {
            if (x == goldenApples.get(i).getX() && y == goldenApples.get(i).getY()) {
                for (int j = 0; j < 5; j++) {
                    if (snake.size() > 5) {
                        snake.remove(j);
                        length--;
                    }
                }
                goldenApples.remove(i);
                i--;
            }
        }
        // ------------ VENOM --------------
        if (venoms.size() == 0 && v >= 100) { // 1400
            int xv = rand.nextInt(screensize / cellsize - 1);
            int yv = rand.nextInt(screensize / cellsize - 1);

            Venom venom = new Venom(xv, yv, cellsize);
            venoms.add(venom);

            v = 0;
        }
        if (venoms.size() != 0 && v >= 200) { // 1700
            venoms.remove(0);
            v = 0;
        }

        for (int i = 0; i < venoms.size(); i++) {
            if (x == venoms.get(i).getX() && y == venoms.get(i).getY()) {
                for (int j = 0; j < 5; j++) {
                    if (snake.size() > 5) {
                        snake.remove(j);
                        length--;
                    }
                }
                if (score - 5 > 0)
                    score -= 5;
                else
                    score = 0;

                scoreLabel.setText("Score: " + score);
                venoms.remove(i);
                i--;
            }
        }
        // --------- EATS ITSELF ------------
        for (int i = 0; i < snake.size(); i++) {
            if (x == snake.get(i).getX() && y == snake.get(i).getY()) {
                if (i != snake.size() - 1) {
                    gameOver();
                    stop();
                }
            }
        }
        // --------- OUT OF SCREEN --------------
        if (x < 0 || x > screensize / cellsize - 1 || y < 0 || y > screensize / cellsize - 1) {
            gameOver();
            stop();
        }

        // ------------ MOVE ---------------
        if (right) x++;
        if (left) x--;
        if (up) y--;
        if (down) y++;

        BodyPart b = new BodyPart(x, y, cellsize);
        snake.add(b);

        if (snake.size() > length) {
            snake.remove(0);
        }

        ga += rand.nextInt(1) + 1;
        v += rand.nextInt(2) + 1;
    }

    public void stop() {
        gameover = true;
        running = false;
    }

    public void gameOver() {
        try {
            gameoverscreen = ImageIO.read((new File("Pictures/game over.png")));
        } catch (IOException e) {
            System.out.println(e);
        }
        repaint();

        String gameoverpath = "Sounds/game over.wav";
        Sound gameover = new Sound();
        gameover.playSound(gameoverpath);
    }

    public void saveScore() {
        String name = JOptionPane.showInputDialog("Enter your name");
        scores.add(name, score);

        try {

            BufferedWriter myWriter = new BufferedWriter(new FileWriter("Scoreboard/scoreboard.txt"));
            myWriter.write("");
            myWriter = new BufferedWriter(new FileWriter("Scoreboard/scoreboard.txt", true));

            for (int i = 0; i < scores.getLength(); i++) {
                myWriter.append(scores.getBoard().get(i).getName() + ": " + scores.getBoard().get(i).getScore());
                myWriter.newLine();
            }
            myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameover) {
            g.drawImage(gameoverscreen, 0, 0, null);
        } else {
            setBackground(Color.BLACK);
            g.setColor(Color.DARK_GRAY);

            for (int i = 0; i < screensize / cellsize; i++) {
                g.drawLine(i * cellsize, 0, i * cellsize, screensize);
                g.drawLine(0, i * cellsize, screensize, i * cellsize);
            }

            for (int i = 0; i < snake.size(); i++) {
                snake.get(i).draw(g);
            }

            for (int i = 0; i < apples.size(); i++) {
                apples.get(i).draw(g);
            }

            for (int i = 0; i < goldenApples.size(); i++) {
                goldenApples.get(i).draw(g);
            }

            for (int i = 0; i < venoms.size(); i++) {
                venoms.get(i).draw(g);
            }
        }
    }


    public void setRight(boolean right) {
        this.right = right;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

}
