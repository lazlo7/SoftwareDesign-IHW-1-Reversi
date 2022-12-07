package net.requef.reversi.app;

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

    public MenuScreen(final ScreenAdder screenAdder,
                      final Scanner inputScanner) {
        super(screenAdder, inputScanner);
    }

    @Override
    public void draw() {
        System.out.printf("%n%n%s%n%n", ASCII_BANNER);
        System.out.printf("(hint: type 'help' for help)%n%n");    }

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
            screenAdder.push(new SettingsScreen(screenAdder, inputScanner));
        } else if ("help".equals(cmd)) {
            logHelp();
        } else {
            log("Unknown command");
        }
    }

    @Override
    public boolean shouldExit() {
        return shouldExit;
    }

    private void startGame() {
        screenAdder.push(new PlayerChooseScreen(screenAdder, inputScanner,
                blackPlayer -> screenAdder.push(new PlayerChooseScreen(screenAdder, inputScanner,
                        whitePlayer -> screenAdder.push(new GameScreen(screenAdder, inputScanner, blackPlayer, whitePlayer)),
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
