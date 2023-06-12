package spw4.game2048;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class GameImpl implements Game {
    private static final int GAME_BOARD_SIZE = 4;

    private static final int TILE_2048 = 2_048;
    private static final int EMPTY_TILE = 0;

    public static Random randomTileSelector;
    public static Random randomPositionSelector;

    private int[][] board;
    private int moves = 0;
    private int score = 0;

    public GameImpl() {
        randomPositionSelector = new Random();
        randomTileSelector = new Random();
    }

    public int getMoves() {
        checkInitialized();

        return moves;
    }

    public int getScore() {
        checkInitialized();

        return score;
    }

    private void checkInitialized() {
        if(board == null) {
            throw new IllegalStateException("Game is not initialized!");
        }
    }

    public int getValueAt(int row, int col) {
        checkInitialized();

        boolean rowInRange = GameImpl.inRange(row);
        boolean columnInRange = GameImpl.inRange(col);
        if(!rowInRange || !columnInRange) {
            throw new IllegalArgumentException(
                    String.format("%s value %d must be >= 0 and < %d",
                            rowInRange ? "Row" : "Column",
                            row, GAME_BOARD_SIZE));
        }

        return board[row][col];
    }

    private static boolean inRange(int value) {
        return value >= 0 && value < GAME_BOARD_SIZE;
    }


    public boolean isOver() {
        checkInitialized();
        return isWon() || (getFreePositions() == 0 && !mergePossible());
    }

    public boolean isWon() {
        checkInitialized();

        return Arrays.stream(board)
                .flatMapToInt(Arrays::stream)
                .anyMatch(val -> val == TILE_2048);
    }

    public void setTileAt(int row, int col, int value) {
        checkInitialized();
        if(row >= 0 && col >= 0
                && row < GAME_BOARD_SIZE && col < GAME_BOARD_SIZE
                && isValuePossible(value)) {
            board[row][col] = value;
        }
    }

    private boolean isValuePossible(int value) {
        for(int i = 1; i <= 11; i++) {
            if(Math.pow(2, i) == value) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        checkInitialized();

        StringBuilder result = new StringBuilder();
        result.append(String.format("Moves: %-3s", moves));
        result.append(String.format("\tScore: %-7s%n", score));
        for (int row = 0; row < GAME_BOARD_SIZE; row++) {
            for (int col = 0; col < GAME_BOARD_SIZE; col++) {
                result.append(String.format("%-5s", getValueAt(row, col)));
            }
            result.append("\n");
        }
        return result.toString();
    }

    private int getFreePositions() {
        var result = 0;
        for (int row = 0; row < GAME_BOARD_SIZE; row++) {
            for (int col = 0; col < GAME_BOARD_SIZE; col++) {
                if(isFree(row, col)) {
                    result++;
                }
            }
        }
        return result;
    }

    private boolean mergePossible() {
        for (int row = 0; row < GAME_BOARD_SIZE; row++) {
            for (int col = 1; col < GAME_BOARD_SIZE; col++) {
                if(board[row][col] == board[row][col-1] || (row > 0 && board[row][col] == board[row-1][col])) {
                    return true;
                }
            }
        }
        return false;
    }

    private void insertIntoFreePosition(int value, int position) {
        var pos = 0;
        for (int row = 0; row < GAME_BOARD_SIZE; row++) {
            for (int col = 0; col < GAME_BOARD_SIZE; col++) {
                if(isFree(row, col)) {
                    if(position == pos) {
                        board[row][col] = value;
                        return; //because inserted
                    }
                    pos++;
                }
            }
        }
    }

    private boolean isFree(int row, int col) {
        return board[row][col] == EMPTY_TILE;
    }

    private int getTile() {
        return randomTileSelector.nextDouble() >= 0.9 ? 4 : 2;
    }

    public void initialize() {
        board = new int[GAME_BOARD_SIZE][GAME_BOARD_SIZE];
        insertIntoFreePosition(getTile(),
                randomPositionSelector.nextInt(0, getFreePositions()));
        insertIntoFreePosition(getTile(),
                randomPositionSelector.nextInt(0, getFreePositions()));
    }

    int getPosition(int index, Direction direction) {
        return switch (direction) {
            case right, down -> GAME_BOARD_SIZE - 1 - index;
            default -> index;
        };
    }

    public void move(Direction direction) {
        checkInitialized();

        for (int i = 0; i < GAME_BOARD_SIZE; i++) {
            int target = 0;
            int currentRow = 0;
            int currentCol = 0;
            int targetRow = 0;
            int targetCol = 0;

            for (int j = 0; j < GAME_BOARD_SIZE; j++) {
                switch (direction) {
                    case right, left -> {
                        currentRow = i;
                        currentCol = getPosition(j, direction);
                        targetRow = currentRow;
                        targetCol = getPosition(target, direction);
                        target = mergeTilesIfPossible(currentRow, currentCol, targetRow, targetCol, target);

                        targetCol = getPosition(target, direction);
                        if(isFree(targetRow, targetCol)) {
                            board[targetRow][targetCol] = board[currentRow][currentCol];
                            board[currentRow][currentCol] = EMPTY_TILE;
                        }
                    }
                    case up, down -> {
                        currentRow = getPosition(j, direction);
                        currentCol = i;
                        targetRow = getPosition(target, direction);
                        targetCol = currentCol;
                        target = mergeTilesIfPossible(currentRow, currentCol, targetRow, targetCol, target);

                        targetRow = getPosition(target, direction);
                        if(isFree(targetRow, targetCol)) {
                            board[targetRow][targetCol] = board[currentRow][currentCol];
                            board[currentRow][currentCol] = EMPTY_TILE;
                        }
                    }
                }
            }
        }

        insertIntoFreePosition(getTile(),
                randomPositionSelector.nextInt(0, getFreePositions()));

        moves++;
    }

    private int mergeTilesIfPossible(int currentRow, int currentCol, int targetRow, int targetCol,
                                     int targetIndex) {
        int newTarget = targetIndex;
        if(!isFree(currentRow, currentCol)) {
            if((currentRow != targetRow || currentCol != targetCol)
                    && board[currentRow][currentCol] == board[targetRow][targetCol]) {
                board[targetRow][targetCol] += board[currentRow][currentCol];
                board[currentRow][currentCol] = EMPTY_TILE;
                score += board[targetRow][targetCol];
                newTarget++;
            } else if (isFree(targetRow, targetCol)) {
                board[targetRow][targetCol] += board[currentRow][currentCol];
                board[currentRow][currentCol] = EMPTY_TILE;
            } else if (currentRow != targetRow || currentCol != targetCol) {
                newTarget++;
            }
        }
        return newTarget;
    }
}
