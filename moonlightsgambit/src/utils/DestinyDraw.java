package moonlightsgambit.utils;

import java.util.*;
import moonlightsgambit.characters.GameCharacter;
import moonlightsgambit.enums.Team;

// Handles Destiny Draw mini-game for final duels
public class DestinyDraw {
    private final GameCharacter player1;
    private final GameCharacter player2;
    private int player1Score = 0;
    private int player2Score = 0;
    private final Scanner scanner = new Scanner(System.in);
    private final Random random = new Random();
    
    private static final int WINNING_SCORE = 5;
    private static final int MAX_CARD_VALUE = 10;
    private static final int MIN_CARD_VALUE = 1;
    private static final int GAMBLE_BONUS_POINTS = 2;
    private static final int TIE_BONUS_POINTS = 1;

    public DestinyDraw(GameCharacter player1, GameCharacter player2) {
        validatePlayers(player1, player2);
        this.player1 = player1;
        this.player2 = player2;
    }

    // Validates players are valid and distinct
    private void validatePlayers(GameCharacter player1, GameCharacter player2) {
        if (player1 == null || player2 == null) {
            throw new IllegalArgumentException("Both players must be non-null");
        }
        if (player1 == player2) {
            throw new IllegalArgumentException("Players must be different characters");
        }
    }

    // Executes the Destiny Draw mini-game
    public GameCharacter playGame() {
        try {
            displayGameIntroduction();
            int round = 1;
            
            // Game loop - continues until one player reaches winning score
            while (player1Score < WINNING_SCORE && player2Score < WINNING_SCORE) {
                System.out.printf("\n--- Round %d ---%n", round++);
                playRound();

                if (player1Score >= WINNING_SCORE || player2Score >= WINNING_SCORE) {
                    break;
                }

                // Use safe input check instead of direct scanner.nextLine()
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
                GameUtils.clearScreen();
            }

            return displayFinalResults();
            
        } catch (Exception e) {
            System.out.println("\n[ERROR] Destiny Draw encountered an issue. Defaulting to first player as winner.");
            return player1;
        }
    }

    // Shows game introduction
    private void displayGameIntroduction() {
        GameUtils.clearScreen();
        System.out.println("*".repeat(69));
        System.out.println("                       DESTINY DRAW CHALLENGE!");
        System.out.println("*".repeat(69));
        
        System.out.printf("\n%s (%s) vs %s (%s)%n", 
            player1.getName(), getTeamDisplay(player1.getTeam()), 
            player2.getName(), getTeamDisplay(player2.getTeam()));
        
        GameUtils.typeText("Fate shuffles her cards... First to get to " + WINNING_SCORE + " points claims victory!", 40);
        GameUtils.ENTERKey();
        GameUtils.clearScreen();
    }

    // Plays single round
    private void playRound() {
        try {
            int player1Draw = drawCard();
            int player2Draw = drawCard();

            GameUtils.clearScreen();
            System.out.printf("%s's turn (%s)...%n", player1.getName(), getTeamDisplay(player1.getTeam()));
            System.out.printf("You draw a card... Your number is: %d%n", player1Draw);
            boolean player1Keep = askPlayer(player1, player1Draw);

            GameUtils.clearScreen();
            System.out.printf("%s's turn (%s)...%n", player2.getName(), getTeamDisplay(player2.getTeam()));
            System.out.printf("You draw a card... Your number is: %d%n", player2Draw);
            boolean player2Keep = askPlayer(player2, player2Draw);

            displayRoundResults(player1Draw, player2Draw, player1Keep, player2Keep);
            resolveRound(player1Draw, player2Draw, player1Keep, player2Keep);
            displayScoreUpdate();
            
        } catch (Exception e) {
            // EXCEPTION HANDLING: Continue game even if round fails
            System.out.println("\n[ERROR] Round encountered an issue. Continuing...");
        }
    }

    // Draws random card value
    private int drawCard() {
        return random.nextInt(MAX_CARD_VALUE) + MIN_CARD_VALUE;
    }

    // Asks player whether to keep or redraw
    private boolean askPlayer(GameCharacter player, int draw) {
        try {
            System.out.printf("\n%s, you drew %d.\n", player.getName(), draw);
            System.out.println("1. KEEP this fate | 2. RISK a redraw");
            int choice = GameUtils.safeReadInt(scanner, 1, 2, "Choose (1 or 2): ");
            GameUtils.ENTERKey();
            return choice == 1;
        } catch (Exception e) {
            System.out.println("[ERROR] Defaulting to KEEP choice.");
            return true;
        }
    }

    // Shows round results
    private void displayRoundResults(int player1Draw, int player2Draw, boolean player1Keep, boolean player2Keep) {
        GameUtils.clearScreen();
        System.out.println("* ROUND RESULTS *");
        System.out.printf("%s drew: %d * chose to %s%n", 
            player1.getName().toUpperCase(), player1Draw, player1Keep ? "KEEP" : "REDRAW");
        System.out.printf("%s drew: %d * chose to %s%n", 
            player2.getName().toUpperCase(), player2Draw, player2Keep ? "KEEP" : "REDRAW");
        System.out.println("\n" + "-".repeat(50));
    }

    // Resolves round based on player choices
    private void resolveRound(int player1Draw, int player2Draw, boolean player1Keep, boolean player2Keep) {
        if (player1Keep && player2Keep) {
            handleBothKeep(player1Draw, player2Draw);
        } else if (!player1Keep && !player2Keep) {
            handleBothRedraw(player1Draw, player2Draw);
        } else {
            handleMixedChoice(player1Keep, player1Draw, player2Draw);
        }
    }

