package net.requef.reversi.app;

import net.requef.reversi.util.ConsoleUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GameScreen extends Screen {
    private final Board board = new Board(8);
    private final Map<BoardPos, List<BoardPos>> possibleMoves = new HashMap<>();
    private Player currentPlayer;
    private Player enemyPlayer;

    private boolean gaveUp = false;

    public GameScreen(final ScreenAdder screenAdder,
                      final Scanner inputScanner,
                      final Player currentPlayer,
                      final Player enemyPlayer) {
        super(screenAdder, inputScanner);
        this.currentPlayer = currentPlayer;
        this.enemyPlayer = enemyPlayer;

        calculatePossibleMoves();
    }

    @Override
    public void draw() {
        // Draw each side's score.
        System.out.printf("\tScore\tBlack: %d\t\tWhite: %d%n%n",
                board.getCellCount(CellType.BLACK), board.getCellCount(CellType.WHITE));

        // Draw the board.
        board.draw();

        // Draw the current player's turn.
        System.out.printf("%n%n\t%s's turn%n%n", currentPlayer.getSide());

        // Show the possible moves (if such option is toggled in settings and player didn't give up).
        if (GameSettings.showPossibleMoves && !possibleMoves.isEmpty() && !gaveUp) {
            System.out.printf("%nPossible moves: ");
            for (final BoardPos boardPos : possibleMoves.keySet()) {
                System.out.printf("%s ", board.toPlayersCoordinates(boardPos));
            }
            System.out.printf("%n");
        }
    }

    @Override
    public void onUpdate() {
        if (gaveUp) {
            shouldExit = true;
            System.out.println("Press enter to continue...");
            ConsoleUtil.readLine(inputScanner);
            return;
        }

        if (possibleMoves.isEmpty()) {
            log("No possible moves, skipping turn...");
            switchPlayers();
            return;
        }

        System.out.print("input> ");
        final var move = currentPlayer.getMove(board, enemyPlayer.getSide(), inputScanner, this::log);

        if (move == null) {
            return;
        }

        if (move.gaveUp()) {
            log(String.format("%s gave up!", currentPlayer.getSide()));
            log(String.format("%s wins with %d points!",
                    enemyPlayer.getSide(), board.getCellCount(enemyPlayer.getSide())));
            gaveUp = true;
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
        if (flipped != null) {
            for (final var pos : flipped) {
                board.setCell(pos.row(), pos.col(), currentPlayer.getSide());
            }
        }

        // Switch players.
        switchPlayers();

        // Calculate possible moves for the upcoming turn,
        // so that they will get drawn on the next draw call.
        calculatePossibleMoves();
    }

    private void calculatePossibleMoves() {
        // Update the possible moves map.
        // Keys: possible moves.
        // Values: enemy's cells to flip for this possible move.
        // Use rules for the game "Othello" aka "Reversi".
        possibleMoves.clear();

        // TODO.
        for (int row = 0; row < board.getBoardSize(); row++) {
            for (int col = 0; col < board.getBoardSize(); col++) {
                if (board.isVacant(row, col)) {
                    possibleMoves.put(new BoardPos(row, col), null);
                }
            }
        }
    }

    private void switchPlayers() {
        final var temp = currentPlayer;
        currentPlayer = enemyPlayer;
        enemyPlayer = temp;
    }

    @Override
    public boolean shouldExit() {
        return shouldExit;
    }
}
