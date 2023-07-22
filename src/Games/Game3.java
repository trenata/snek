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


public class Game3 extends JPanel implements Runnable, Game {
    private int screensize;
    private int cellsize;
    private Thread thread;
    private boolean running;
    private Random rand;
    private Scores scores;
    private boolean gameover;

    private BufferedImage gameoverscreen;
    private JLabel scoreLabel = new JLabel();
    private int score;

    private int x, y, length;
    private boolean right, left, up, down;

    private ArrayList<BodyPart> snake;
    private ArrayList<Apple> apples;
    private ArrayList<Obstacle> obstacles;


    public Game3(final int screensize, final int cellsize, Scores scores) {
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

        score = 0;
        scoreLabel.setText("Score: 0");

        snake = new ArrayList<>();
        apples = new ArrayList<>();
        obstacles = new ArrayList<>();

        rand = new Random();

        for (int i = 0; i < rand.nextInt(3) + 6; i++) {
            Obstacle wp = new Obstacle(rand.nextInt(screensize / cellsize), rand.nextInt(screensize / cellsize), cellsize, (rand.nextInt(4) + 1) * cellsize);
            obstacles.add(wp);
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
                Thread.sleep(75);
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
                length += 5;
                apples.remove(i);
                i--;

                score++;
                scoreLabel.setText("Score: " + score);

            }
        }
        // ------------ WALL --------------
        for (int i = 0; i < obstacles.size(); i++) {
            if (new Rectangle(x * cellsize, y * cellsize, cellsize, cellsize).intersects(new Rectangle(obstacles.get(i).getX() * cellsize, obstacles.get(i).getY() * cellsize, obstacles.get(i).getSize(), obstacles.get(i).getSize()))) {
                gameOver();
                stop();
            }
            for (int j = 0; j < apples.size(); j++) {
                if (new Rectangle(obstacles.get(i).getX() * cellsize, obstacles.get(i).getY() * cellsize, obstacles.get(i).getSize(), obstacles.get(i).getSize()).intersects(new Rectangle(apples.get(j).getX() * cellsize, apples.get(j).getY() * cellsize, apples.get(j).getSize(), apples.get(j).getSize()))) {
                    apples.remove(j);
                    j--;
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
        if (right) x++;
        if (left) x--;
        if (up) y--;
        if (down) y++;

        BodyPart b = new BodyPart(x, y, cellsize);
        snake.add(b);

        if (snake.size() > length) {
            snake.remove(0);
        }
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

            for (int i = 0; i < obstacles.size(); i++) {
                obstacles.get(i).draw(g);
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
