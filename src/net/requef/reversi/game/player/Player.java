package net.requef.reversi.game.player;

import net.requef.reversi.game.board.CellType;

// Describes an entity that can control a board.
public abstract class Player implements BoardAnalyzer {
    public final CellType cellTypeController;

    public Player(CellType cellTypeController) {
        this.cellTypeController = cellTypeController;
    }
}
