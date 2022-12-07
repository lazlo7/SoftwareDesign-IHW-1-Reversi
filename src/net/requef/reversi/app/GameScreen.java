package net.requef.reversi.app;

import net.requef.reversi.util.ConsoleUtil;

import java.util.*;
import java.util.function.Consumer;

public class GameScreen extends Screen {
    private final Board board = new Board(8);
    private final Map<BoardPos, List<BoardPos>> possibleMoves = new HashMap<>();
    private Player currentPlayer;
    private Player enemyPlayer;
    private boolean isGameOver = false;
    private boolean previousTurnSkipped = false;

    public GameScreen(final ScreenPusher screenPusher,
                      final Scanner inputScanner,
                      final Player currentPlayer,
                      final Player enemyPlayer) {
        super(screenPusher, inputScanner);
        this.currentPlayer = currentPlayer;
        this.enemyPlayer = enemyPlayer;

        updatePossibleMoves();
    }

    @Override
    public void draw() {
        // Draw each side's score.
        System.out.printf("\tScore\tBlack: %d\t\tWhite: %d%n%n",
                board.getCellCount(CellType.BLACK), board.getCellCount(CellType.WHITE));

        // Visualise possible moves (if such option is toggled in settings and player didn't give up).
        if (GameSettings.showPossibleMoves && !isGameOver) {
            board.clear(CellType.POSSIBLE);
            for (final var possibleMove : possibleMoves.keySet()) {
                board.setCell(possibleMove.row(), possibleMove.col(), CellType.POSSIBLE);
            }
        }

        // Draw the board.
        board.draw();

        // Draw the current player's turn.
        System.out.printf("%n%n\t%s's turn%n%n", currentPlayer.getSide());

        // Show the possible moves (if such option is toggled in settings and player didn't give up).
        if (GameSettings.showPossibleMoves && !possibleMoves.isEmpty() && !isGameOver) {
            System.out.printf("%nPossible moves: ");
            for (final BoardPos boardPos : possibleMoves.keySet()) {
                System.out.printf("%s ", board.toPlayersCoordinates(boardPos));
            }
            System.out.printf("%n");
        }
    }

    @Override
    public void onUpdate() {
        if (isGameOver) {
            shouldExit = true;
            System.out.println("Press enter to continue...");
            ConsoleUtil.readLine(inputScanner);
            return;
        }

        if (board.isFull()) {
            log("Board is full! Game over.");
            endGame();
            return;
        }

        if (possibleMoves.isEmpty()) {
            if (previousTurnSkipped) {
                log("Stalemate! Both players can't make a move.");
                endGame();
                return;
            }
            log("No possible moves, skipping turn...");
            previousTurnSkipped = true;
            switchPlayers();
            return;
        }

        previousTurnSkipped = false;
        System.out.print("input> ");
        final var move = currentPlayer.getMove(board, enemyPlayer.getSide(), inputScanner, this::log);

        if (move == null) {
            return;
        }

        if (move.gaveUp()) {
            log(String.format("%s gave up!", currentPlayer.getSide()));
            log(String.format("%s wins with %d points!",
                    enemyPlayer.getSide(), board.getCellCount(enemyPlayer.getSide())));
            isGameOver = true;
            return;
        }

        assert move.pos() != null : "move.pos() can't be null unless player gave up";
        if (!possibleMoves.containsKey(move.pos())) {
            log("Invalid move");
            return;
        }

        // Make the move.
        board.setCell(move.pos().row(), move.pos().col(), currentPlayer.getSide());

        // Flip enemy's cells.
        final var flipped = possibleMoves.get(move.pos());
        assert flipped != null : "possible moves value can't be null";
        for (final var pos : flipped) {
            board.setCell(pos.row(), pos.col(), currentPlayer.getSide());
        }

        // Switch players.
        switchPlayers();

        // Calculate possible moves for the upcoming turn,
        // so that they will get drawn on the next draw call.
        updatePossibleMoves();
    }

    private void endGame() {
        final var blackPoints = board.getCellCount(CellType.BLACK);
        final var whitePoints = board.getCellCount(CellType.WHITE);

        if (blackPoints > whitePoints) {
            log(String.format("BLACK wins with %d points!", blackPoints));
        } else if (whitePoints > blackPoints) {
            log(String.format("WHITE wins with %d points!", whitePoints));
        } else {
            log("Draw! Both players have the same number of points.");
        }

        isGameOver = true;
    }

    private void updatePossibleMoves() {
        // Update the possible moves map.
        possibleMoves.clear();

        final Consumer<Map.Entry<BoardPos, List<BoardPos>>> addIfPossible
                = (final Map.Entry<BoardPos, List<BoardPos>> entry) -> {
            if (entry != null && !entry.getValue().isEmpty()) {
                if (possibleMoves.containsKey(entry.getKey())) {
                    possibleMoves.get(entry.getKey()).addAll(entry.getValue());
                    return;
                }
                possibleMoves.put(entry.getKey(), entry.getValue());
            }
        };

        for (int row = 0; row < board.getBoardSize(); ++row) {
            for (int col = 0; col < board.getBoardSize(); ++col) {
                // Check only such cells that don't contain black or white chips.
                if (board.getCell(row, col) != currentPlayer.getSide()) {
                    continue;
                }

                // Diagonal (going to top-left).
                addIfPossible.accept(explorePossibleMove(row, col, -1, -1));
                // Diagonal (going to top-right).
                addIfPossible.accept(explorePossibleMove(row, col, -1, 1));
                // Diagonal (going to bottom-right).
                addIfPossible.accept(explorePossibleMove(row, col, 1, 1));
                // Diagonal (going to bottom-left).
                addIfPossible.accept(explorePossibleMove(row, col, 1, -1));
                // Horizontal (going to left).
                addIfPossible.accept(explorePossibleMove(row, col, 0, -1));
                // Horizontal (going to right).
                addIfPossible.accept(explorePossibleMove(row, col, 0, 1));
                // Vertical (going to top).
                addIfPossible.accept(explorePossibleMove(row, col, -1, 0));
                // Vertical (going to bottom).
                addIfPossible.accept(explorePossibleMove(row, col, 1, 0));
            }
        }
    }

    private Map.Entry<BoardPos, List<BoardPos>> explorePossibleMove(int row,
                                                                   int col,
                                                                   int rowDir,
                                                                   int colDir) {
        final List<BoardPos> flipped = new ArrayList<>();

        while (true) {
            row += rowDir;
            col += colDir;

            // If we are out of bounds, return an empty list.
            if (!(board.isBoardPosValid(row) && board.isBoardPosValid(col))) {
                return null;
            }

            // Continue if we found a cell of the current's player's side.
            if (board.getCell(row, col) == currentPlayer.getSide()) {
                // But If we found at least one flipped cell, return an empty list.
                if (!flipped.isEmpty()) {
                    return null;
                }
                continue;
            }

            // Check if we found a cell of the enemy's side and add it to the list.
            if (board.getCell(row, col) == enemyPlayer.getSide()) {
                flipped.add(new BoardPos(row, col));
                continue;
            }

            // If we found a vacant cell, return the list of flipped cells.
            if (board.isVacant(row, col)) {
                return new AbstractMap.SimpleImmutableEntry<>(new BoardPos(row, col), flipped);
            }

            // Should be unreachable.
            assert false : "Unreachable";
        }
    }

    private void switchPlayers() {
        final var temp = currentPlayer;
        currentPlayer = enemyPlayer;
        enemyPlayer = temp;
    }
}
