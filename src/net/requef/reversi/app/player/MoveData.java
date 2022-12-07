package net.requef.reversi.app.player;

import net.requef.reversi.app.board.BoardPos;

public record MoveData(BoardPos pos, boolean gaveUp, boolean revert) {
}
