package moonlightsgambit.interfaces;

import moonlightsgambit.MoonlightsGambit;

// Interface defining contract for all game phases
public interface GamePhase {
    
    // Executes specific phase logic
    void executePhase(MoonlightsGambit game);
    // Gets display name of phase
    String getPhaseName();
}

