package net.requef.reversi.app;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public abstract class Player {
    protected final CellType side;

    public Player(final CellType side) {
        this.side = side;
    }

    public CellType getSide() {
        return side;
    }

    public abstract MoveData getMove(final Board board,
                                     final CellType enemy,
                                     final Scanner inputScanner,
                                     final Consumer<String> logger,
                                     final Map<BoardPos, List<BoardPos>> possibleMoves);
}
