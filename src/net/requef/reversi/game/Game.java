package net.requef.reversi.game;

import net.requef.reversi.game.board.Board;
import net.requef.reversi.game.board.BoardCoordinates;
import net.requef.reversi.game.board.CellType;
import net.requef.reversi.game.player.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Game implements Screen {
    private static final int endTurnDelayMillis = 1000;
    private final Queue<String> infoQueue = new LinkedList<>();
    private final Board board = new Board();
    private Player currentPlayer;
    private Player enemyPlayer;

    private boolean isGameFinished = false;
    private final List<BoardCoordinates> possibleMoves = new ArrayList<>();

    public Game(Player player1, Player player2) {
        this.currentPlayer = player1;
        this.enemyPlayer = player2;
    }

    public void updatePossibleMoves(CellType mine, CellType enemy) {
        possibleMoves.clear();

        for (int row = 0; row < board.getBoardSize(); ++row) {
            for (int col = 0; col < board.getBoardSize(); ++col) {
                if (board.getCell(row, col) == CellType.EMPTY) {
                    if (isMoveValid(row, col, mine, enemy)) {
                        possibleMoves.add(new BoardCoordinates(row, col));
                    }
                }
            }
        }
    }

    public boolean isMoveValid(int row, int col, CellType mine, CellType enemy) {
        if (board.getCell(row, col) != CellType.EMPTY) {
            return false;
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (i == 1 && j == 1) {
                    continue;
                }

                int rowDirection = i - 1;
                int colDirection = j - 1;

                int currentRow = row + rowDirection;
                int currentCol = col + colDirection;

                if (!board.areValidCoordinates(currentRow, currentCol)) {
                    continue;
                }

                if (board.getCell(currentRow, currentCol) == enemy) {
                    while (true) {
                        currentRow += rowDirection;
                        currentCol += colDirection;

                        if (!board.areValidCoordinates(currentRow, currentCol)) {
                            break;
                        }

                        if (board.getCell(currentRow, currentCol) == mine) {
                            return true;
                        }

                        if (board.getCell(currentRow, currentCol) == CellType.EMPTY) {
                            break;
                        }
                    }
                }
            }
        }

        return false;
    }

    public void flipPieces(int row, int col, CellType mine, CellType enemy) {
        board.setCell(row, col, mine);

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (i == 1 && j == 1) {
                    continue;
                }

                int rowDirection = i - 1;
                int colDirection = j - 1;

                int currentRow = row + rowDirection;
                int currentCol = col + colDirection;

                if (!board.areValidCoordinates(currentRow, currentCol)) {
                    continue;
                }

                if (board.getCell(currentRow, currentCol) == enemy) {
                    while (true) {
                        currentRow += rowDirection;
                        currentCol += colDirection;

                        if (!board.areValidCoordinates(currentRow, currentCol)) {
                            break;
                        }

                        if (board.getCell(currentRow, currentCol) == mine) {
                            while (true) {
                                currentRow -= rowDirection;
                                currentCol -= colDirection;

                                if (currentRow == row && currentCol == col) {
                                    break;
                                }

                                board.setCell(currentRow, currentCol, mine);
                            }

                            break;
                        }

                        if (board.getCell(currentRow, currentCol) == CellType.EMPTY) {
                            break;
                        }
                    }
                }
            }
        }
    }

    private void preUpdate() {
        // Clear the board from suggested moves.
        board.clear(CellType.SUGGESTED);

        // Update possible moves.
        updatePossibleMoves(currentPlayer.cellTypeController, enemyPlayer.cellTypeController);
    }

    private void redraw() {
        // Draw the current players' scores.
        System.out.printf("%n%n");
        System.out.printf("Score | %s: %d | %s: %d %n",
                currentPlayer.cellTypeController,
                board.getCellCount(currentPlayer.cellTypeController),
                enemyPlayer.cellTypeController,
                board.getCellCount(enemyPlayer.cellTypeController));

        // Draw the board.
        System.out.printf("%n%n");
        board.draw();

        // Draw the player's turn.
        System.out.printf("%n%n%s turn", currentPlayer.cellTypeController);

        // Draw the info queue.
        if (!infoQueue.isEmpty()) {
            System.out.printf("%nInfo:%n");
            while (!infoQueue.isEmpty()) {
                System.out.printf("%s%n", infoQueue.poll());
            }
        }

        // Draw the possible player's moves.
        if (possibleMoves.isEmpty()) {
            System.out.println("No possible moves. Skipping turn...");
            try {
                Thread.sleep(endTurnDelayMillis);
            } catch (InterruptedException e) {
                System.out.println("[WARNING] Thread sleep delay was interrupted");
            }
            return;
        }

        System.out.printf("%nPossible moves: ");
        for (BoardCoordinates coordinates : possibleMoves) {
            System.out.printf(board.toPlayersCoordinates(coordinates));
            if (coordinates != possibleMoves.get(possibleMoves.size() - 1)) {
                System.out.print(", ");
            }
        }

        System.out.println();
    }

    private void postUpdate() {
        final var moveData = currentPlayer.move(possibleMoves,
                enemyPlayer.cellTypeController,
                board);
        assert moveData != null : "[FATAL ERROR] Move data cannot be null";

        if (moveData.forfeiting()) {
            System.out.printf("%s forfeited!%n", currentPlayer.cellTypeController);
            isGameFinished = true;
            return;
        }

        // Check the player's move.
        final var moveCoordinates = moveData.coordinates();
        if (possibleMoves.contains(moveCoordinates)) {
            logPlayerInfo(String.format("Placing a piece at %s",
                    board.toPlayersCoordinates(moveCoordinates)));

            // Flip the enemy's pieces.
            flipPieces(moveCoordinates.row(), moveCoordinates.col(),
                    currentPlayer.cellTypeController, enemyPlayer.cellTypeController);
        } else {
            logPlayerInfo(String.format("Invalid move at %s",
                    board.toPlayersCoordinates(moveCoordinates)));
            return;
        }

        // Switch players.
        Player temp = currentPlayer;
        currentPlayer = enemyPlayer;
        enemyPlayer = temp;
    }

    private void logPlayerInfo(final String info) {
        infoQueue.add(String.format("%s: %s", currentPlayer.cellTypeController, info));
    }

    @Override
    public void draw() {
        preUpdate();
        redraw();
        postUpdate();
    }

    @Override
    public boolean isFinished() {
        return isGameFinished;
    }
}
