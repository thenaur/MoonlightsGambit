package moonlightsgambit.phases;

import java.util.List;
import moonlightsgambit.MoonlightsGambit;
import moonlightsgambit.characters.GameCharacter;
import moonlightsgambit.interfaces.GamePhase;
import moonlightsgambit.utils.GameUtils;

// Handles dawn phase where night actions resolve and deaths are announced
public class DawnPhase implements GamePhase {
    
    @Override
    public void executePhase(MoonlightsGambit game) {
        List<GameCharacter> alivePlayers = game.getAlivePlayers();
        // Final two players check
        if (alivePlayers.size() == 2) {
            System.out.println("\n>>> Only 2 players remain! The game will proceed to final showdown...");
            GameUtils.ENTERKey();
            return;
        }

        displayDawnPhaseHeader();
        processNightEvents(game);
        displaySurvivalStatus(game);
        displayRemainingPlayers(game);
        GameUtils.ENTERKey();
    }
    

    // Internal display logic for dawn phase
    private void displayDawnPhaseHeader() {
        GameUtils.clearScreen();
        System.out.println("*".repeat(36));
        System.out.println("             DAWN PHASE");
        System.out.println("*".repeat(36));
        GameUtils.typeText("\nDawn breaks over Artemia Academy...", 40);
        GameUtils.typeText("The night's events are revealed...", 40);
    }
    
    // Processes night actions and determines outcomes
    private void processNightEvents(MoonlightsGambit game) {
        GameCharacter killedPlayer = game.processNightActions();
        
        System.out.println("\n[ANNOUNCEMENT] THE HOST ANNOUNCES:");
        System.out.println("=".repeat(65));
        
        if (killedPlayer != null) {
            handlePlayerDeath(killedPlayer);
        } else {
            handleNoDeaths(game);
        }
    }
    
    // Handles player death scenario
    private void handlePlayerDeath(GameCharacter killedPlayer) {
        System.out.printf("[DEATH] Tragically, %s was found lifeless at dawn!%n", killedPlayer.getName());
        killedPlayer.setAlive(false);
    }
    
    private void handleNoDeaths(MoonlightsGambit game) {
        System.out.println("[SURVIVAL] Miraculously, no one perished in the night!");
        System.out.println("The academy remains unscathed... for now.");
    }
    
    //Displays survival status message
    private void displaySurvivalStatus(MoonlightsGambit game) {
    }
    
    // Shows all remaining players with status
    private void displayRemainingPlayers(MoonlightsGambit game) {
        System.out.println("\nRemaining Champions:");
        for (GameCharacter player : game.getPlayers()) {
            if (player != null) {
                if (player.isAlive()) {
                    System.out.printf("[ALIVE] %s%n", player.getName());
                } else {
                    System.out.printf("[DEAD] %s%n", player.getName());
                }
            }
        }
    }
    
    @Override
    public String getPhaseName() {
        return "Dawn Phase";
    }
}
