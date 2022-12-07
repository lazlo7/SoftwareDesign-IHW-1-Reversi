package net.requef.reversi.util;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleUtil {
    /**
     * Clear the console using the ANSI code.
     */
    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Tries to read a line from the console. Throws NoSuchElementException if the scanner contains no lines.
     * @param inputScanner The scanner to read from.
     * @return the line read from the console
     * @throws NoSuchElementException
     */
    public static String readLine(Scanner inputScanner) throws NoSuchElementException {
        try {
            return inputScanner.nextLine();
        } catch (final NoSuchElementException e) {
            System.err.println("[FATAL ERROR] Input string does not exist (probably ctrl+c was pressed)");
            e.printStackTrace();
            throw e;
        }
    }
}
