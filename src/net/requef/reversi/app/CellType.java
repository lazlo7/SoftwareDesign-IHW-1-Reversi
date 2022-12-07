package net.requef.reversi.app;

import net.requef.reversi.util.ConsoleColors;

public enum CellType {
    EMPTY(ConsoleColors.BLACK, ' '),
    BLACK(ConsoleColors.BLACK_BRIGHT, 'X'),
    WHITE(ConsoleColors.WHITE_BRIGHT, 'X'),
    POSSIBLE(ConsoleColors.GREEN_BACKGROUND_BRIGHT, ' ');

    private final String consoleColor;
    private final char symbol;

    CellType(final String consoleColor,
             final char symbol) {
        this.consoleColor = consoleColor;
        this.symbol = symbol;
    }

    public String getConsoleString() {
        return consoleColor + symbol + ConsoleColors.RESET;
    }
}
