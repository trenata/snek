package Games;

public interface Game {

    void beginning();

    void start();

    void run();

    void tick();

    void stop();

    void gameOver();

    void saveScore();

    void setRight(boolean right);

    void setLeft(boolean left);

    void setUp(boolean up);

    void setDown(boolean down);

    boolean isRight();

    boolean isLeft();

    boolean isUp();

    boolean isDown();

}
