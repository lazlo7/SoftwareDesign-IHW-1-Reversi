package net.requef.reversi.app.screen;

import net.requef.reversi.app.Drawable;
import net.requef.reversi.app.screen.ScreenPusher;

import java.util.*;

public abstract class Screen implements Drawable {
    private final Queue<String> logQueue = new LinkedList<>();
    protected final ScreenPusher screenPusher;
    protected final Scanner inputScanner;
    protected boolean shouldExit = false;

    protected Screen(final ScreenPusher screenPusher, final Scanner inputScanner) {
        this.screenPusher = screenPusher;
        this.inputScanner = inputScanner;
    }

    public abstract void onUpdate();
    public boolean shouldExit() {
        return shouldExit;
    }

    protected void log(final String message) {
        logQueue.add(message);
    }

    public void dumpLogs() {
        while (!logQueue.isEmpty()) {
            System.out.println(logQueue.poll());
        }
    }

    public static List<String> splitInput(final String input) {
        return Arrays.stream(input.split(" ")).toList();
    }

    protected boolean checkArgumentNumber(int expected, int found) {
        if (expected != found) {
            log(String.format("Expected %d argument(s), found - %d", expected, found));
            return false;
        }
        return true;
    }
}