package spw4.game2048;

import java.util.*;

public class GameImpl implements Game {
    private final int[][] board;

    public GameImpl() {
        board = new int[4][];
        for (int i = 0; i < 4; i++) {
            board[i] = new int[4];
        }
    }

    public int getMoves() { return 0; }
    public int getScore() { return 0; }
    public int getValueAt(int row, int col) { return board[row][col]; }
    public boolean isOver() { return false; }
    public boolean isWon() { return false; }

    public void initialize() {
        for (int i = 0; i < 4; i++) {
            Arrays.fill(board[i], 0);
        }

        Random random = new Random();
        board[random.nextInt(4)][random.nextInt(4)] = 2;
    }

    public void move(Direction direction) {
        initialize();
    }
}
