package moonlightsgambit.characters;

import moonlightsgambit.MoonlightsGambit;
import moonlightsgambit.enums.Team;
import moonlightsgambit.utils.GameUtils;

public class Luna extends GameCharacter {
    
    private static final String ROLE_NAME = "Luna - The Saboteur";
    private static final String ROLE_DESCRIPTION = "The Saboteur (Evil Team)";
    private static final String LORE_DESCRIPTION = "Her chaos disrupts the natural order.";
    private static final String ACTION_PROMPT = "Choose a player to sabotage (block ability NEXT round): ";
    
    public Luna(String name) {
        super(name, Team.EVIL);
    }
    
    @Override
    public void performAction(GameCharacter target, MoonlightsGambit game) {
        validateGameInstance(game);

        if (!validateTarget(target)) {
            displayInvalidTargetMessage(target);
            return;
        }

        // ALWAYS show sabotage message (even if target is blessed)
        displayActionMessage(target);  

        // Record sabotage for NEXT ROUND (works even if target is blessed!)
        game.recordSabotage(target);
    }
    
    @Override
    protected boolean validateTarget(GameCharacter target) {
        return super.validateTarget(target) && target != this;
    }
    
    @Override
    protected void executeAction(GameCharacter target, MoonlightsGambit game) {
        // Handled in overridden performAction
    }
    
    @Override
    protected void displayActionMessage(GameCharacter target) {
        GameUtils.typeText(String.format("[SABOTAGE] %s weaves chaos around %s!", 
                          getName(), target.getName()), TEXT_DELAY_MS);
    }
    
    @Override
    protected void displayAbilityBlockedMessage() {
        GameUtils.typeText(String.format("[BLOCKED] A shadow disrupts %s's focus - her chaos fails!", 
                          getName()), TEXT_DELAY_MS);
    }
    
    @Override
    protected void displayInvalidTargetMessage(GameCharacter target) {
        if (target == null) {
            GameUtils.typeText(ERROR_NO_TARGET, TEXT_DELAY_MS);
        } else if (!target.isAlive()) {
            GameUtils.typeText(String.format(ERROR_TARGET_DEAD, target.getName()), TEXT_DELAY_MS);
        } else if (target == this) {
            GameUtils.typeText(String.format(ERROR_SELF_TARGET, getName()), TEXT_DELAY_MS);
        }
    }
    
    @Override public String getRoleDescription() { return ROLE_DESCRIPTION; }
    @Override public String getActionPrompt() { return ACTION_PROMPT; }
    @Override public String getLoreDescription() { return LORE_DESCRIPTION; }
    @Override public String getRoleName() { return ROLE_NAME; }
}
