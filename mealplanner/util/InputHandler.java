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
}
