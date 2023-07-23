package mealplanner.util;

import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner = new Scanner(System.in);

    public InputHandler() {
    }

    public String getNextString() {
        String input = scanner.nextLine();
        return input != null ? input.trim() : "";
    }

    public int getNextInt() {
        String nextString = getNextString();
        if (nextString.matches("\\d+")) {
            return Integer.parseInt(nextString);
        }
        return 0;
    }
}
