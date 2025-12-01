package moonlightsgambit.characters;

import moonlightsgambit.MoonlightsGambit;
import moonlightsgambit.enums.Team;
import moonlightsgambit.utils.GameUtils;

// The Seeker character - investigates other players
public class Calisto extends GameCharacter {

    private static final String ROLE_NAME = "Calisto - The Seeker";
    private static final String ROLE_DESCRIPTION = "The Seeker (Good Team)";
    private static final String LORE_DESCRIPTION = "His intuition pierces through shadows.";
    private static final String ACTION_PROMPT = "Choose a player to investigate: ";
 
    // Constructor
    public Calisto(String name) {
        super(name, Team.GOOD); 
    }
    
    // Perform night action
    @Override
    protected void executeAction(GameCharacter target, MoonlightsGambit game) {
        GameUtils.typeText(String.format("[RESULT] %s is %s", 
                          target.getName(), target.getRoleName()), TEXT_DELAY_MS);
    }
    
    @Override
    protected void displayActionMessage(GameCharacter target) {
        GameUtils.typeText(String.format("[INVESTIGATE] %s investigates %s!", 
                          getName(), target.getName()), TEXT_DELAY_MS);
    }
    
    @Override
    protected void displayAbilityBlockedMessage() {
        GameUtils.typeText(String.format("[BLOCKED] A dark fog clouds %s's vision - the truth remains hidden!", 
                          getName()), TEXT_DELAY_MS);
    }
    
    //Abstract method implementations for role details
    @Override public String getRoleDescription() { return ROLE_DESCRIPTION; }
    @Override public String getActionPrompt() { return ACTION_PROMPT; }
    @Override public String getLoreDescription() { return LORE_DESCRIPTION; }
    @Override public String getRoleName() { return ROLE_NAME; }
}
