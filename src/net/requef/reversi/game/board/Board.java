package net.requef.reversi.game.board;

import net.requef.reversi.game.Drawable;
import net.requef.reversi.util.ConsoleColors;

import java.util.List;

public class Board implements Drawable {
    private static final int BOARD_SIZE = 8;
    private static final int CELL_INNER_MARGIN = 1;
    private static final int COLUMN_COUNT
            = BOARD_SIZE * (1 + CELL_INNER_MARGIN * 2 + 1) + 1;
    private static final int ROW_COUNT = BOARD_SIZE * (1 + 1) + 1;

    private final CellType[][] board = new CellType[BOARD_SIZE][BOARD_SIZE];

    public Board() {
        reset();
    }

    public void clear() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = CellType.EMPTY;
            }
        }
    }

    public void clear(final CellType clearFrom) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == clearFrom) {
                    board[i][j] = CellType.EMPTY;
                }
            }
        }
    }

    public boolean areValidCoordinates(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }

    public void setCell(int x, int y, CellType cellType) {
        board[x][y] = cellType;
    }

    public CellType getCell(int x, int y) {
        return board[x][y];
    }

    public int getCellCount(final CellType cellType) {
        int count = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == cellType) {
                    count++;
                }
            }
        }
        return count;
    }

    public void reset() {
        clear();
        setCell(3, 3, CellType.BLACK);
        setCell(4, 4, CellType.BLACK);
        setCell(4, 3, CellType.WHITE);
        setCell(3, 4, CellType.WHITE);
    }

    public List<BoardCoordinates> getFlippedCells(int row,
                                                  int col,
                                                  final CellType mine,
                                                  final CellType enemy) {
        return null;
    }

    public BoardCoordinates fromPlayersCoordinates(final String playersCoordinates) {
        int col = playersCoordinates.charAt(0) - 'a';
        int row = BOARD_SIZE - (playersCoordinates.charAt(1) - '0');
        return new BoardCoordinates(row, col);
    }

    public String toPlayersCoordinates(final BoardCoordinates boardCoordinates) {
        char col = (char) (boardCoordinates.row() + 'a');
        char row = (char) (BOARD_SIZE - (boardCoordinates.col() - '0'));
        return String.format("%c%c", col, row);
    }

    public boolean areValidPlayersCoordinates(final String input) {
        if (input.length() != 2) {
            return false;
        }

        final char col = input.charAt(0);
        final char row = input.charAt(1);
        if (col < 'a' || col > 'a' + BOARD_SIZE - 1) {
            return false;
        }

        if (row < '1' || row > '1' + BOARD_SIZE - 1) {
            return false;
        }

        return true;
    }

    @Override
    public void draw() {
        /*
             ---------------------------------
           8 | # | # | # | # | # | # | # | # |
             ---------------------------------
           7 | # | # | # | # | # | # | # | # |
             ---------------------------------
           6 | # | # | # | # | # | # | # | # |
             ---------------------------------
           5 | # | # | # | # | # | # | # | # |
             ---------------------------------
           4 | # | # | # | # | # | # | # | # |
             ---------------------------------
           3 | # | # | # | # | # | # | # | # |
             ---------------------------------
           2 | # | # | # | # | # | # | # | # |
             ---------------------------------
           1 | # | # | # | # | # | # | # | # |
             ---------------------------------
               a   b   c   d   e   f   g   h
        */
        for (int row = 0; row < BOARD_SIZE; ++row) {
            // Draw the horizontal line
            System.out.print("\t");
            System.out.println("-".repeat(COLUMN_COUNT));

            // Draw the number.
            System.out.printf("%d\t", BOARD_SIZE - row);

            for (int col = 0; col < BOARD_SIZE; ++col) {
                // Draw the cell.
                System.out.print("|");
                System.out.print(" ".repeat(CELL_INNER_MARGIN));

                final var cellColor = board[row][col].getConsoleColor();
                if (cellColor != null) {
                    System.out.print(board[col][row].getConsoleColor() + "#" + ConsoleColors.RESET);
                } else {
                    System.out.print(" ");
                }
                System.out.print(" ".repeat(CELL_INNER_MARGIN));
            }

            // Draw the right border of the last cell.
            System.out.println("|");
        }

        // Draw the last horizontal line.
        System.out.print("\t");
        System.out.println("-".repeat(COLUMN_COUNT));

        // Draw the first column letter.
        System.out.print("\t");
        System.out.print(" ".repeat(1 + CELL_INNER_MARGIN));
        System.out.print("a");

        // Draw the rest column letters.
        for (int i = 1; i < BOARD_SIZE; ++i) {
            System.out.print(" ".repeat(1 + CELL_INNER_MARGIN * 2));
            System.out.print((char) ('a' + i));
        }
    }
}
