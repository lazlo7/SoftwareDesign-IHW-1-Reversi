package net.requef.reversi.game.player;

import net.requef.reversi.game.board.Board;
import net.requef.reversi.game.board.BoardCoordinates;
import net.requef.reversi.game.board.CellType;
import net.requef.reversi.game.board.MoveData;

import java.util.List;

public interface BoardAnalyzer {
    MoveData move(final List<BoardCoordinates> possibleMoves, final CellType enemy, final Board board);
}
