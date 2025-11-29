package moonlightsgambit.characters;
import moonlightsgambit.enums.Team;
import moonlightsgambit.MoonlightsGambit;

// Base class for all game characters
public abstract class GameCharacter {
    
    private static final String NAME_VALIDATION_MSG = "Character name cannot be null or empty";
    private static final String TEAM_VALIDATION_MSG = "Team cannot be null";
    private static final String ORDER_VALIDATION_MSG = "Order must be positive";

    private String name;
    private boolean isAlive;
    private Team team;
    private int order;
    private boolean abilityBlocked;
    private boolean isBlessed;

    public GameCharacter(String name, Team team) {
        validateConstructorParameters(name, team);
        this.name = name.trim();
        this.team = team;
        this.isAlive = true;
        this.abilityBlocked = false;
        this.isBlessed = false;
    }

    private void validateConstructorParameters(String name, Team team) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(NAME_VALIDATION_MSG);
        }
        if (team == null) {
            throw new IllegalArgumentException(TEAM_VALIDATION_MSG);
        }
    }

    // Getters and setters with validation
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

    public int getOrder() { 
        return order; 
    }

    public void setOrder(int order) { 
        if (order <= 0) {
            throw new IllegalArgumentException(ORDER_VALIDATION_MSG);
        }
        this.order = order;
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

    // Abstract methods for character-specific behavior
    public abstract void performAction(GameCharacter target, MoonlightsGambit game);
    public abstract String getRoleDescription();
    public abstract String getActionPrompt();
    public abstract String getLoreDescription();
    public abstract String getRoleName();
    public void resetNightAction() {}

    // Gets character status
    public String getStatus() {
        StringBuilder status = new StringBuilder();
        status.append(String.format("%s - %s [%s]", name, getRoleDescription(), 
                           isAlive ? "ALIVE" : "DEAD"));
        
        if (abilityBlocked && isAlive) {
            status.append(" [SABOTAGED]");
        }

        return status.toString();
    }

    @Override
    public String toString() {
        return getStatus();
    }

    // Validates basic target conditions
    protected boolean isValidBasicTarget(GameCharacter target) {
        return target != null && target.isAlive();
    }
 
    // Validates game instance
    protected void validateGameInstance(MoonlightsGambit game) {
        if (game == null) {
            throw new IllegalArgumentException("Game instance cannot be null");
        }
    }
}
