package moonlightsgambit.characters;

import moonlightsgambit.MoonlightsGambit;
import moonlightsgambit.enums.Team;
import moonlightsgambit.utils.GameUtils;

// The Believer character - protects other players
public class Elara extends GameCharacter {

    private static final String ROLE_NAME = "Elara - The Believer";
    private static final String ROLE_DESCRIPTION = "The Believer (Good Team)";
    private static final String LORE_DESCRIPTION = "Her faith shines brighter than the moon.";
    private static final String ACTION_PROMPT = "Choose a player to protect with divine light (0 to protect yourself): ";

    // Constructor
    public Elara(String name) {
        super(name, Team.GOOD);
    }
    
    @Override
    public void performAction(GameCharacter target, MoonlightsGambit game) {
        validateGameInstance(game);
    
        // Check if Elara is ability blocked (sabotaged THIS round)
        if (isAbilityBlocked()) {
            // Show that she's trying but gets blocked
            GameUtils.typeText(String.format("[PROTECT] %s calls upon the divine light...", getName()), TEXT_DELAY_MS);
            displayAbilityBlockedMessage();
            return; // Stop here - blessing fails
        }
    
        if (!validateTarget(target)) {
            displayInvalidTargetMessage(target);
            return;
        }
    
        // Normal protection flow
        displayActionMessage(target);
        executeAction(target, game);
    }
    
    @Override
    protected boolean validateTarget(GameCharacter target) {
        // Elara can target herself
        return target != null && target.isAlive();
    }
    
    // Perform night action
    @Override
    protected void executeAction(GameCharacter target, MoonlightsGambit game) {
        target.setBlessed(true);
    }
    
    @Override
    protected void displayActionMessage(GameCharacter target) {
        String protectionTarget = (target == this) ? "herself" : target.getName();
        GameUtils.typeText(String.format("[PROTECT] %s channels divine light to protect %s!", 
                          getName(), protectionTarget), TEXT_DELAY_MS);
    }
    
    @Override
    protected void displayAbilityBlockedMessage() {
        GameUtils.typeText(String.format("[BLOCKED] A shadow disrupts %s's prayer - the blessing fails to hold!", 
                          getName()), TEXT_DELAY_MS);
    }
    
    //Abstract method implementations for role details
    @Override public String getRoleDescription() { return ROLE_DESCRIPTION; }
    @Override public String getActionPrompt() { return ACTION_PROMPT; }
    @Override public String getLoreDescription() { return LORE_DESCRIPTION; }
    @Override public String getRoleName() { return ROLE_NAME; }
}
