package net.requef.reversi.app;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class EasyBotPlayer extends BotPlayer {
    public EasyBotPlayer(final CellType side) {
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

        final var move = makeMove(board, possibleMoves);
        System.out.print(board.toPlayersCoordinates(move.pos()));

        if (GameSettings.addDelayToBotMoves) {
            sleepThinkingDelay();
        }

        return move;
    }
}
