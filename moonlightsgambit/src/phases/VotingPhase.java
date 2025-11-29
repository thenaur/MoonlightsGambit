package moonlightsgambit.phases;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import moonlightsgambit.MoonlightsGambit;
import moonlightsgambit.characters.Calisto;
import moonlightsgambit.characters.Elara;
import moonlightsgambit.characters.GameCharacter;
import moonlightsgambit.characters.Luna;
import moonlightsgambit.characters.Orion;
import moonlightsgambit.enums.Team;
import moonlightsgambit.interfaces.GamePhase;
import moonlightsgambit.utils.DestinyDraw;
import moonlightsgambit.utils.GameUtils;

// Handles voting phase where players vote to eliminate others
public class VotingPhase implements GamePhase {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void executePhase(MoonlightsGambit game) {
        List<GameCharacter> alivePlayers = game.getAlivePlayers();

        // Handle final two players scenario
        if (alivePlayers.size() == 2) {
            handleFinalTwoPlayers(game, alivePlayers);
            return;
        }

        displayVotingPhaseHeader();
        Map<GameCharacter, Integer> votes = initializeVotes(alivePlayers);
        collectVotes(alivePlayers, votes);
        processVotingResults(game, votes, alivePlayers);
    }

    // Shows voting phase header
    private void displayVotingPhaseHeader() {
        GameUtils.clearScreen();
        System.out.println("*".repeat(40));
        System.out.println("           VOTING PHASE");
        System.out.println("*".repeat(40));
        GameUtils.typeText("\nThe academy gathers to cast their votes.", 30);
        GameUtils.typeText("Each member must accuse one among them to be banished.", 30);
        GameUtils.ENTERKey();
    }

    // Initializes vote counts for alive players
    private Map<GameCharacter, Integer> initializeVotes(List<GameCharacter> alivePlayers) {
        Map<GameCharacter, Integer> votes = new HashMap<>();
        for (GameCharacter player : alivePlayers) {
            votes.put(player, 0);
        }
        return votes;
    }

    // Collects votes from all alive players
    private void collectVotes(List<GameCharacter> alivePlayers, Map<GameCharacter, Integer> votes) {
        for (GameCharacter voter : alivePlayers) {
            collectSingleVote(voter, alivePlayers, votes);
        }
    }

    // Collects single player's vote
    private void collectSingleVote(GameCharacter voter, List<GameCharacter> alivePlayers, Map<GameCharacter, Integer> votes) {
        GameUtils.clearScreen();
        System.out.println(voter.getName() + ", your turn to accuse:");
        displayAlivePlayers(voter, alivePlayers);

        GameCharacter target = getValidVoteTarget(voter, alivePlayers);
        votes.put(target, votes.getOrDefault(target, 0) + 1);
        System.out.println("Vote recorded.");
        GameUtils.ENTERKey();
    }

    // Shows list of alive players for voting
    private void displayAlivePlayers(GameCharacter voter, List<GameCharacter> alivePlayers) {
        System.out.println("Alive players:");
        int index = 1;
        for (GameCharacter candidate : alivePlayers) {
            if (candidate == voter) {
                System.out.printf("%d. %s (You)%n", index++, candidate.getName());
            } else {
                System.out.printf("%d. %s%n", index++, candidate.getName());
            }
        }
    }

    // Gets valid vote target with input validation
    private GameCharacter getValidVoteTarget(GameCharacter voter, List<GameCharacter> alivePlayers) {
        int pick;
        do {
            pick = readInt(1, alivePlayers.size(), "Choose a player to vote (enter number): ");
            if (alivePlayers.get(pick - 1) == voter) {
                System.out.println("  You cannot vote for yourself, please choose someone else!");
            }
        } while (alivePlayers.get(pick - 1) == voter);
        
        return alivePlayers.get(pick - 1);
    }

    // Processes voting results and determines elimination
    private void processVotingResults(MoonlightsGambit game, Map<GameCharacter, Integer> votes, List<GameCharacter> alivePlayers) {
        GameUtils.clearScreen();
        System.out.println("*".repeat(50));
        System.out.println("                 VOTING RESULTS");
        System.out.println("*".repeat(50));

        GameCharacter eliminated = determineEliminated(votes, alivePlayers);

        if (eliminated != null) {
            handlePlayerElimination(game, eliminated);
        } else {
            handleTieScenario();
        }

        game.checkGameEndCondition();
    }

