package net.requef.reversi.app;

public class Board implements Drawable {
    private static final int CELL_INNER_MARGIN = 1;
    private final int boardSize;
    private final int columnCount;
    private final CellType[][] board;

    public Board(int boardSize) {
        this.boardSize = boardSize;
        columnCount = boardSize * (1 + CELL_INNER_MARGIN * 2 + 1) + 1;
        board = new CellType[boardSize][boardSize];
        reset();
    }

    public int getBoardSize() {
        return boardSize;
    }

    public CellType getCell(int row, int col) {
        return board[row][col];
    }

    public void setCell(int row, int col, CellType cellType) {
        board[row][col] = cellType;
    }

    public boolean isVacant(int row, int col) {
        return !(board[row][col] == CellType.BLACK || board[row][col] == CellType.WHITE);
    }

    public void clear() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = CellType.EMPTY;
            }
        }
    }

    public void clear(final CellType clearFrom) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == clearFrom) {
                    board[i][j] = CellType.EMPTY;
                }
            }
        }
    }

    public int getCellCount(final CellType cellType) {
        int count = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == cellType) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isFull() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (isVacant(row, col)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void reset() {
        clear();
        setCell(3, 3, CellType.BLACK);
        setCell(4, 4, CellType.BLACK);
        setCell(4, 3, CellType.WHITE);
        setCell(3, 4, CellType.WHITE);
    }

    public BoardPos fromPlayersCoordinates(final String playersCoordinates) {
        int col = playersCoordinates.charAt(0) - 'a';
        int row = boardSize - (playersCoordinates.charAt(1) - '0');
        return new BoardPos(row, col);
    }

    public String toPlayersCoordinates(final BoardPos boardCoordinates) {
        char col = (char) (boardCoordinates.col() + 'a');
        char row = (char) (boardSize - (boardCoordinates.row() - '0'));
        return String.format("%c%c", col, row);
    }

    public boolean isBoardPosValid(int pos) {
        return pos >= 0 && pos < boardSize;
    }

    public boolean areValidPlayersCoordinates(final String input) {
        if (input.length() != 2) {
            return false;
        }
        final var boardPos = fromPlayersCoordinates(input);
        return isBoardPosValid(boardPos.row()) && isBoardPosValid(boardPos.col());
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
        for (int row = 0; row < boardSize; ++row) {
            // Draw the horizontal line
            System.out.print("\t\t");
            System.out.println("-".repeat(columnCount));

            // Draw the number.
            System.out.printf("\t%d\t", boardSize - row);

            for (int col = 0; col < boardSize; ++col) {
                // Draw the cell.
                System.out.print("|");
                System.out.print(" ".repeat(CELL_INNER_MARGIN));
                System.out.print(board[row][col].getConsoleString());
                System.out.print(" ".repeat(CELL_INNER_MARGIN));
            }

            // Draw the right border of the last cell.
            System.out.println("|");
        }

        // Draw the last horizontal line.
        System.out.print("\t\t");
        System.out.println("-".repeat(columnCount));

        // Draw the first column letter.
        System.out.print("\t\t");
        System.out.print(" ".repeat(1 + CELL_INNER_MARGIN));
        System.out.print("a");

        // Draw the rest column letters.
        for (int i = 1; i < boardSize; ++i) {
            System.out.print(" ".repeat(1 + CELL_INNER_MARGIN * 2));
            System.out.print((char) ('a' + i));
        }
    }
}