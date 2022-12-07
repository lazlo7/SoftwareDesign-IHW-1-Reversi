package net.requef.reversi.app;

import java.util.Scanner;
import java.util.function.Consumer;

public abstract class Player {
    private final CellType side;

    public Player(final CellType side) {
        this.side = side;
    }

    public CellType getSide() {
        return side;
    }

    public abstract MoveData getMove(final Board board,
                                     final CellType enemy,
                                     final Scanner inputScanner,
                                     final Consumer<String> logger);
}