    // Handles player elimination
    private void handlePlayerElimination(MoonlightsGambit game, GameCharacter eliminated) {
        System.out.printf("\n[ELIMINATED] The academy has spoken! %s is banished!%n", eliminated.getName());
        eliminated.setAlive(false);
        GameUtils.ENTERKey();

        // Check if elimination creates final two scenario
        if (game.getAlivePlayers().size() == 2) {
            handleFinalTwoPlayers(game, game.getAlivePlayers());
        }
    }

    // Handles tie scenario
    private void handleTieScenario() {
        System.out.println("\n[TIE] The vote ends in a stalemate!");
        System.out.println("   No one is eliminated this round...");
        System.out.println("   The game continues with current players!");
    }

    // Determines which player is eliminated based on votes
private GameCharacter determineEliminated(Map<GameCharacter, Integer> votes, List<GameCharacter> alivePlayers) {
    int maxVotes = 0;
    GameCharacter eliminated = null;
    boolean isTie = false;

    System.out.println("Vote Count:");

    for (GameCharacter player : alivePlayers) {
        int playerVotes = votes.getOrDefault(player, 0);
        System.out.printf("  %s: %d votes%n", player.getName(), playerVotes);

        if (playerVotes > maxVotes) {
            maxVotes = playerVotes;
            eliminated = player;
            isTie = false;
        } else if (playerVotes == maxVotes && maxVotes > 0) {
            isTie = true;
        }
    }

    return (isTie || maxVotes == 0) ? null : eliminated;
}

    /* ========== FINAL TWO PLAYERS HANDLING ========== */

    // Handles final two players scenario
    private void handleFinalTwoPlayers(MoonlightsGambit game, List<GameCharacter> finalTwo) {
        GameCharacter player1 = finalTwo.get(0);
        GameCharacter player2 = finalTwo.get(1);

        // EXCEPTION HANDLING: Null safety check
        if (player1 == null || player2 == null) {
            return;
        }

        // Check for special relationships
        if (areLovers(player1, player2)) {
            loversDilemma(game, player1, player2);
            return;
        }
        
        if (areFriends(player1, player2)) {
            friendsCrossroads(game, player1, player2);
            return;
        }

        // Check if both players are from the same team
        if (player1.getTeam() == player2.getTeam()) {
            handleSameTeamVictory(game, player1, player2);
            return;
        }

        // Default: cross-team duel
        crossTeamDuel(game, player1, player2);
    }

    // Checks if players are lovers (Elara and Orion)
    private boolean areLovers(GameCharacter player1, GameCharacter player2) {
        return (player1 instanceof Elara && player2 instanceof Orion) || 
               (player1 instanceof Orion && player2 instanceof Elara);
    }

    // Checks if players are friends (Calisto and Luna)
    private boolean areFriends(GameCharacter player1, GameCharacter player2) {
        return (player1 instanceof Calisto && player2 instanceof Luna) || 
               (player1 instanceof Luna && player2 instanceof Calisto);
    }

    // Handles same team victory
    private void handleSameTeamVictory(MoonlightsGambit game, GameCharacter player1, GameCharacter player2) {
        if (player1.getTeam() == Team.GOOD) {
            NarrativeEnding.printGoodEnding(player1, player2);
            GameUtils.ENTERKey();
        } else {
            NarrativeEnding.printEvilEnding(player1, player2);
            GameUtils.ENTERKey();
        }
        game.setGameOver(true);
    }

    /* ---------- LOVERS DILEMMA SCENARIO ---------- */

    // Handles lovers dilemma scenario
    private void loversDilemma(MoonlightsGambit game, GameCharacter player1, GameCharacter player2) {
        GameCharacter elara = player1 instanceof Elara ? player1 : player2;
        GameCharacter orion = player1 instanceof Orion ? player1 : player2;

        displayLoversIntroduction();
        int elaraChoice = getElaraChoice();
        int orionChoice = getOrionChoice();
        processLoversChoice(game, player1, player2, elara, orion, elaraChoice, orionChoice);
    }

    // Shows lovers dilemma introduction
    private void displayLoversIntroduction() {
        GameUtils.clearScreen();
        System.out.println("-".repeat(76));
        System.out.println("        	 	     THE LOVERS DILEMMA");
        System.out.println("-".repeat(76));
        GameUtils.typeText("\nOnly two souls remain in the moonlit academy...", 30);
        GameUtils.typeText("Elara and Orion stand facing each other, the weight of their hidden truths", 30);
        GameUtils.typeText("finally revealed. The bonds of love clash with the call of duty.", 30);
        GameUtils.ENTERKey();
    }

