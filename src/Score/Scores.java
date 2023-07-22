package Score;

import java.util.ArrayList;
import java.util.Comparator;

public class Scores {

    private ArrayList<NameScore> board;

    public Scores() {
        board = new ArrayList<>();
    }

    public void add(String name, int score) {
        board.add(new NameScore(name, score));

        board.sort(Comparator.comparing(NameScore::getScore).reversed());

        if(board.size() > 5) {
            board.remove(5);
        }
    }

    public ArrayList<NameScore> getBoard() {
        return board;
    }

    public int getLength() {
        return board.size();
    }
}
