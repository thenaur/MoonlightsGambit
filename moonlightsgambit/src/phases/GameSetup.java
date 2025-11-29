package moonlightsgambit.phases;

import java.util.*;
import moonlightsgambit.MoonlightsGambit;
import moonlightsgambit.characters.*;
import moonlightsgambit.interfaces.GamePhase;
import moonlightsgambit.utils.GameUtils;

// Manages player setup and role assignment phase
public class GameSetup implements GamePhase {
    private final Scanner scanner;
    private static final int MAX_PLAYERS = 4;
    private static final int MAX_NAME_LENGTH = 15;
    private static final String NAME_REGEX = "^[A-Za-z]+$";

    public GameSetup() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void executePhase(MoonlightsGambit game) {
        displaySetupHeader();
        GameCharacter[] players = initializePlayers();
        game.setPlayers(players);
        displayCompletionMessage();
        game.displayCycleHeader(1);
    }

   // Shows setup phase header
    private void displaySetupHeader() {
        GameUtils.clearScreen();
        System.out.println("*".repeat(50));
        System.out.println("                 ROLE ASSIGNMENT!");
        System.out.println("*".repeat(50));
        GameUtils.ENTERKey();
    }

    // Creates players array with assigned roles
    private GameCharacter[] initializePlayers() {
        GameCharacter[] players = new GameCharacter[MAX_PLAYERS];
        List<String> availableRoles = new ArrayList<>(Arrays.asList("Elara", "Calisto", "Orion", "Luna"));
        Collections.shuffle(availableRoles);

        for (int i = 0; i < MAX_PLAYERS; i++) {
            setupPlayer(players, availableRoles, i);
        }
        
        return players;
    }

    // Sets up individual player with name and role
    private void setupPlayer(GameCharacter[] players, List<String> availableRoles, int playerIndex) {
        GameUtils.clearScreen();
        System.out.printf("Player %d, approach the host...%n", playerIndex + 1);
        
        String playerName = getValidPlayerName(players, playerIndex);
        String role = availableRoles.get(playerIndex);
        
        GameCharacter player = createPlayerWithRole(role, playerName);
        player.setOrder(playerIndex + 1);
        players[playerIndex] = player;

        displayRoleAssignment(player, playerIndex);
    }

    // Validates and gets unique player name
    private String getValidPlayerName(GameCharacter[] players, int currentIndex) {
        while (true) {
            System.out.print("Enter your name: ");
            String name = scanner.nextLine().trim();

            try {
                validatePlayerName(name, players, currentIndex);
                return name;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Validates player name against game rules
    private void validatePlayerName(String name, GameCharacter[] players, int currentIndex) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        
        if (!name.matches(NAME_REGEX)) {
            throw new IllegalArgumentException("Name can only contain letters (A-Z, a-z).");
        }
        
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("Name cannot exceed " + MAX_NAME_LENGTH + " characters.");
        }
        
        // Check for duplicate names (case insensitive)
        for (int i = 0; i < currentIndex; i++) {
            if (players[i] != null && players[i].getName().equalsIgnoreCase(name)) {
                throw new IllegalArgumentException("Name '" + name + "' is already taken.");
            }
        }
    }

    // Shows assigned role information
    private void displayRoleAssignment(GameCharacter player, int playerIndex) {
        GameUtils.clearScreen();
        System.out.println("*".repeat(50));
        System.out.println("                  ROLE ASSIGNED!");
        System.out.println("*".repeat(50));
        System.out.printf("\nPlayer %d: %s%n", playerIndex + 1, player.getName());
        System.out.printf("Role: %s%n", player.getRoleDescription());
        System.out.printf("Lore: %s%n", player.getLoreDescription());
        System.out.printf("Allegiance: %s%n", player.getTeam().getDisplayName());
        System.out.println("\n" + "-".repeat(50));
        System.out.println("Keep this a secret! Move on to the next player...");
        GameUtils.ENTERKey();
    }

    // Creates player instances based on role
    private GameCharacter createPlayerWithRole(String role, String name) {
        return switch (role) {
            case "Elara" -> new Elara(name);
            case "Calisto" -> new Calisto(name);
            case "Orion" -> new Orion(name);
            case "Luna" -> new Luna(name);
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        };
    }

    // Shows completion message after setup
    private void displayCompletionMessage() {
        GameUtils.clearScreen();
        System.out.println("*".repeat(50));
        System.out.println("                ALL ROLES ASSIGNED");
        System.out.println("*".repeat(50));
        System.out.println("\nThe game begins as moonlight falls...");
        GameUtils.ENTERKey();
    }

    @Override
    public String getPhaseName() {
        return "Game Setup";
    }
}