    // Gets Elara's choice in lovers dilemma
    private int getElaraChoice() {
        GameUtils.clearScreen();
        System.out.println("-".repeat(70));
        GameUtils.typeText("Elara's eyes meet Orion's across the shattered hall.", 30);
        GameUtils.typeText("The man you love stands opposed to your beliefs.", 30);
        GameUtils.typeText("\nDo you choose to follow your heart or protect your destiny?", 30);
        int choice = readInt(1, 2, """
                                   1. Chaos - Fight for your cause, even if it destroys your love
                                   2. Peace - Choose love over war, embrace the person behind the role
                                   Choice (1 or 2):""");
        
        System.out.println("\nAction recorded...");
        GameUtils.ENTERKey();
        return choice;
    }

    // Gets Orion's choice in lovers dilemma
    private int getOrionChoice() {
        GameUtils.clearScreen();
        System.out.println("-".repeat(85));
        GameUtils.typeText("Orion, the woman you swore you'd choose over anything stands as your final obstacle.", 30);
        GameUtils.typeText("Every memory you share wars with the duty you serve.", 30);
        GameUtils.typeText("\nWhat will you sacrifice for victory?", 30);
        int choice = readInt(1, 2, """
                                   1. Chaos - Complete your mission, no matter the cost
                                   2. Peace - Lay down your blade for the love you found
                                   Choice (1 or 2):""");
        
        System.out.println("\nAction recorded...");
        GameUtils.ENTERKey();
        return choice;
    }

    // Processes lovers' choices and determines outcome
    private void processLoversChoice(MoonlightsGambit game, GameCharacter player1, GameCharacter player2, 
                                   GameCharacter elara, GameCharacter orion, int elaraChoice, int orionChoice) {
        GameUtils.clearScreen();
        if (elaraChoice == 2 && orionChoice == 2) {
            handleLoversVictory(game, elara, orion);
        } else {
            handleLoversDefeat(game, player1, player2);
        }
    }

    // Handles lovers choosing each other (victory)
    private void handleLoversVictory(MoonlightsGambit game, GameCharacter elara, GameCharacter orion) {
        GameUtils.clearScreen();
        System.out.println("=".repeat(74));
        System.out.println("                        LOVE TRIUMPHS OVER DESTINY");
        System.out.println("=".repeat(74));
        System.out.println("Elara and Orion choose each other, defying fate and the academy's rules.");
        elara.setAlive(true);
        orion.setAlive(true);
        GameUtils.ENTERKey();
        NarrativeEnding.displayLoversVictory(game, elara, orion);
        game.setGameOver(true);
        game.setSpecialEnding(true);
    }

    // Handles lovers choosing duty over love
    private void handleLoversDefeat(MoonlightsGambit game, GameCharacter player1, GameCharacter player2) {
        GameUtils.clearScreen();
        System.out.println("-".repeat(85));
        GameUtils.typeText("A rift tears through the moonlit hall as love shatters under the weight of destiny.", 30);
        GameUtils.typeText("The academy witnesses a heartbreak that echoes through eternity.", 30);
        GameUtils.ENTERKey();
        crossTeamDuel(game, player1, player2);
    }

    /* ---------- FRIENDS CROSSROADS SCENARIO ---------- */

    // Handles friends crossroads scenario
    private void friendsCrossroads(MoonlightsGambit game, GameCharacter player1, GameCharacter player2) {
        GameCharacter calisto = player1 instanceof Calisto ? player1 : player2;
        GameCharacter luna = player1 instanceof Luna ? player1 : player2;

        displayFriendsIntroduction();
        int calistoChoice = getCalistoChoice();
        int lunaChoice = getLunaChoice();
        processFriendsChoice(game, player1, player2, calisto, luna, calistoChoice, lunaChoice);
    }

    // Shows friends crossroads introduction
    private void displayFriendsIntroduction() {
        GameUtils.clearScreen();
        System.out.println("-".repeat(61));
        System.out.println("                   THE FRIENDS' CROSSROADS");
        System.out.println("-".repeat(61));
        GameUtils.typeText("\nOnly two souls remain in the moonlit academy...", 30);
        GameUtils.typeText("Calisto and Luna stand amidst the ruins of their friendship,", 30);
        GameUtils.typeText("childhood bonds shattered by opposing loyalties.", 30);
        GameUtils.ENTERKey();
    }

