package net.requef.reversi.app;

import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class BotPlayer extends Player {
    private static final Random RANDOM = new Random();

    public BotPlayer(CellType side) {
        super(side);
    }

    protected MoveData makeMove(final Board board,
                                final Map<BoardPos, List<BoardPos>> possibleMoves) {
        assert !possibleMoves.isEmpty() : "No possible moves";

        BoardPos bestMove = null;
        double bestScore = -1;

        for (final var entry : possibleMoves.entrySet()) {
            final var move = entry.getKey();
            final var score = evaluateMove(board, move, entry.getValue());
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return new MoveData(bestMove, false, false);
    }

    protected double getBestMoveScore(final Board board,
                                      final Map<BoardPos, List<BoardPos>> possibleMoves) {
        assert !possibleMoves.isEmpty() : "No possible moves";

        double bestScore = -1;
        for (final var entry : possibleMoves.entrySet()) {
            final var score = evaluateMove(board, entry.getKey(), entry.getValue());
            if (score > bestScore) {
                bestScore = score;
            }
        }

        return bestScore;
    }

    protected double evaluateMove(final Board board,
                                  final BoardPos placedPos,
                                  final List<BoardPos> flipped) {
        final double placedScore = isTouchingBorder(board, placedPos) ?
                isTouchingCorner(board, placedPos) ? 0.8 : 0.4
                : 0;

        final double flippedScore = flipped
                .stream()
                .mapToInt(pos -> isTouchingBorder(board, pos) ? 2 : 1)
                .sum();

        return placedScore + flippedScore;
    }

    protected boolean isTouchingBorder(final Board board,
                                       final BoardPos pos) {
        return pos.row() == 0 || pos.row() == board.getBoardSize() - 1 ||
                pos.col() == 0 || pos.col() == board.getBoardSize() - 1;
    }

    protected boolean isTouchingCorner(final Board board,
                                       final BoardPos pos) {
        return (pos.row() == 0 || pos.row() == board.getBoardSize() - 1) &&
                (pos.col() == 0 || pos.col() == board.getBoardSize() - 1);
    }

    protected void sleepThinkingDelay() {
        try {
            Thread.sleep(200 + RANDOM.nextInt(1000));
        } catch (InterruptedException ignored) {
        }
    }
}
