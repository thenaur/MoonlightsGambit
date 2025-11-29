package moonlightsgambit.characters;

import moonlightsgambit.MoonlightsGambit;
import moonlightsgambit.enums.Team;
import moonlightsgambit.utils.GameUtils;

// The Seeker character - investigates other players
public class Calisto extends GameCharacter {

    private static final String ROLE_NAME = "Calisto - The Seeker";
    private static final String ROLE_DESCRIPTION = "The Seeker (Light Team)";
    private static final String LORE_DESCRIPTION = "His intuition pierces through shadows.";
    private static final String ACTION_PROMPT = "Choose a player to investigate: ";
    private static final int TEXT_DELAY_MS = 30;
 
    public Calisto(String name) {
        super(name, Team.GOOD);
    }
  
    @Override
    public void performAction(GameCharacter target, MoonlightsGambit game) {
        validateGameInstance(game);
        
        if (isAbilityBlocked()) {
            displayAbilityBlockedMessage();
            return;
        }
        
        if (isValidBasicTarget(target)) {
            executeInvestigation(target, game);
        } else {
            displayInvalidTargetMessage(target);
        }
    }
 
    // Executes investigation action
    private void executeInvestigation(GameCharacter target, MoonlightsGambit game) {
        displayInvestigationStart(target);
        displayInvestigationResult(target);
        game.recordInvestigation(target);
    }
 
    private void displayInvestigationStart(GameCharacter target) {
        GameUtils.typeText(String.format("[INVESTIGATE] %s investigates %s!", getName(), target.getName()), TEXT_DELAY_MS);
    }

    private void displayInvestigationResult(GameCharacter target) {
        GameUtils.typeText(String.format("[RESULT] %s is %s", target.getName(), target.getRoleName()), TEXT_DELAY_MS);
    }

    private void displayAbilityBlockedMessage() {
        GameUtils.typeText(String.format("[BLOCKED] A dark fog clouds s%'s vision - the truth remains hidden!", getName()), TEXT_DELAY_MS);
    }
 
    private void displayInvalidTargetMessage(GameCharacter target) {
        if (target == null) {
            GameUtils.typeText("[ERROR] Cannot investigate - no target selected", TEXT_DELAY_MS);
        } else if (!target.isAlive()) {
            GameUtils.typeText(String.format("[ERROR] Cannot investigate %s - target is not alive", target.getName()), TEXT_DELAY_MS);
        }
    }

    @Override
    public String getRoleDescription() {
        return ROLE_DESCRIPTION;
    }

    @Override
    public String getActionPrompt() {
        return ACTION_PROMPT;
    }

    @Override
    public String getLoreDescription() {
        return LORE_DESCRIPTION;
    }

    @Override
    public String getRoleName() {
        return ROLE_NAME;
    }
 
    @Override
    public void resetNightAction() { 
    }
}


