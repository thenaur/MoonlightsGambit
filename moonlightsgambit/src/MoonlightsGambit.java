package moonlightsgambit;

import java.util.*;
import moonlightsgambit.characters.*;
import moonlightsgambit.enums.Team;
import moonlightsgambit.interfaces.GamePhase;
import moonlightsgambit.phases.*;
import moonlightsgambit.utils.GameUtils;

// Main game controller managing game state and flow
public class MoonlightsGambit {
    private GameCharacter[] players = new GameCharacter[4];
    private boolean gameOver = false;
    private boolean specialEnding = false;
    private GameCharacter huntTarget;
    private final List<GameCharacter> playersToSabotageNextRound = new ArrayList<>();
    
    // Constants for game configuration
    private static final int MAX_PLAYERS = 4;
    private static final int MIN_PLAYERS_FOR_VOTING = 3;

    // Resets game to initial state
    public void resetGame() {
        this.players = new GameCharacter[MAX_PLAYERS];
        this.gameOver = false;
        this.specialEnding = false;
        this.huntTarget = null;
        this.playersToSabotageNextRound.clear();
    }

    // Starts main game loop and manages phases
    public void startGame() {
        resetGame();
        
        // Array of game phases demonstrating polymorphism through GamePhase interface
        GamePhase[] phases = initializeGamePhases();

        int cycle = 1;
        while (!gameOver) {
            displayCycleHeader(cycle);
            
            List<GameCharacter> alivePlayers = getAlivePlayers();
            if (isFinalTwoScenario(alivePlayers)) {
                handleFinalTwo(alivePlayers);
                break;
            }

            if (cycle > 1) {
                applySabotageForThisRound();
            }

            executeGamePhases(phases, cycle);
            
            if (gameOver) break;
            
            cycle++;
            resetNightActions();
            checkGameEndCondition();
        }

        if (!specialEnding) {
            displayShortFinalResults();
        }
    }

    // Initializes all game phases
    private GamePhase[] initializeGamePhases() {
        return new GamePhase[] { 
            new IntroPhase(), 
            new GameSetup(), 
            new MoonPhase(), 
            new DawnPhase(), 
            new VotingPhase() 
        };
    }

    // Shows cycle header
    private void displayCycleHeader(int cycle) {
        System.out.printf("\n--- CYCLE %d ---%n", cycle);
        System.out.println("*".repeat(50));
    }

    // Executes all game phases for current cycle
    private void executeGamePhases(GamePhase[] phases, int cycle) {
        for (GamePhase phase : phases) {
            if (gameOver) break;
            if (phase == null) continue;
            
            // Skip setup phases after first cycle
            if (shouldSkipPhase(phase, cycle)) continue;
            
            phase.executePhase(this);
            
            // Check for final two condition after each phase
            if (getAlivePlayers().size() == 2 && !gameOver) {
                handleFinalTwo(getAlivePlayers());
                break;
            }
        }
    }

    //Determines if a phase should be skipped based on cycle number
    private boolean shouldSkipPhase(GamePhase phase, int cycle) {
        return cycle > 1 && (phase instanceof GameSetup || phase instanceof IntroPhase);
    }
    
    public GameCharacter getHuntTarget() {
        return huntTarget;
    }

    public void setHuntTarget(GameCharacter huntTarget) {
        this.huntTarget = huntTarget;
    }

    public List<GameCharacter> getAlivePlayers() {
        List<GameCharacter> alive = new ArrayList<>();
        for (GameCharacter player : players) {
            if (player != null && player.isAlive()) {
                alive.add(player);
            }
        }
        return alive;
    }

    public GameCharacter[] getPlayers() { 
        return Arrays.copyOf(players, players.length); // Defensive copy
    }
    
    public void setPlayers(GameCharacter[] players) { 
        if (players == null || players.length != MAX_PLAYERS) {
            throw new IllegalArgumentException("Players array must be non-null and length " + MAX_PLAYERS);
        }
        this.players = Arrays.copyOf(players, players.length); // Defensive copy
    }
    
    public boolean isGameOver() { 
        return gameOver; 
    }
    
    public void setGameOver(boolean gameOver) { 
        this.gameOver = gameOver; 
    }
    
    public void setSpecialEnding(boolean specialEnding) { 
        this.specialEnding = specialEnding; 
    }

    private void applySabotageForThisRound() {
        // Reset all player states
        for (GameCharacter player : players) {
            if (player != null) { 
                player.setAbilityBlocked(false); 
                player.setBlessed(false); 
            }
        }
        
        // Apply sabotage to targeted players
        for (GameCharacter player : playersToSabotageNextRound) {
            if (player != null && player.isAlive()) {
                player.setAbilityBlocked(true);
            }
        }
        playersToSabotageNextRound.clear();
    }

    // Records game actions
    public void recordHunt(GameCharacter target) { 
        this.huntTarget = target; 
    }
    
    public void recordProtection(GameCharacter target) { 
    }
    
    public void recordSabotage(GameCharacter target) {
        playersToSabotageNextRound.add(target); 
    }
    
    public void recordInvestigation(GameCharacter target) { 
    }

    // Processes night actions and determines outcomes
    public GameCharacter processNightActions() {
        if (huntTarget != null && !huntTarget.isBlessed()) {
            return huntTarget;
        }
        return null;
    }

    // Resets night-specific actions
    private void resetNightActions() {
        for (GameCharacter player : players) {
            if (player != null) {
                player.resetNightAction();
            }
        }
        huntTarget = null;
    }

