package moonlightsgambit.utils;

import java.io.IOException;
import java.util.Scanner;

// Utility class providing common game functionality
public class GameUtils {
    private GameUtils() {
        throw new IllegalStateException("Utility class - cannot be instantiated");
    }
    
    // Constants for consistent messaging throughout the game - DRY principle
    public static final String FATE_VICTORIOUS = "W, VICTORIOUS";
    public static final String FATE_DEFEATED = "L, DEFEATED";
    public static final String GAME_CONCLUDED = "                  GAME CONCLUDED";
    public static final String LIGHT_PREVAILED = "THE LIGHT HAS PREVAILED";
    public static final String SHADOWS_TRIUMPHED = "THE SHADOWS HAVE TRIUMPHED";
    public static final String FATE_HAS_SPOKEN = "                 FATE HAS SPOKEN!";
    
    // Platform-specific constants
    private static final String WINDOWS_OS = "Windows";
    private static final String WINDOWS_CLEAR_COMMAND = "cmd";
    private static final String WINDOWS_CLEAR_ARG = "/c";
    private static final String WINDOWS_CLEAR_SUBCOMMAND = "cls";
    private static final String UNIX_CLEAR_COMMAND = "clear";
    
    // Gets fate result based on victory condition
    public static String getFate(boolean victorious) {
        return victorious ? FATE_VICTORIOUS : FATE_DEFEATED;
    }
   
    public static void displayPlayerResult(String playerName, String roleName, boolean victorious) {
        String fate = getFate(victorious);
        System.out.printf("%s as %s    %s%n", playerName, roleName, fate);
    }

    // Clears console screen
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains(WINDOWS_OS)) {
                executeClearCommand(WINDOWS_CLEAR_COMMAND, WINDOWS_CLEAR_ARG, WINDOWS_CLEAR_SUBCOMMAND);
            } else {
                executeClearCommand(UNIX_CLEAR_COMMAND);
            }
        } catch (IOException | InterruptedException e) {
            handleClearScreenFailure();
        }
    }

    // Executes clear command
    private static void executeClearCommand(String... command) throws IOException, InterruptedException {
        new ProcessBuilder(command).inheritIO().start().waitFor();
    }

    private static void handleClearScreenFailure() {
        System.out.println("\n".repeat(50));
    }

    // Types text with typewriter effect
    public static void typeText(String text, int delayMs) {
        try {
            printTextWithDelay(text, delayMs);
        } catch (InterruptedException e) {
            handleTypeTextInterruption(text);
        }
    }

    private static void printTextWithDelay(String text, int delayMs) throws InterruptedException {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            System.out.flush();
            Thread.sleep(delayMs);
        }
        System.out.println();
    }

    private static void handleTypeTextInterruption(String text) {
        System.out.println(text);
        Thread.currentThread().interrupt(); // Restore interrupt status
    }

    // Waits for ENTER key press
    public static void ENTERKey() {
        System.out.println("\n--- press enter to continue ---");

        try {
            waitForEnterPress();
            clearInputBuffer();
        } catch (IOException e) {
        }
    }

    private static void waitForEnterPress() throws IOException {
        System.in.read();
    }

    private static void clearInputBuffer() {
        new Scanner(System.in).nextLine();
    }

// Safely reads integer within specified range
    public static int safeReadInt(Scanner scanner, int min, int max, String prompt) {
        while (true) {
            System.out.print(prompt + " ");
            String rawInput = scanner.nextLine().trim();

            if (isInputEmpty(rawInput)) {
                displayInputError("Input cannot be empty – try again.");
                continue;
            }

            try {
                return parseAndValidateInput(rawInput, min, max);
            } catch (NumberFormatException ex) {
                displayInputError("Not a valid number – try again.");
            }
        }
    }

    private static boolean isInputEmpty(String input) {
        return input.isEmpty();
    }

    private static void displayInputError(String message) {
        System.out.println("  " + message);
    }

    private static int parseAndValidateInput(String input, int min, int max) {
        int value = Integer.parseInt(input);
        validateInputRange(value, min, max);
        return value;
    }

    private static void validateInputRange(int value, int min, int max) {
        if (value < min || value > max) {
            throw new NumberFormatException("Value out of range");
        }
    }
}

