package net.requef.reversi.game;

public record GameSettings(boolean showPossibleMoves,
                           boolean visualisePossibleMoves,
                           boolean addBotThinkingDelay) {
    public static final GameSettings DEFAULT =
            new GameSettings(true, true, true);
}
