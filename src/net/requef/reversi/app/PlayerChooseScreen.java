package net.requef.reversi.app;

import net.requef.reversi.util.ConsoleUtil;

import java.util.Scanner;
import java.util.function.Consumer;

public class PlayerChooseScreen extends Screen {
    private final Consumer<Player> playerSetter;
    private final CellType playerSide;

    public PlayerChooseScreen(final ScreenPusher screenPusher,
                              final Scanner inputScanner,
                              final Consumer<Player> playerSetter,
                              final CellType playerSide) {
        super(screenPusher, inputScanner);
        this.playerSetter = playerSetter;
        this.playerSide = playerSide;
    }

    @Override
    public void draw() {
        System.out.printf("Choose %s player's type: <human/easybot/probot>%n", playerSide);
    }

    @Override
    public void onUpdate() {
        System.out.print("input> ");
        final var input = splitInput(ConsoleUtil.readLine(inputScanner));
        checkArgumentNumber(1, input.size());
        final var type = input.get(0);
        switch (type) {
            case "human" -> setPlayer(new HumanPlayer(playerSide));
            case "easybot" -> setPlayer(new EasyBotPlayer(playerSide));
            case "probot" -> setPlayer(new ProBotPlayer(playerSide));
            default -> log("Unknown player type");
        }
    }

    private void setPlayer(final Player player) {
        log("accepted " + player.getClass().getSimpleName());
        playerSetter.accept(player);
        shouldExit = true;
    }
}
