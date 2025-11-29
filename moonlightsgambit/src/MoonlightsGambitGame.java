package moonlightsgambit;

import java.io.IOException;
import java.util.Scanner;
import moonlightsgambit.utils.GameUtils;

// Main entry point for the Moonlight's Gambit game
public class MoonlightsGambitGame {    
    public static void main(String[] args) {
        displayGameWelcome();
        
        do {
            runGameSession();
        } while (shouldPlayAgain());
    }
    
    // Shows game welcome message
    private static void displayGameWelcome() {
        System.out.println("\nInitializing Moonlight's Gambit...");
        GameUtils.ENTERKey();
    }
    
    private static void runGameSession() {
        try {
            MoonlightsGambit game = new MoonlightsGambit();
            game.startGame();
        } catch (Exception e) {
            handleGameError(e);
        }
    }
    
    private static void handleGameError(Exception e) {
        System.out.println("\n[ERROR] An unexpected error occurred during the game.");
        System.out.println("The game session will now restart.");
        System.out.println("Error details: " + e.getMessage());
        GameUtils.ENTERKey();
    }

    // Asks the user whether to start a new game.
    private static boolean shouldPlayAgain() {
        GameUtils.ENTERKey();
        GameUtils.clearScreen();
        Scanner sc = new Scanner(System.in);
        System.out.println("~*~*".repeat(23));
        GameUtils.typeText("The story ends... but every ending is a new beginning!", 25);
        int choice = GameUtils.safeReadInt(sc, 1, 2, "Would you like to play again?  (1 - NO , 2 - YES):");
        
        if (choice == 1) {          // 1 = stop
            displayGoodbye();
            return false;
        }
        
        return true;                // 2 = restart
    }

    private static void displayGoodbye() {
        GameUtils.typeText("\nYou did it! Another tale of love and loyalty written in the stars.", 30);
        GameUtils.typeText("Thanks for playing our little moonlit drama, hope you had as much fun as we did making it!", 30);
        GameUtils.typeText("Until next time, players!", 40);
        GameUtils.typeText("~*~*".repeat(23), 8);
    }
}
