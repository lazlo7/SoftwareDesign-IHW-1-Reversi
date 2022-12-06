package net.requef.reversi.game.board;

import net.requef.reversi.util.ConsoleColors;

public enum CellType {
    EMPTY(null),
    BLACK(ConsoleColors.BLACK),
    WHITE(ConsoleColors.WHITE_BRIGHT),
    SUGGESTED(ConsoleColors.YELLOW);

    private final String consoleColor;

    CellType(final String consoleColor) {
        this.consoleColor = consoleColor;
    }

    public String getConsoleColor() {
        return consoleColor;
    }
}
