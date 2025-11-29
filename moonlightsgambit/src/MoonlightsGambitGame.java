package moonlightsgambit;

import java.io.IOException;
import moonlightsgambit.utils.GameUtils;

// Main entry point for the Moonlight's Gambit game
public class MoonlightsGambitGame {
    
    public static void main(String[] args) {
        displayGameWelcome();
        
        do {
            runGameSession();
        } while (shouldPlayAgain());
        
        displayGameExit();
    }
    
    // Shows game welcome message
    private static void displayGameWelcome() {
        System.out.println("\nInitializing Moonlight's Gambit...");
        GameUtils.ENTERKey();
    }
    
    // Runs single game session
    private static void runGameSession() {
        try {
            MoonlightsGambit game = new MoonlightsGambit();
            game.startGame();
        } catch (Exception e) {
            handleGameError(e);
        }
        
        displayPlayAgainPrompt();
    }
    
    // Handles game errors
    private static void handleGameError(Exception e) {
        System.out.println("\n[ERROR] An unexpected error occurred during the game.");
        System.out.println("The game session will now restart.");
        System.out.println("Error details: " + e.getMessage());
        GameUtils.ENTERKey();
    }
    
    // Shows play again prompt
    private static void displayPlayAgainPrompt() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           Press ENTER to play again...");
        System.out.println("=".repeat(50));
    }
    
    // Checks if user wants to play again
    private static boolean shouldPlayAgain() {
        try {
            // Wait for ENTER key press
            System.in.read();
            // Clear the input buffer
            new java.util.Scanner(System.in).nextLine();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    // Validates if phase can execute
    private static void displayGameExit() {
        System.out.println("\nThank you for playing Moonlight's Gambit!");
    }
}


