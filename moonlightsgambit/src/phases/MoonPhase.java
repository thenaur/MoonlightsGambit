package moonlightsgambit.phases;

import java.util.*;
import moonlightsgambit.MoonlightsGambit;
import moonlightsgambit.characters.*;
import moonlightsgambit.interfaces.GamePhase;
import moonlightsgambit.utils.GameUtils;

// Handles moon phase where players perform night actions
public class MoonPhase implements GamePhase {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void executePhase(MoonlightsGambit game) {
        displayMoonPhaseHeader();
        executePlayerActions(game);
        displayPhaseConclusion();
    }

    // Shows moon phase header
    private void displayMoonPhaseHeader() {
        GameUtils.clearScreen();
        System.out.println("*".repeat(44));
        System.out.println("                 MOON PHASE");
        System.out.println("*".repeat(44));
        GameUtils.typeText("\nMoonlight descends upon Artemia Academy...", 40);
        GameUtils.typeText("Hidden powers awaken in the shadows...", 40);
        GameUtils.ENTERKey();
    }

    // Executes actions for all alive players
    private void executePlayerActions(MoonlightsGambit game) {
        for (GameCharacter player : game.getPlayers()) {
            if (player == null || !player.isAlive()) continue;

            executeSinglePlayerAction(player, game);
        }
    }

    // Executes action for single player
    private void executeSinglePlayerAction(GameCharacter player, MoonlightsGambit game) {
        GameUtils.clearScreen();
        displayPlayerTurnInfo(player);

        System.out.println(player.getLoreDescription());

        List<GameCharacter> targets = buildTargetList(player, game);
        printTargets(player, targets);

        int choice = readTargetChoice(player, targets);
        GameCharacter target = targets.get(choice);  
        player.performAction(target, game); // POLYMORPHIC CALL

        System.out.println("\nAction recorded...");
        GameUtils.ENTERKey();
    }

    // Shows player turn information
    private void displayPlayerTurnInfo(GameCharacter player) {
        System.out.printf("%s's turn - %s%n", player.getName(), player.getRoleDescription());
    }

    // Builds list of valid targets for player
    private List<GameCharacter> buildTargetList(GameCharacter actor, MoonlightsGambit game) {
        List<GameCharacter> out = new ArrayList<>();
        if (actor instanceof Elara) out.add(actor);  // Elara can target herself

        for (GameCharacter p : game.getPlayers()) {
            if (p != null && p.isAlive() && p != actor) out.add(p);
        }

        return out;
    }

    // Prints available targets for selection
    private void printTargets(GameCharacter actor, List<GameCharacter> list) {
        System.out.println("\nAlive players to target:");
        int number = (actor instanceof Elara) ? 0 : 1; 
        
        for (GameCharacter t : list) {
            System.out.printf("%d. %s%n", number++, t.getName());
        }
    }

    // Reads and validates target choice
    private int readTargetChoice(GameCharacter actor, List<GameCharacter> list) {
        if (actor == null) return 0;
        int min = (actor instanceof Elara) ? 0 : 1;
        int max = (actor instanceof Elara) ? list.size() - 1 : list.size();
        return readInt(min, max, actor.getActionPrompt())
             - (actor instanceof Elara ? 0 : 1);    
    }

    // Safely reads integer input
    private int readInt(int min, int max, String prompt) {
        return GameUtils.safeReadInt(scanner, min, max, prompt);
    }

    // Shows phase conclusion
    private void displayPhaseConclusion() {
        GameUtils.clearScreen();
        GameUtils.typeText("The night's secrets are sealed...", 30);
        GameUtils.ENTERKey();
    }

    @Override
    public String getPhaseName() {
        return "Moon Phase";
    }
}
