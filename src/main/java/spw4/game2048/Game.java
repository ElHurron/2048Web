package spw4.game2048;

public interface Game {
    void initialize();
    void move(Direction direction);
    int getMoves();
    int getScore();
    int getValueAt(int row, int col);
    boolean isOver();
    boolean isWon();
}
