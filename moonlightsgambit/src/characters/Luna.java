package moonlightsgambit.characters;

import moonlightsgambit.MoonlightsGambit;
import moonlightsgambit.enums.Team;
import moonlightsgambit.utils.GameUtils;

public class Luna extends GameCharacter {
    
    private static final String ROLE_NAME = "Luna - The Saboteur";
    private static final String ROLE_DESCRIPTION = "The Saboteur (Shadows Team)";
    private static final String LORE_DESCRIPTION = "Her chaos disrupts the natural order.";
    private static final String ACTION_PROMPT = "Choose a player to sabotage (block ability NEXT round): ";
    private static final int TEXT_DELAY_MS = 30;
    
    public Luna(String name) {
        super(name, Team.EVIL);
    }
    
    @Override
    public void performAction(GameCharacter target, MoonlightsGambit game) {
        validateGameInstance(game);
        
        // ALWAYS show sabotage attempt first
        displaySabotageMessage(target);
        
        if (isAbilityBlocked()) {
            return;
        }
        
        if (isValidTarget(target)) {
            executeSabotageAction(target, game);
        } else {
            displayInvalidTargetMessage(target);
        }
    }
    
    private boolean isValidTarget(GameCharacter target) {
        return isValidBasicTarget(target) && target != this; 
    }
    
    private void executeSabotageAction(GameCharacter target, MoonlightsGambit game) {
        if (isTargetProtected(target)) {
            return;
        }
        
        game.recordSabotage(target);
    }
    
    private boolean isTargetProtected(GameCharacter target) {
        return target.isBlessed();
    }
    
    private void displaySabotageMessage(GameCharacter target) {
        GameUtils.typeText(String.format("[SABOTAGE] %s weaves chaos around %s!", getName(), target.getName()), TEXT_DELAY_MS);
    }
    
    private void displayInvalidTargetMessage(GameCharacter target) {
        if (target == null) {
            GameUtils.typeText("[ERROR] Cannot sabotage - no target selected", TEXT_DELAY_MS);
        } else if (!target.isAlive()) {
            GameUtils.typeText(String.format("[ERROR] Cannot sabotage %s - target is not alive", target.getName()), TEXT_DELAY_MS);
        } else if (target == this) {
            GameUtils.typeText(String.format("[ERROR] %s cannot sabotage herself!", getName()), TEXT_DELAY_MS);
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
