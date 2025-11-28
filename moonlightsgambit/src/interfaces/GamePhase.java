package moonlightsgambit.interfaces;

import moonlightsgambit.MoonlightsGambit;

// Interface defining contract for all game phases
public interface GamePhase {
    
    // Executes specific phase logic
    void executePhase(MoonlightsGambit game);
    
    // Gets display name of phase
    String getPhaseName();
    
    // Optional cleanup logic
    default void cleanupPhase(MoonlightsGambit game) {
        // Default empty implementation
    }
    
    // Validates if phase can execute
    default boolean canExecute(MoonlightsGambit game) {
        return game != null;
    }
}
