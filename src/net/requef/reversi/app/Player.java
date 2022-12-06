package net.requef.reversi.app;

public abstract class Player {
    private final CellType side;

    public Player(final CellType side) {
        this.side = side;
    }

    public CellType getSide() {
        return side;
    }
}
