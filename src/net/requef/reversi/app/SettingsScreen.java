package net.requef.reversi.app;

import net.requef.reversi.util.ConsoleUtil;

import java.util.Scanner;

public class SettingsScreen extends Screen {
    public SettingsScreen(final ScreenPusher screenPusher,
                          final Scanner inputScanner) {
        super(screenPusher, inputScanner);
    }

    @Override
    public void draw() {
        System.out.println("Settings:");
        System.out.printf("1: [%s] Print possible moves%n", GameSettings.showPossibleMoves ? "X" : " ");
    }

    @Override
    public void onUpdate() {
        System.out.print("input> ");
        final var input = splitInput(ConsoleUtil.readLine(inputScanner));

        if (input.isEmpty()) {
            log("Please enter a valid command or type 'help'");
            return;
        }

        final var cmd = input.get(0);

        if ("exit".equals(cmd)) {
            shouldExit = true;
        } else if ("toggle".equals(cmd)) {
            if (!checkArgumentNumber(2, input.size())) {
                return;
            }
            toggleSetting(input.get(1));
        } else if ("help".equals(cmd)) {
            logHelp();
        } else {
            log("Unknown command");
        }
    }

    private void toggleSetting(final String settingNumber) {
        switch (settingNumber) {
            case "1" -> GameSettings.showPossibleMoves = !GameSettings.showPossibleMoves;
            default -> log("Unknown setting number");
        }
    }

    private void logHelp() {
        log("Available commands:");
        log("exit - return to menu;");
        log("toggle <setting_number> - toggle setting with given number;");
        log("help - show this help message.");
    }
}
