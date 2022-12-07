package net.requef.reversi.app;

import net.requef.reversi.app.screen.MenuScreen;
import net.requef.reversi.app.screen.ScreenManager;
import net.requef.reversi.util.ConsoleUtil;

import java.util.Scanner;

public class Application {
    public void run() {
        final var inputScanner = new Scanner(System.in);
        final var screenManager = new ScreenManager();

        screenManager.push(new MenuScreen(screenManager, inputScanner));

        while (!screenManager.isEmpty()) {
            if (screenManager.peek().shouldExit()) {
                screenManager.pop();
                continue;
            }

            final var screen = screenManager.peek();

            ConsoleUtil.clearConsole();
            screen.draw();
            screen.dumpLogs();
            screen.onUpdate();
        }
    }
}
