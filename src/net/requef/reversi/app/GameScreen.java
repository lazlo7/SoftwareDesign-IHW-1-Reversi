package net.requef.reversi.app;

import net.requef.reversi.util.ConsoleUtil;

import java.util.Scanner;

public class GameScreen extends Screen {
    private Player currentPlayer;
    private Player enemyPlayer;

    public GameScreen(final ScreenAdder screenAdder,
                      final Scanner inputScanner,
                      final Player currentPlayer,
                      final Player enemyPlayer) {
        super(screenAdder, inputScanner);
        this.currentPlayer = currentPlayer;
        this.enemyPlayer = enemyPlayer;
    }

    @Override
    public void draw() {
        System.out.println("This is game screen");
        System.out.println(currentPlayer.getSide());
        System.out.println(enemyPlayer.getSide());
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
        } else {
            log("Unknown command");
        }
    }

    private void switchPlayers() {
        final var temp = currentPlayer;
        currentPlayer = enemyPlayer;
        enemyPlayer = temp;
    }

    @Override
    public boolean shouldExit() {
        return shouldExit;
    }
}
