package net.requef.reversi.app;

import net.requef.reversi.util.ConsoleUtil;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class HumanPlayer extends Player {
    public HumanPlayer(final CellType side) {
        super(side);
    }

    @Override
    public MoveData getMove(final Board board,
                            final CellType enemy,
                            final Scanner inputScanner,
                            final Consumer<String> logger,
                            final Map<BoardPos, List<BoardPos>> possibleMoves) {
        final var input = Screen.splitInput(ConsoleUtil.readLine(inputScanner));

        if (input.size() != 1) {
            logger.accept(String.format("Expected 1 argument, found: %d (type 'help' for more)", input.size()));
            return null;
        }

        final var cmd = input.get(0);
        if ("giveup".equals(cmd)) {
            return new MoveData(null, true, false);
        } else if ("help".equals(cmd)) {
            logHelp(logger);
            return null;
        } else if ("revert".equals(cmd)) {
            return new MoveData(null, false, true);
        }

        if (!board.areValidPlayersCoordinates(cmd)) {
            logger.accept("Unknown command or invalid coordinates");
            return null;
        }

        return new MoveData(board.fromPlayersCoordinates(cmd), false, false);
    }

    private void logHelp(final Consumer<String> logger) {
        logger.accept("Available commands:");
        logger.accept("giveup - give up (instantly lose the game and exit);");
        logger.accept("help - show this help message.");
        logger.accept("revert - revert to your previous move.");
        logger.accept("<position> - place your piece on the <position> (e.g \"a3\", \"d5\");");
    }
}
