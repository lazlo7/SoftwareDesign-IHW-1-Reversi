package net.requef.reversi;

import net.requef.reversi.game.Menu;
import net.requef.reversi.game.Screen;
import net.requef.reversi.util.ConsoleUtil;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final var inputScanner = new Scanner(System.in);
        final Deque<Screen> screens = new ArrayDeque<>();

        // Menu -> Game(menu.player1, menu.player2).
        // Game -> Menu (after the game is finished).
        // Game -> Menu (by typing q)
        // Menu -> exit (by typing q).

        screens.add(new Menu(inputScanner, screens::add));

        while (!screens.isEmpty()) {
            final Screen currentScreen = screens.getLast();
            ConsoleUtil.clearConsole();
            currentScreen.draw();

            if (currentScreen.isFinished()) {
                System.out.printf("[DEBUG] %s: Screen is finished%n", currentScreen.getClass().getSimpleName());
                screens.pop();
            }
        }
    }
}