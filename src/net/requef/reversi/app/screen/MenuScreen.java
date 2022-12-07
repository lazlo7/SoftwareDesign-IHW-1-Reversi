package net.requef.reversi.app.screen;

import net.requef.reversi.app.board.CellType;
import net.requef.reversi.util.ConsoleUtil;

import java.util.Scanner;

public class MenuScreen extends Screen {
    private static final String ASCII_BANNER = """
            
                  ___           ___           ___           ___           ___           ___                \s
                 /\\  \\         /\\  \\         /\\__\\         /\\  \\         /\\  \\         /\\  \\          ___  \s
                /::\\  \\       /::\\  \\       /:/  /        /::\\  \\       /::\\  \\       /::\\  \\        /\\  \\ \s
               /:/\\:\\  \\     /:/\\:\\  \\     /:/  /        /:/\\:\\  \\     /:/\\:\\  \\     /:/\\ \\  \\       \\:\\  \\\s
              /::\\~\\:\\  \\   /::\\~\\:\\  \\   /:/__/  ___   /::\\~\\:\\  \\   /::\\~\\:\\  \\   _\\:\\~\\ \\  \\      /::\\__\\
             /:/\\:\\ \\:\\__\\ /:/\\:\\ \\:\\__\\  |:|  | /\\__\\ /:/\\:\\ \\:\\__\\ /:/\\:\\ \\:\\__\\ /\\ \\:\\ \\ \\__\\  __/:/\\/__/
             \\/_|::\\/:/  / \\:\\~\\:\\ \\/__/  |:|  |/:/  / \\:\\~\\:\\ \\/__/ \\/_|::\\/:/  / \\:\\ \\:\\ \\/__/ /\\/:/  /  \s
                |:|::/  /   \\:\\ \\:\\__\\    |:|__/:/  /   \\:\\ \\:\\__\\      |:|::/  /   \\:\\ \\:\\__\\   \\::/__/   \s
                |:|\\/__/     \\:\\ \\/__/     \\::::/__/     \\:\\ \\/__/      |:|\\/__/     \\:\\/:/  /    \\:\\__\\   \s
                |:|  |        \\:\\__\\        ~~~~          \\:\\__\\        |:|  |        \\::/  /      \\/__/   \s
                 \\|__|         \\/__/                       \\/__/         \\|__|         \\/__/               \s
            """;

    private int bestScore = -1;

    public MenuScreen(final ScreenPusher screenPusher,
                      final Scanner inputScanner) {
        super(screenPusher, inputScanner);
    }

    @Override
    public void draw() {
        System.out.printf("%n%n%s%n%n", ASCII_BANNER);
        if (bestScore != -1) {
            System.out.printf("Best score: %d%n%n", bestScore);
        }
        System.out.printf("(hint: type \"help\" for help)%n%n");
    }

    @Override
    public void onUpdate() {
        System.out.print("input> ");
        final var input = splitInput(ConsoleUtil.readLine(inputScanner));

        if (input.isEmpty()) {
            log("Please enter a valid command");
            return;
        }

        final var cmd = input.get(0);
        if ("exit".equals(cmd)) {
            shouldExit = true;
        } else if ("start".equals(cmd)) {
            startGame();
        } else if ("settings".equals(cmd)) {
            screenPusher.push(new SettingsScreen(screenPusher, inputScanner));
        } else if ("help".equals(cmd)) {
            logHelp();
        } else {
            log("Unknown command");
        }
    }

    private void startGame() {
        screenPusher.push(new PlayerChooseScreen(screenPusher, inputScanner,
                blackPlayer -> screenPusher.push(new PlayerChooseScreen(screenPusher, inputScanner,
                        whitePlayer -> screenPusher.push(new GameScreen(screenPusher, inputScanner, blackPlayer,
                                whitePlayer, score -> bestScore = Math.max(bestScore, score))),
                        CellType.WHITE)),
                CellType.BLACK));
    }

    private void logHelp() {
        log("Available commands:");
        log("start - start a new game;");
        log("exit - exit the application;");
        log("settings - open settings screen;");
        log("help - show this help message.");
    }
}
