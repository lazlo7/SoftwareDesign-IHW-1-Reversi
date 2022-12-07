package net.requef.reversi.app;

import java.util.Deque;
import java.util.LinkedList;

public class ScreenManager implements ScreenPusher {
    private final Deque<Screen> screens = new LinkedList<>();

    @Override
    public void push(final Screen screen) {
        screens.addLast(screen);
    }

    public void pop() {
        screens.removeLast();
    }

    public Screen peek() {
        return screens.peekLast();
    }

    public boolean isEmpty() {
        return screens.isEmpty();
    }
}
