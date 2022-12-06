package net.requef.reversi.game.player;

import net.requef.reversi.game.board.Board;
import net.requef.reversi.game.board.BoardCoordinates;
import net.requef.reversi.game.board.CellType;
import net.requef.reversi.game.board.MoveData;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class HumanPlayer extends Player {
    private final Scanner inputScanner;

    public HumanPlayer(Scanner inputScanner,
                       CellType cellTypeController) {
        super(cellTypeController);
        this.inputScanner = inputScanner;
    }

    @Override
    public MoveData move(final List<BoardCoordinates> possibleMoves, final CellType enemy, final Board board) {
        String input;
        while (true) {
            System.out.print("Please enter a valid move (e.g. a1, g7, h4) or \"forfeit\": ");
            try {
                input = inputScanner.nextLine();
            } catch (NoSuchElementException e) {
                continue;
            }

            if ("forfeit".equals(input)) {
                return new MoveData(null, true);
            }

            if (!board.areValidPlayersCoordinates(input)) {
                continue;
            }

            final var coordinates = board.fromPlayersCoordinates(input);
            if (!possibleMoves.contains(coordinates)) {
                continue;
            }

            return new MoveData(coordinates, false);
        }
    }
}
