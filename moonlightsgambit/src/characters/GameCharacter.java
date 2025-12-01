package moonlightsgambit.characters;

import moonlightsgambit.enums.Team;
import moonlightsgambit.MoonlightsGambit;
import moonlightsgambit.utils.GameUtils;

// Base class for all game characters
public abstract class GameCharacter {
    
    // Constants (moved from individual classes)
    protected static final int TEXT_DELAY_MS = 30;
    protected static final String ERROR_NO_TARGET = "[ERROR] Cannot perform action - no target selected";
    protected static final String ERROR_TARGET_DEAD = "[ERROR] Cannot perform action on %s - target is not alive";
    protected static final String ERROR_SELF_TARGET = "[ERROR] %s cannot target themselves!";
    
    // Validation messages
    private static final String NAME_VALIDATION_MSG = "Character name cannot be null or empty";
    private static final String TEAM_VALIDATION_MSG = "Team cannot be null";

    private String name;
    private boolean isAlive;
    private Team team;
    private boolean abilityBlocked;
    private boolean isBlessed;
    private int order; 

    public GameCharacter(String name, Team team) {
        validateConstructorParameters(name, team);
        this.name = name.trim();
        this.team = team;
        this.isAlive = true;
        this.abilityBlocked = false;
        this.isBlessed = false;
        this.order = 0;
    }

    private void validateConstructorParameters(String name, Team team) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(NAME_VALIDATION_MSG);
        }
        if (team == null) {
            throw new IllegalArgumentException(TEAM_VALIDATION_MSG);
        }
    }

    // ===== TEMPLATE METHOD PATTERN =====
    public void performAction(GameCharacter target, MoonlightsGambit game) {
        validateGameInstance(game);
        
        if (!validateTarget(target)) {
            displayInvalidTargetMessage(target);
            return;
        }
        
        if (isAbilityBlocked()) {
            displayAbilityBlockedMessage();
            return;
        }
        
        displayActionMessage(target);
        executeAction(target, game);
    }
    
    // ===== ABSTRACT METHODS =====
    protected abstract void executeAction(GameCharacter target, MoonlightsGambit game);
    protected abstract void displayActionMessage(GameCharacter target);
    protected abstract void displayAbilityBlockedMessage();
    
    // ===== PROTECTED VALIDATION METHODS =====
    protected boolean validateTarget(GameCharacter target) {
        return target != null && target.isAlive();
    }
    
    protected void displayInvalidTargetMessage(GameCharacter target) {
        if (target == null) {
            GameUtils.typeText(ERROR_NO_TARGET, TEXT_DELAY_MS);
        } else if (!target.isAlive()) {
            GameUtils.typeText(String.format(ERROR_TARGET_DEAD, target.getName()), TEXT_DELAY_MS);
        }
    }
    
    protected void validateGameInstance(MoonlightsGambit game) {
        if (game == null) {
            throw new IllegalArgumentException("Game instance cannot be null");
        }
    }
    
    // ===== NEW METHODS TO FIX COMPILATION ERRORS =====
    
    // Called from MoonlightsGambit.java line 178
    public void resetNightAction() {
        // Reset night-specific statuses
        this.abilityBlocked = false;
        this.isBlessed = false;
        // Note: DO NOT reset isAlive here - that's permanent
    }
    
    // Called from GameSetup.java line 60
    public void setOrder(int order) {
        this.order = order;
    }
    
    public int getOrder() {
        return this.order;
    }
    
    // ===== GETTERS & SETTERS =====
    public String getName() { 
        return name; 
    }
 
    public void setName(String name) { 
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(NAME_VALIDATION_MSG);
        }
        this.name = name.trim();
    }

    public boolean isAlive() { 
        return isAlive; 
    }

    public void setAlive(boolean alive) { 
        isAlive = alive; 
    }
  
    public Team getTeam() { 
        return team; 
    }

    public void setTeam(Team team) { 
        if (team == null) {
            throw new IllegalArgumentException(TEAM_VALIDATION_MSG);
        }
        this.team = team;
    }

    public boolean isAbilityBlocked() { 
        return abilityBlocked; 
    }

    public void setAbilityBlocked(boolean blocked) { 
        abilityBlocked = blocked; 
    }

    public boolean isBlessed() { 
        return isBlessed; 
    }

    public void setBlessed(boolean blessed) { 
        isBlessed = blessed; 
    }

    // ===== ABSTRACT METHODS FOR CHARACTER INFO =====
    public abstract String getRoleDescription();
    public abstract String getActionPrompt();
    public abstract String getLoreDescription();
    public abstract String getRoleName();

    @Override
    public String toString() {
        return String.format("%s - %s [%s]", name, getRoleDescription(), 
                           isAlive ? "ALIVE" : "DEAD");
    }
}
