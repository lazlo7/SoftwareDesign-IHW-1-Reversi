package net.requef.reversi.app;

import net.requef.reversi.util.ConsoleUtil;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.IntConsumer;

public class GameScreen extends Screen {
    private final Board board = new Board(8);
    private final IntConsumer bestScoreSetter;
    private Map<BoardPos, List<BoardPos>> possibleMoves;
    private Player currentPlayer;
    private Player enemyPlayer;
    private boolean isGameOver = false;
    private boolean previousTurnSkipped = false;

    public GameScreen(final ScreenPusher screenPusher,
                      final Scanner inputScanner,
                      final Player currentPlayer,
                      final Player enemyPlayer,
                      final IntConsumer bestScoreSetter) {
        super(screenPusher, inputScanner);
        this.currentPlayer = currentPlayer;
        this.enemyPlayer = enemyPlayer;
        this.bestScoreSetter = bestScoreSetter;
        possibleMoves = board.getPossibleMoves(currentPlayer.getSide(), enemyPlayer.getSide());
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
            possibleMoves = board.getPossibleMoves(currentPlayer.getSide(), enemyPlayer.getSide());
            return;
        }

        previousTurnSkipped = false;
        System.out.print("input> ");
        final var move =
                currentPlayer.getMove(board, enemyPlayer.getSide(), inputScanner, this::log, possibleMoves);

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
        possibleMoves = board.getPossibleMoves(currentPlayer.getSide(), enemyPlayer.getSide());
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

        if (currentPlayer instanceof HumanPlayer && enemyPlayer instanceof HumanPlayer) {
            bestScoreSetter.accept(Math.max(blackPoints, whitePoints));
        } else if (currentPlayer instanceof HumanPlayer
                && board.getCellCount(currentPlayer.getSide()) > board.getCellCount(enemyPlayer.getSide())) {
            bestScoreSetter.accept(board.getCellCount(currentPlayer.getSide()));
        } else if (enemyPlayer instanceof HumanPlayer && board.getCellCount(enemyPlayer.getSide()) > board.getCellCount(currentPlayer.getSide())) {
            bestScoreSetter.accept(board.getCellCount(enemyPlayer.getSide()));
        }

        isGameOver = true;
    }

    private void switchPlayers() {
        final var temp = currentPlayer;
        currentPlayer = enemyPlayer;
        enemyPlayer = temp;
    }
}
