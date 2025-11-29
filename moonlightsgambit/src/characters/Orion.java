package moonlightsgambit.characters;

import moonlightsgambit.MoonlightsGambit;
import moonlightsgambit.enums.Team;
import moonlightsgambit.utils.GameUtils;

// The Reaper character - hunts other players
public class Orion extends GameCharacter {
    
    private static final String ROLE_NAME = "Orion - The Reaper";
    private static final String ROLE_DESCRIPTION = "The Reaper (Shadows Team)";
    private static final String LORE_DESCRIPTION = "His touch brings silence to the night.";
    private static final String ACTION_PROMPT = "Choose a player to hunt under the moonlight: ";
    private static final int TEXT_DELAY_MS = 30;
    
    public Orion(String name) {
        super(name, Team.EVIL);
    }
    
    @Override
    public void performAction(GameCharacter target, MoonlightsGambit game) {
        validateGameInstance(game);
        
        if (isAbilityBlocked()) {
            displayAbilityBlockedMessage();
            return;
        }
        
        if (isValidBasicTarget(target)) {
            executeHuntAction(target, game);
        } else {
            displayInvalidTargetMessage(target);
        }
    }
    
    // Executes hunt action
    private void executeHuntAction(GameCharacter target, MoonlightsGambit game) {
        displayHuntMessage(target);
        recordHuntAction(target, game);
    }
    
    private void displayHuntMessage(GameCharacter target) {
        GameUtils.typeText(String.format("[HUNT] %s marks %s for the reaping!", getName(), target.getName()), TEXT_DELAY_MS);
    }
    
    private void recordHuntAction(GameCharacter target, MoonlightsGambit game) {
        game.recordHunt(target);
    }
    
    private void displayAbilityBlockedMessage() {
        GameUtils.typeText(String.format("[HUNT] %s marks %s for the reaping!", getName(), target.getName()), TEXT_DELAY_MS);
    }
    
    private void displayInvalidTargetMessage(GameCharacter target) {
        if (target == null) {
            GameUtils.typeText("[ERROR] Cannot hunt - no target selected", TEXT_DELAY_MS);
        } else if (!target.isAlive()) {
            GameUtils.typeText(String.format("[ERROR] Cannot hunt %s - target is not alive", target.getName()), TEXT_DELAY_MS);
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

