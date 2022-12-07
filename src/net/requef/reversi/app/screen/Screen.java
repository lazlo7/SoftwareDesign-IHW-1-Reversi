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

    /**
     * onUpdate() gets called every frame after draw().
     * User interaction and application logic should be implemented here.
     */
    public abstract void onUpdate();

    /**
     * Checks if the screen should be exited.
     * @return true if the screen should be exited, false otherwise
     */
    public boolean shouldExit() {
        return shouldExit;
    }

    protected void log(final String message) {
        logQueue.add(message);
    }

    /**
     * Dumps the entire log queue to the console.
     */
    public void dumpLogs() {
        while (!logQueue.isEmpty()) {
            System.out.println(logQueue.poll());
        }
    }

    /**
     * Splits the input string into a list of arguments using space as a delimiter.
     * @param input the input string
     * @return a list of arguments
     */
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
