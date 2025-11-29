package moonlightsgambit.phases;

import moonlightsgambit.MoonlightsGambit;
import moonlightsgambit.interfaces.GamePhase;
import moonlightsgambit.utils.GameUtils;

// Handles game introduction phase
public class IntroPhase implements GamePhase {
    
    @Override
    public void executePhase(MoonlightsGambit game) {
        displayGameTitle();
        displayWelcomeMessage();
        game.displayCycleHeader(1);
    }
    
    // Shows game title with formatted header
    private void displayGameTitle() {
        GameUtils.clearScreen();
        System.out.println("*".repeat(36));
        System.out.println("         MOONLIGHT'S GAMBIT");
        System.out.println("*".repeat(36));
        GameUtils.ENTERKey();
    }
    
    // Displays welcome message and game story
    private void displayWelcomeMessage() {
        GameUtils.clearScreen();
        GameUtils.typeText("In the ancient academy of Artemia, four souls are bound by love and friendship... and secrets that could destroy everything. ", 30);
        GameUtils.typeText(" ", 40);
        GameUtils.typeText("Elara and Orion share a passionate love, each hiding a destiny the other cannot know. ", 30);
        GameUtils.typeText("She carries divine light; he serves the shadow's call.", 30);
        GameUtils.typeText("If they discover each other's truth, can love survive?", 30);
        GameUtils.typeText(" ", 50);
        GameUtils.typeText("Luna and Calisto have been friends since childhood, now investigating the mysterious attacks together.", 50);
        GameUtils.typeText("But one seeks justice, while the other creates chaos.", 30);
        GameUtils.typeText("How far will friendship stretch when lies run deep?", 30);
        GameUtils.typeText(" ", 30);
        GameUtils.typeText("None knows the roles fate has dealt them.", 30);
        GameUtils.typeText("In this game of hidden loyalties, every choice could shatter the very bonds they're trying to protect.", 30);
        GameUtils.typeText(" ", 50);
        GameUtils.typeText("The masks are on... let the game unfold.", 40);
        GameUtils.ENTERKey();
    }
    
    @Override
    public String getPhaseName() {
        return "Introduction";
    }
}
