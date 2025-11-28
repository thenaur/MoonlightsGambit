package moonlightsgambit.characters;

import moonlightsgambit.MoonlightsGambit;
import moonlightsgambit.enums.Team;
import moonlightsgambit.utils.GameUtils;

// The Believer character - protects other players
public class Elara extends GameCharacter {

    private static final String ROLE_NAME = "Elara - The Believer";
    private static final String ROLE_DESCRIPTION = "The Believer (Light Team)";
    private static final String LORE_DESCRIPTION = "Her faith shines brighter than the moon.";
    private static final String ACTION_PROMPT = "Choose a player to protect with divine light (0 to protect yourself): ";
    private static final int TEXT_DELAY_MS = 30;

    public Elara(String name) {
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
            executeProtectionAction(target, game);
        } else {
            displayInvalidTargetMessage(target);
        }
    }
// Executes protection action    
    private void executeProtectionAction(GameCharacter target, MoonlightsGambit game) {
        displayProtectionMessage(target);
        applyDivineProtection(target);
        game.recordProtection(target);
    }
    
    private void displayProtectionMessage(GameCharacter target) {
        String protectionTarget = getProtectionTargetName(target);
        GameUtils.typeText(String.format("[PROTECT] %s channels divine light to protect %s!", getName(), protectionTarget), TEXT_DELAY_MS);
    }

    private String getProtectionTargetName(GameCharacter target) {
        return (target == this) ? "herself" : target.getName();
    }
    
    private void applyDivineProtection(GameCharacter target) {
        target.setBlessed(true);
    }

    private void displayAbilityBlockedMessage() {
        GameUtils.typeText(String.format("[BLOCKED] %s's ability is blocked by the SABOTEUR! Cannot protect.", getName()), TEXT_DELAY_MS);
    }
    
    private void displayInvalidTargetMessage(GameCharacter target) {
        if (target == null) {
            GameUtils.typeText("[ERROR] Cannot protect - no target selected", TEXT_DELAY_MS);
        } else if (!target.isAlive()) {
            GameUtils.typeText(String.format("[ERROR] Cannot protect %s - target is not alive", target.getName()), TEXT_DELAY_MS);
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