    // Gets Calisto's choice
    private int getCalistoChoice() {
        GameUtils.clearScreen();
        System.out.println("-".repeat(70));
        GameUtils.typeText("Calisto, you face the friend who walked beside you since childhood.", 30);
        GameUtils.typeText("The one who shared your dreams now stands as your enemy.", 30);
        GameUtils.typeText("\nDo you hold to justice or to memory?", 30);
        int choice = readInt(1, 2, """
                                   1. Chaos - Uphold the truth, even if it breaks your oldest bond
                                   2. Peace - Choose friendship over duty, forgive the betrayal
                                   Choice (1 or 2):""");
        
        System.out.println("\nAction recorded...");
        GameUtils.ENTERKey();
        return choice;
    }

    // Gets Luna's choice
    private int getLunaChoice() {
        GameUtils.clearScreen();
        System.out.println("-".repeat(70));
        GameUtils.typeText("Luna, your dearest friend has discovered the chaos you kept buried.", 30);
        GameUtils.typeText("The havoc you wreaked has led to this final moment.", 30);
        GameUtils.typeText("\nWhat matters more – your cause or your companion?", 30);
        int choice = readInt(1, 2, """
                                   1. Chaos - See your mission through, no turning back now
                                   2. Peace - Remember what you were before the shadows called
                                   Choice (1 or 2):""");
        
        System.out.println("\nAction recorded...");
        GameUtils.ENTERKey();
        return choice;
    }

    // Processes friends' choices
    private void processFriendsChoice(MoonlightsGambit game, GameCharacter player1, GameCharacter player2,
                                    GameCharacter calisto, GameCharacter luna, int calistoChoice, int lunaChoice) {
        GameUtils.clearScreen();
        if (calistoChoice == 2 && lunaChoice == 2) {
            handleFriendsVictory(game, calisto, luna);
        } else {
            handleFriendsDefeat(game, player1, player2);
        }
    }

    // Handles friends choosing each other
    private void handleFriendsVictory(MoonlightsGambit game, GameCharacter calisto, GameCharacter luna) {
        game.displayFriendshipRemains();
        luna.setAlive(true);
        calisto.setAlive(true);
        GameUtils.ENTERKey();
        NarrativeEnding.displayFriendsVictory(game, calisto, luna);
        game.setGameOver(true);
        game.setSpecialEnding(true);
    }

    // Handles friends choosing duty over friendship
    private void handleFriendsDefeat(MoonlightsGambit game, GameCharacter player1, GameCharacter player2) {
        game.displayBondsBroken();
        GameUtils.ENTERKey();
        crossTeamDuel(game, player1, player2);
    }

    /* ---------- CROSS-TEAM DUEL ---------- */

    // Handles cross-team duel between final two players
    private void crossTeamDuel(MoonlightsGambit game, GameCharacter player1, GameCharacter player2) {
        displayDuelIntroduction(player1, player2);
        
        DestinyDraw duel = new DestinyDraw(player1, player2);
        GameCharacter winner = duel.playGame();
        GameCharacter loser = (winner == player1) ? player2 : player1;
        
        displayEpilogue(winner, loser);
        GameUtils.ENTERKey();

        game.displayDestinyDrawGameConcluded(winner.getTeam());
        game.setGameOver(true);
        game.setSpecialEnding(true);
    }

    // Shows duel introduction
    private void displayDuelIntroduction(GameCharacter player1, GameCharacter player2) {
        GameUtils.clearScreen();
        System.out.println(">>> CROSS-TEAM SHOWDOWN! <<<\n");
        System.out.printf("%s (%s) vs %s (%s)%n",
                player1.getRoleName(), getTeamDisplay(player1.getTeam()),
                player2.getRoleName(), getTeamDisplay(player2.getTeam()));
        GameUtils.typeText("Destiny must decide the academy's fate...", 40);
        GameUtils.ENTERKey();
    }

    // Shows epilogue based on duel outcome
    private void displayEpilogue(GameCharacter winner, GameCharacter loser) {
        GameUtils.clearScreen();
        System.out.println("=".repeat(50));
        System.out.println("                     EPILOGUE");
        System.out.println("=".repeat(50));
        
        if (winner.getTeam() == Team.GOOD) { 
            NarrativeEnding.printGoodEnding(winner, loser);
        } else { 
            NarrativeEnding.printEvilEnding(winner, loser);
        }
    }

    /* ---------- UTILITY METHODS ---------- */

    // Safely reads integer input
    private int readInt(int min, int max, String prompt) {
        return GameUtils.safeReadInt(scanner, min, max, prompt);
    }

    // Gets display name for team
    private String getTeamDisplay(Team team) {
        return team == Team.GOOD ? "The Light" : "The Shadows";
    }

    @Override
    public String getPhaseName() {
        return "Voting Phase";
    }
}
