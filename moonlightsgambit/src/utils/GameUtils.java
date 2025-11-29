package moonlightsgambit.utils;

import java.io.IOException;
import java.util.Scanner;

public class GameUtils {
    private GameUtils() {
        throw new IllegalStateException("Utility class - cannot be instantiated");
    }
    
    public static final String FATE_VICTORIOUS = "W, VICTORIOUS";
    public static final String FATE_DEFEATED = "L, DEFEATED";
    public static final String GAME_CONCLUDED = "                  GAME CONCLUDED";
    public static final String LIGHT_PREVAILED = "THE LIGHT HAS PREVAILED";
    public static final String SHADOWS_TRIUMPHED = "THE SHADOWS HAVE TRIUMPHED";
    public static final String FATE_HAS_SPOKEN = "                 FATE HAS SPOKEN!";
    
    private static final String WINDOWS_OS = "Windows";
    private static final String WINDOWS_CLEAR_COMMAND = "cmd";
    private static final String WINDOWS_CLEAR_ARG = "/c";
    private static final String WINDOWS_CLEAR_SUBCOMMAND = "cls";
    private static final String UNIX_CLEAR_COMMAND = "clear";
    
    public static String getFate(boolean victorious) {
        return victorious ? FATE_VICTORIOUS : FATE_DEFEATED;
    }
   
    public static void displayPlayerResult(String playerName, String roleName, boolean victorious) {
        String fate = getFate(victorious);
        System.out.printf("%s as %s    %s%n", playerName, roleName, fate);
    }

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains(WINDOWS_OS)) {
                new ProcessBuilder(WINDOWS_CLEAR_COMMAND, WINDOWS_CLEAR_ARG, WINDOWS_CLEAR_SUBCOMMAND).inheritIO().start().waitFor();
            } else {
                new ProcessBuilder(UNIX_CLEAR_COMMAND).inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("\n".repeat(50));
        }
    }

    public static void typeText(String text, int delayMs) {
        try {
            for (char c : text.toCharArray()) {
                System.out.print(c);
                System.out.flush();
                Thread.sleep(delayMs);
            }
            System.out.println();
        } catch (InterruptedException e) {
            System.out.println(text);
            Thread.currentThread().interrupt();
        }
    }

    public static void ENTERKey() {
        System.out.println("\n--- press enter to continue ---");
        try {
            new Scanner(System.in).nextLine();
        } catch (Exception e) {
            // Silent handling
        }
    }

    public static int safeReadInt(Scanner scanner, int min, int max, String prompt) {
        while (true) {
            System.out.print(prompt + " ");
            String rawInput = scanner.nextLine().trim();

            if (rawInput.isEmpty()) {
                System.out.println("  Input cannot be empty - try again.");
                continue;
            }

            try {
                int value = Integer.parseInt(rawInput);
                if (value < min || value > max) {
                    System.out.println("  Please enter a number between " + min + " and " + max);
                    continue;
                }
                return value;
            } catch (NumberFormatException ex) {
                System.out.println("  Not a valid number - try again.");
            }
        }
    }
}
