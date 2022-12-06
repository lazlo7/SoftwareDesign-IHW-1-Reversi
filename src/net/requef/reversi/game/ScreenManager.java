package net.requef.reversi.game;

import java.util.ArrayDeque;
import java.util.Deque;

public class ScreenManager implements ScreenPusher {
    private final Deque<Screen> screens = new ArrayDeque<>();

    public void pushScreen(final Screen screen) {
        screens.push(screen);
    }

    public Screen popScreen() {
        return screens.pop();
    }

    public Screen peekScreen() {
        return screens.peek();
    }

    public boolean isEmpty() {
        return screens.isEmpty();
    }
}
