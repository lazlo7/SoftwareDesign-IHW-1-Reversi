package net.requef.reversi.game;

import net.requef.reversi.game.board.CellType;
import net.requef.reversi.game.player.EasyBotPlayer;
import net.requef.reversi.game.player.HumanPlayer;
import net.requef.reversi.game.player.Player;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Consumer;

public class Menu implements Screen {
    private final Scanner inputScanner;
    private final Consumer<Screen> screenAdder;

    private Player player1 = null;
    private Player player2 = null;

    private boolean exiting = false;

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Menu(final Scanner inputScanner, final Consumer<Screen> gameScreenAdder) {
        this.inputScanner = inputScanner;
        this.screenAdder = gameScreenAdder;
    }

    private Player choosePlayerType(CellType playerCellType) {
        String playerType;
        while (true) {
            try {
                playerType = inputScanner.nextLine();
            } catch (NoSuchElementException e) {
                System.out.println("Enter a valid player type: <human>/<bot> ");
                continue;
            } catch (IllegalStateException e) {
                System.out.println("[FATAL ERROR] Scanner is closed");
                e.printStackTrace();
                throw e;
            }

            if (playerType.equals("human")) {
                return new HumanPlayer(inputScanner, playerCellType);
            } else if (playerType.equals("bot")) {
                return new EasyBotPlayer(playerCellType);
            } else {
                System.out.println("Enter a valid player type: <human>/<bot> ");
            }
        }
    }

    private void inputLoop() {
        String input;

        while(true) {
            try {
                input = inputScanner.nextLine();
            } catch (NoSuchElementException e) {
                System.out.println("Enter a valid command: <start>/<exit> ");
                continue;
            } catch (IllegalStateException e) {
                System.out.println("[FATAL ERROR] Scanner is closed");
                e.printStackTrace();
                throw e;
            }

            if (input.equals("start")) {
                startGameInput();
                return;
            } else if (input.equals("exit")) {
                exiting = true;
                return;
            } else {
                System.out.println("Enter a valid command: <start>/<exit> ");
            }
        }
    }

    private void startGameInput() {
        System.out.println("Choose Black Player type: <human>/<bot> ");
        player1 = choosePlayerType(CellType.BLACK);
        System.out.println("Choose White Player type: <human>/<bot> ");
        player2 = choosePlayerType(CellType.WHITE);
        screenAdder.accept(new Game(player1, player2));
    }

    @Override
    public void draw() {
        // Draw game banner.
        System.out.println("""
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
                """);

        System.out.println("Type \"start\" to start a new game or \"exit\" to exit the application");
        // Start the input loop right after drawing the banner.
        inputLoop();
    }

    @Override
    public boolean isFinished() {
        return exiting;
    }
}
