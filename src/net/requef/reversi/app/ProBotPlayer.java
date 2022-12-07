package net.requef.reversi.app;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class ProBotPlayer extends BotPlayer {
    public ProBotPlayer(final CellType side) {
        super(side);
    }

    @Override
    public MoveData getMove(final Board board,
                            final CellType enemy,
                            final Scanner inputScanner,
                            final Consumer<String> logger,
                            final Map<BoardPos, List<BoardPos>> possibleMoves) {
        if (GameSettings.addDelayToBotMoves) {
            sleepThinkingDelay();
        }

        BoardPos bestMove = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (final var botPossibleMove : possibleMoves.entrySet()) {
            final var botRow = botPossibleMove.getKey().row();
            final var botCol = botPossibleMove.getKey().col();

            final var previousCellType = board.getCell(botRow, botCol);

            board.setCell(botRow, botCol, side);
            final var enemyPossibleMoves = board.getPossibleMoves(enemy, side);
            final double bestEnemyMoveScore = enemyPossibleMoves.isEmpty() ?
                    0 : getBestMoveScore(board, enemyPossibleMoves);

            board.setCell(botRow, botCol, previousCellType);
            final double score =
                    evaluateMove(board, botPossibleMove.getKey(), botPossibleMove.getValue()) - bestEnemyMoveScore;

            if (score > bestScore) {
                bestScore = score;
                bestMove = botPossibleMove.getKey();
            }
        }

        assert bestMove != null : "No move found";
        System.out.print(board.toPlayersCoordinates(bestMove));

        if (GameSettings.addDelayToBotMoves) {
            sleepThinkingDelay();
        }

        return new MoveData(bestMove, false);
    }
}
