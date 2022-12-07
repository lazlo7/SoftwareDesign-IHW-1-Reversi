package net.requef.reversi.app;

import java.util.Scanner;
import java.util.function.Consumer;

public class EasyBotPlayer extends Player {
    public EasyBotPlayer(final CellType side) {
        super(side);
    }

    @Override
    public MoveData getMove(final Board board,
                            final CellType enemy,
                            final Scanner inputScanner,
                            final Consumer<String> logger) {
        // TODO
        return null;
    }
}