    // Handles both players keeping cards
    private void handleBothKeep(int player1Draw, int player2Draw) {
    System.out.println("Both choose to trust fate...");

    if (player1Draw > player2Draw) {
        awardRoundWin(player1);  // Remove the draw values
    } else if (player2Draw > player1Draw) {
        awardRoundWin(player2);  // Remove the draw values
    } else {
        handleTie();
    }
}

    // Handles both players redrawing
    private void handleBothRedraw(int player1Original, int player2Original) {
        System.out.println("Both dare to challenge fate...");
        int player1New = drawCard();
        int player2New = drawCard();
        
        System.out.printf("%s: %d -> %d%n", player1.getName().toUpperCase(), player1Original, player1New);
        System.out.printf("%s: %d -> %d%n", player2.getName().toUpperCase(), player2Original, player2New);

        if (player1New > player2New) {
            awardGambleSuccess(player1);
        } else if (player2New > player1New) {
            awardGambleSuccess(player2);
        } else {
            handleTie();
        }
    }

    // Handles mixed choices
    private void handleMixedChoice(boolean player1Keep, int player1Draw, int player2Draw) {
        GameCharacter keeper = player1Keep ? player1 : player2;
        GameCharacter gambler = player1Keep ? player2 : player1;
        int keepValue = player1Keep ? player1Draw : player2Draw;
        int gamblerOriginal = player1Keep ? player2Draw : player1Draw;

        System.out.printf("%s challenges fate...%n", gambler.getName().toUpperCase());
        System.out.printf("%d -> ", gamblerOriginal);
        
        int newDraw = drawCard();
        System.out.printf("%d%n", newDraw);

        if (newDraw > keepValue) {
            awardGambleBonus(gambler);
        } else if (newDraw < keepValue) {
            awardGambleBonus(keeper);
        } else {
            awardTieBonus(keeper);
        }
    }

    // Awards points for round win
    private void awardRoundWin(GameCharacter winner) {  // Remove winnerDraw and loserDraw parameters
    System.out.printf("\n~~ %s WINS THE ROUND%n", winner.getName().toUpperCase());
    System.out.println("+1 point to " + winner.getName());
    incrementScore(winner);
}

    // Awards points for successful gamble
    private void awardGambleSuccess(GameCharacter winner) {
        System.out.printf("\n~~ GAMBLE SUCCESS%n");
        System.out.println("+1 point to " + winner.getName());
        incrementScore(winner);
    }

    // Awards bonus points for gamble
    private void awardGambleBonus(GameCharacter winner) {
        System.out.printf("\n~~ GAMBLE %s%n", winner == player1 || winner == player2 ? "SUCCESS" : "FAILS");
        System.out.println("+" + GAMBLE_BONUS_POINTS + " points to " + winner.getName());
        if (winner == player1) {
            player1Score += GAMBLE_BONUS_POINTS;
        } else {
            player2Score += GAMBLE_BONUS_POINTS;
        }
    }

    // Awards points for tie
    private void awardTieBonus(GameCharacter keeper) {
        System.out.printf("\n~~ TIE - CAUTION REWARDED%n");
        System.out.println("+" + TIE_BONUS_POINTS + " point to " + keeper.getName());
        incrementScore(keeper);
    }

    // Handles tie scenario
    private void handleTie() {
        System.out.println("\n~~ TIE - FATE REMAINS BALANCED");
        System.out.println("No points awarded");
    }

    // Increments player score
    private void incrementScore(GameCharacter player) {
        if (player == player1) {
            player1Score++;
        } else {
            player2Score++;
        }
    }

    // Shows current score
    private void displayScoreUpdate() {
        System.out.println("\n* SCORE UPDATE *");
        System.out.printf("%s: %d | %s: %d%n",
            player1.getName().toUpperCase(), player1Score, 
            player2.getName().toUpperCase(), player2Score);

        GameUtils.ENTERKey();
    }

    // Shows final results and returns winner
    private GameCharacter displayFinalResults() {
        GameUtils.clearScreen();
        System.out.println("=".repeat(34));
        System.out.println("           FINAL SCORE");
        System.out.println("=".repeat(34));
        System.out.printf("%s: %d points | %s: %d points%n",
                player1.getName(), player1Score, player2.getName(), player2Score);
        
        GameCharacter winner = player1Score >= WINNING_SCORE ? player1 : player2;
        GameCharacter loser = (winner == player1) ? player2 : player1;
        
        displayVictoryMessage(winner, loser);
        GameUtils.ENTERKey();
        
        return winner;
    }

    // Shows victory message
    private void displayVictoryMessage(GameCharacter winner, GameCharacter loser) {
        System.out.println("\n" + "*".repeat(34));
        System.out.printf("     %s WINS THE DUEL!%n", winner.getName().toUpperCase());
        System.out.println("*".repeat(34));
        
        System.out.printf("\n%s (%s) defeats %s (%s)%n",
            winner.getRoleName(), getTeamDisplay(winner.getTeam()),
            loser.getRoleName(), getTeamDisplay(loser.getTeam()));
        
        if (winner.getTeam() == Team.GOOD) {
            System.out.println("* THE LIGHT PREVAILS - GOOD TEAM VICTORY!");
        } else {
            System.out.println("* THE SHADOWS TRIUMPH - EVIL TEAM VICTORY!");
        }
    }

    // Gets display name for team
    private String getTeamDisplay(Team team) {
        return team == Team.GOOD ? "The Light" : "The Shadows";
    }
}

