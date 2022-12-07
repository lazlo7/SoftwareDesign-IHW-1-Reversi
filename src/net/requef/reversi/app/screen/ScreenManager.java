package net.requef.reversi.app.screen;

import java.util.Deque;
import java.util.LinkedList;

public class ScreenManager implements ScreenPusher {
    private final Deque<Screen> screens = new LinkedList<>();

    @Override
    public void push(final Screen screen) {
        screens.addLast(screen);
    }

    /**
     * Pops the top of the screen stack.
     */
    public void pop() {
        screens.removeLast();
    }

    /**
     * Peeks into the top of the screen stack.
     * @return the top of the screen stack.
     */
    public Screen peek() {
        return screens.peekLast();
    }

    /**
     * Checks if the screen stack is empty.
     * @return true if the screen stack is empty, false otherwise.
     */
    public boolean isEmpty() {
        return screens.isEmpty();
    }
}
