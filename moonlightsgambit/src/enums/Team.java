package moonlightsgambit.enums;

// Represents the two teams in the game
public enum Team {
    GOOD("The Light"),
    EVIL("The Shadows");

    private final String displayName;
    
    Team(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    // Checks if this team opposes another team
    public boolean isOpposingTeam(Team otherTeam) {
        return this != otherTeam;
    }
    
    // Gets opposing team
    public Team getOpposingTeam() {
        return this == GOOD ? EVIL : GOOD;
    }
   
    @Override
    public String toString() {
        return displayName;
    }
}

