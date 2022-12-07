package net.requef.reversi.app.screen;

/**
 * Interface for pushing screens to the screen manager.
 */
public interface ScreenPusher {
    /**
     * Pushes a screen to the screen manager.
     * @param screen Screen to push.
     */
    void push(final Screen screen);
}
