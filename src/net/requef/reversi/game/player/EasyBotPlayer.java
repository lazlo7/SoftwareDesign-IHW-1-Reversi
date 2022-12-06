package net.requef.reversi.game.player;

import net.requef.reversi.game.board.Board;
import net.requef.reversi.game.board.BoardCoordinates;
import net.requef.reversi.game.board.CellType;
import net.requef.reversi.game.board.MoveData;

import java.util.List;

public class EasyBotPlayer extends Player {
    public EasyBotPlayer(CellType cellTypeController) {
        super(cellTypeController);
    }

    @Override
    public MoveData move(final List<BoardCoordinates> possibleMoves, final CellType enemy, final Board board) {
        System.out.println("*thinking...*");

        var bestMove = possibleMoves.get(0);
        double bestScore = estimateMoveEffectiveness(bestMove.row(),
                bestMove.col(),
                enemy,
                board);

        for (final var move : possibleMoves.stream().skip(1).toList()) {
            final var score = estimateMoveEffectiveness(move.row(),
                    move.col(),
                    enemy,
                    board);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return new MoveData(bestMove, false);
    }

    // Estimation function f(row, col) = s + sum(from i = 1, to i = number of cell flipped, s_i),
    // where s_i = 2 if the flipped cell touches a wall, 1 otherwise
    // and s = 0.8 if the placed cell is in a corner;
    // s = 0.4 if the placed cell touches a wall;
    // s = 0 otherwise
    private double estimateMoveEffectiveness(int row,
                                            int col,
                                            final CellType enemy,
                                            final Board board) {
        final double placed_score = isTouchingWall(row, col, board) ? isInCorner(row, col, board) ? 0.8 : 0.4 : 0;
        final int flipped_score = board
                .getFlippedCells(row, col, cellTypeController, enemy)
                .stream()
                .mapToInt(cell -> isTouchingWall(cell.row(), cell.col(), board) ? 2 : 1)
                .sum();

        return placed_score + flipped_score;
    }

    private boolean isInCorner(int row, int col, final Board board) {
        return (row == 0 && col == board.getBoardSize() - 1)
                || (row == 0 && col == 0)
                || (row == board.getBoardSize() - 1 && col == 0)
                || (row == board.getBoardSize() - 1 && col == board.getBoardSize() - 1);
    }

    private boolean isTouchingWall(int row, int col, final Board board) {
        return row == 0
                || row == board.getBoardSize() - 1
                || col == 0
                || col == board.getBoardSize() - 1;
    }
}
