package net.requef.reversi.util;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleUtil {
    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

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
