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

    @Override
    public String toString() {
        return displayName;
    }
}


