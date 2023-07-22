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


public class Game2 extends JPanel implements Runnable, Game {
    private int screensize;
    private int cellsize;
    private Thread thread;
    private boolean running;
    private Random rand;
    private boolean gameover;
    private Scores scores;

    private BufferedImage gameoverscreen;
    private JLabel scoreLabel = new JLabel();
    private int score;

    private int x, y, length;
    private boolean right, left, up, down;

    private int ticks;
    private int w;
    private int xw;
    private int yw;

    private ArrayList<BodyPart> snake;
    private ArrayList<Apple> apples;
    private ArrayList<Wall> walls;
    private ArrayList<Integer> wallpoz;
    private int k;


    public Game2(final int screensize, final int cellsize, Scores scores) {
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

        ticks = 0;
        w = 0;
        xw = 0;
        yw = 0;

        score = 0;
        scoreLabel.setText("Score: 0");

        snake = new ArrayList<>();
        apples = new ArrayList<>();
        walls = new ArrayList<>();

        rand = new Random();

        // creates array with 1/ 2/ -1/ -2 based on how many blocks and where they need to appear when the walls start coming
        wallpoz = new ArrayList<>();

        k = 1;
        int q = 0;
        for (int j = 0; j < screensize / cellsize / 4; j++) {
            for (int i = 0; i < screensize / cellsize - q; i++) {
                wallpoz.add(-2);
            }
            for (int i = 0; i < screensize / cellsize - q - 1; i++) {
                wallpoz.add(1);
            }
            for (int i = 0; i < screensize / cellsize - q - 1; i++) {
                wallpoz.add(2);
            }
            for (int i = 0; i < screensize / cellsize - q - 2; i++) {
                wallpoz.add(-1);
            }
            q += 2;
        }

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
                Thread.sleep(10);
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
        // ------------ WALL --------------
        int beforeWall = 1000;
        if (w >= beforeWall) {
            if (w - beforeWall >= 2) {
                Wall wall = new Wall(xw, yw, cellsize);
                walls.add(wall);
                if (k < wallpoz.size()) {
                    if (wallpoz.get(k) == 1)
                        xw++;
                    if (wallpoz.get(k) == 2)
                        yw--;
                    if (wallpoz.get(k) == -1)
                        xw--;
                    if (wallpoz.get(k) == -2)
                        yw++;
                }

                k++;
                w = beforeWall;

                for (int i = 0; i < walls.size(); i++) {
                    if (x == walls.get(i).getX() && y == walls.get(i).getY()) {
                        gameOver();
                        stop();
                    }
                    for (int j = 0; j < apples.size(); j++) {
                        if (walls.get(i).getX() == apples.get(j).getX() && walls.get(i).getY() == apples.get(j).getY()) {
                            apples.remove(j);
                            j--;
                        }
                    }
                }
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
        if ((ticks > 5 && score < 2) || (ticks > 3 && score >= 2 && score < 5) || (ticks > 2 && score >= 5)) {
            if (right) x++;
            if (left) x--;
            if (up) y--;
            if (down) y++;

            ticks = 0;

            BodyPart b = new BodyPart(x, y, cellsize);
            snake.add(b);

            if (snake.size() > length) {
                snake.remove(0);
            }
        }

        ticks++;
        w++;
    }

    public void stop() {
        running = false;
        gameover = true;
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

            for (int i = 0; i < walls.size(); i++) {
                walls.get(i).draw(g);
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