    // Checks if game end conditions are met
    public void checkGameEndCondition() {
        List<GameCharacter> alivePlayers = getAlivePlayers();
        int goodCount = 0, evilCount = 0;
        
        for (GameCharacter player : alivePlayers) {
            if (player.getTeam() == Team.GOOD) {
                goodCount++;
            } else if (player.getTeam() == Team.EVIL) {
                evilCount++;
            }
        }

        if (goodCount == 0 && evilCount > 0) { 
            gameOver = true; 
        } else if (evilCount == 0 && goodCount > 0) { 
            gameOver = true; 
        }
    }

    // Checks if only two players remain
    private boolean isFinalTwoScenario(List<GameCharacter> alivePlayers) {
        return alivePlayers.size() == 2;
    }

    // Handles final two players scenario
    private void handleFinalTwo(List<GameCharacter> lastTwo) {
        GameCharacter player1 = lastTwo.get(0);
        GameCharacter player2 = lastTwo.get(1);

        // If both players are on the same team, that team wins
        if (player1.getTeam() == player2.getTeam()) {
            handleSameTeamVictory(player1, player2);
            return;
        }
        
        // Cross-team final two will be handled by VotingPhase
        gameOver = true;
        specialEnding = true;
    }

    // Handles victory when final two players are on same team
    private void handleSameTeamVictory(GameCharacter player1, GameCharacter player2) {
        if (player1.getTeam() == Team.GOOD) {
            moonlightsgambit.phases.NarrativeEnding.printGoodEnding(player1, player2);
        } else { 
            moonlightsgambit.phases.NarrativeEnding.printEvilEnding(player1, player2);
        }
        gameOver = true;
        specialEnding = true;
    }

    // Shows short final results when game ends normally
    private void displayShortFinalResults() {
        List<GameCharacter> alivePlayers = getAlivePlayers();
        Team winningTeam = determineWinningTeam(alivePlayers);

        displayGameConclusionHeader();
        displayPlayerResults(winningTeam);
        displayVictoryMessage(winningTeam);
        displayFateConclusion();
    }

    // Determines winning team based on alive players
    private Team determineWinningTeam(List<GameCharacter> alivePlayers) {
        if (alivePlayers.isEmpty()) {
            return null;
        }
        return alivePlayers.get(0).getTeam();
    }

    // Shows game conclusion header
    private void displayGameConclusionHeader() {
        GameUtils.clearScreen();
        System.out.println("=".repeat(50));
        System.out.println(GameUtils.GAME_CONCLUDED);
        System.out.println("=".repeat(50));
    }

    // Shows results for all players
    private void displayPlayerResults(Team winningTeam) {
        for (GameCharacter player : players) {
            if (player == null) continue;
            
            boolean victorious = winningTeam != null && player.getTeam() == winningTeam;
            String fate = GameUtils.getFate(victorious);
            String roleDisplay = getRoleNameForDisplay(player);
            
            System.out.printf("""
                              
                              %s as %s    %s%n""", player.getName(), roleDisplay, fate);
        }
    }

    // Shows victory message based on winning team
    private void displayVictoryMessage(Team winningTeam) {
        System.out.println("\n" + "-".repeat(50));
        
        if (winningTeam == null) {
            System.out.println("THE GAME ENDED IN AN UNKNOWN STATE");
        } else {
            switch (winningTeam) {
                case GOOD -> {
                    System.out.println("\n" + GameUtils.LIGHT_PREVAILED);
                    System.out.println("Justice and order are restored to Artemia Academy");
                }
                case EVIL -> {
                    System.out.println("\n" + GameUtils.SHADOWS_TRIUMPHED);
                    System.out.println("Darkness descends upon Artemia Academy");
                }
                default -> System.out.println("THE GAME ENDED IN AN UNKNOWN STATE");
            }
        }
    }

    // Shows fate conclusion message
    private void displayFateConclusion() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(GameUtils.FATE_HAS_SPOKEN);
        System.out.println("=".repeat(50));
    }

    // Gets display name for character roles
    private String getRoleNameForDisplay(GameCharacter player) {
        if (player instanceof Elara) return "Believer";
        if (player instanceof Calisto) return "Seeker";
        if (player instanceof Orion) return "Reaper";
        if (player instanceof Luna) return "Saboteur";
        return "Unknown";
    }
    
    // Additional utility methods for game flow
    public int getAlivePlayerCount() {
        return getAlivePlayers().size();
    }
    
    //Checks if voting phase should be skipped (only 2 players alive)
    public boolean shouldSkipVotingPhase() {
        return getAlivePlayerCount() < MIN_PLAYERS_FOR_VOTING;
    }

      
   // Shows friendship remains message
    public void displayFriendshipRemains() {
        System.out.println("=".repeat(52));
        System.out.println("          FRIENDSHIP SURVIVES THE DARKNESS");
        System.out.println("=".repeat(52));
        System.out.println("\nCalisto and Luna choose friendship over conflict...");
        System.out.println("Their bond remains, though forever changed...");
    }

    // Shows bonds broken message
    public void displayBondsBroken() {
        System.out.println("=".repeat(50));
        System.out.println("            BONDS SHATTER IN THE NIGHT");
        System.out.println("=".repeat(50));
        System.out.println("\nFriendship gives way to duty and conflict...");
        System.out.println("The final choice leads to confrontation...");
    }

    // Shows destiny draw game conclusion
    public void displayDestinyDrawGameConcluded(Team winningTeam) {
        displayGameConclusionHeader();
        displayPlayerResults(winningTeam);
        displayVictoryMessage(winningTeam);
        displayFateConclusion();
    }
}

