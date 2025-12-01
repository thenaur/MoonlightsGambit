package moonlightsgambit.characters;

import moonlightsgambit.MoonlightsGambit;
import moonlightsgambit.enums.Team;
import moonlightsgambit.utils.GameUtils;

// The Reaper character - hunts other players
public class Orion extends GameCharacter {
    
    private static final String ROLE_NAME = "Orion - The Reaper";
    private static final String ROLE_DESCRIPTION = "The Reaper (Evil Team)";
    private static final String LORE_DESCRIPTION = "His touch brings silence to the night.";
    private static final String ACTION_PROMPT = "Choose a player to hunt under the moonlight: ";
    
    // Constructor
    public Orion(String name) {
        super(name, Team.EVIL);
    }
    
    @Override
    public void performAction(GameCharacter target, MoonlightsGambit game) {
        validateGameInstance(game);
        
        if (!validateTarget(target)) {
            displayInvalidTargetMessage(target);
            return;
        }
        
        displayActionMessage(target);
        
        if (isAbilityBlocked()) {
            displayAbilityBlockedMessage();
            return;
        }
        
        if (!target.isBlessed()) {
            game.recordHunt(target);
        }
        // Silent failure if target is blessed
    }
    
    // Validate target (cannot target self)
    @Override
    protected void executeAction(GameCharacter target, MoonlightsGambit game) {
        // Moved logic to performAction for clarity
    }
    
    @Override
    protected void displayActionMessage(GameCharacter target) {
        GameUtils.typeText(String.format("[HUNT] %s marks %s for the reaping!", 
                          getName(), target.getName()), TEXT_DELAY_MS);
    }
    
    @Override
    protected void displayAbilityBlockedMessage() {
        GameUtils.typeText(String.format("[BLOCKED] A shadow disrupts %s's focus - the hunt fails!", 
                          getName()), TEXT_DELAY_MS);
    }
    
    //Abstract method implementations for role details
    @Override public String getRoleDescription() { return ROLE_DESCRIPTION; }
    @Override public String getActionPrompt() { return ACTION_PROMPT; }
    @Override public String getLoreDescription() { return LORE_DESCRIPTION; }
    @Override public String getRoleName() { return ROLE_NAME; }
}
