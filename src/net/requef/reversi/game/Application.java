package net.requef.reversi.game;

import java.util.Scanner;

public class Application {
    private GameSettings gameSettings = GameSettings.DEFAULT;

    public void run() {
        final var inputScanner = new Scanner(System.in);
        final var screenManager = new ScreenManager();

        screenManager.pushScreen(new Menu(inputScanner, screenManager::pushScreen));
    }
}
