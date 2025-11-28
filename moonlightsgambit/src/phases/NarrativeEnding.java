package moonlightsgambit.phases;

import moonlightsgambit.MoonlightsGambit;
import moonlightsgambit.characters.GameCharacter;
import moonlightsgambit.utils.GameUtils;

// Handles narrative endings for different game outcomes
public class NarrativeEnding {

    // Shows evil team victory narrative
    public static void printEvilEnding(GameCharacter a, GameCharacter b) {
        GameUtils.clearScreen();
        System.out.println("*".repeat(50));
        System.out.println("                EVIL TEAM VICTORY!");
        System.out.println("*".repeat(50));

        GameUtils.typeText("\nUnder the reign of shadow and disorder, the academy collapses slowly into ruin, its once-sacred halls cracking under the weight of choices twisted by fear and longing.", 30);
        GameUtils.typeText("Orion walks through the wreckage with heaviness, every crumble of stone reminding him of what Elara once believed he could become, and what he ultimately destroyed instead.", 30);
        GameUtils.typeText("Luna watches the downfall with a quiet thrill, her chaos blooming freely at last, while Orion feels each fading echo like a ghost tugging at his resolve.", 30);
        GameUtils.typeText("Artemia does not fall in flames, but in a quiet, devastating unraveling of its memories buried beneath the rubble, as its hope is swallowed by the very shadows sworn to protect it.", 30);
        
        System.out.println("\n" + "*".repeat(50));
        System.out.println("                DARKNESS TRIUMPHS");
        System.out.println("                    [THE END.]");
        System.out.println("*".repeat(50));
        
        GameUtils.ENTERKey();
    }

    // Shows good team victory narrative
    public static void printGoodEnding(GameCharacter player1, GameCharacter player2) {
        GameUtils.clearScreen();
        System.out.println("*".repeat(50));
        System.out.println("                GOOD TEAM VICTORY!");
        System.out.println("*".repeat(50));

        GameUtils.typeText("\nUnder the gentle light of the restored moon, the academy begins to heal, its ancient stones whispering of hope and renewal.", 30);
        GameUtils.typeText("Elara's faith has shielded the innocent, while Calisto's keen insight uncovered the truth before darkness could prevail.", 30);
        GameUtils.typeText("Though shadows may always linger at the edges of Artemia, the bonds of trust and justice have proven stronger than any chaos.", 30);
        GameUtils.typeText("A new dawn rises over the academy, not without scars, but with the promise of peace carefully won and cherished.", 30);
        
        System.out.println("\n" + "*".repeat(50));
        System.out.println("                 LIGHT PREVAILS");
        System.out.println("                    [THE END.]");
        System.out.println("*".repeat(50));
        
        GameUtils.ENTERKey();
    }

    //Displays lovers victory scenario with special narrative
    public static void displayLoversVictory(MoonlightsGambit game, GameCharacter elara, GameCharacter orion) {
        GameUtils.clearScreen();
        System.out.println("*".repeat(80));
        System.out.println("                               LOVE CONQUERS ALL!");
        System.out.println("*".repeat(80));

        GameUtils.typeText("\nElara: \"We've survived, and yet the night feels heavier than before.\"", 30);
        GameUtils.typeText("Orion: \"Fate has spared us both… and shadows still linger between us.\"", 30);
        GameUtils.typeText("", 30);
        GameUtils.typeText("In the silent halls of Artemia, two figures stand apart yet unharmed.", 30);
        GameUtils.typeText("Neither light nor shadow claims total victory, and the night holds its breath.", 30);
        GameUtils.typeText("Bonds remain intact, yet every choice leaves a trace on hearts and halls alike.", 30);
        
        GameUtils.ENTERKey();
        displayGameConclusion(game, elara, orion, "LOVE HAS TRIUMPHED OVER WAR!");
    }

    // Shows friends victory scenario
    public static void displayFriendsVictory(MoonlightsGambit game, GameCharacter calisto, GameCharacter luna) {
        GameUtils.clearScreen();
        System.out.println("*".repeat(88));
        System.out.println("                              A WOUNDED FRIENDSHIP REMAINS");
        System.out.println("*".repeat(88));
        
        GameUtils.typeText("\nCalisto: \"We've survived… though trust will not be so easily restored.\"", 30);
        GameUtils.typeText("Luna: \"Chaos and order coexist for now, but nothing feels certain.\"", 30);
        GameUtils.typeText("", 30);
        GameUtils.typeText("The ancient halls of Artemia stand silent, holding their breath as moonlight filters", 30);
        GameUtils.typeText("through fractured windows. Though survival has granted them another dawn, the weight", 30);
        GameUtils.typeText("of betrayal hangs heavy in the air between them. Calisto's gaze remains guarded,", 30);
        GameUtils.typeText("memories of childhood laughter now shadowed by recent deception, while Luna watches", 30);
        GameUtils.typeText("with a newfound caution, the thrill of chaos tempered by the cost of nearly losing", 30);
        GameUtils.typeText("her oldest friend. The bond between seeker and saboteur has not completely broken,", 30);
        GameUtils.typeText("but it now exists in the fragile space between what was and what might never be again.", 30);
        
        GameUtils.ENTERKey();
        displayGameConclusion(game, calisto, luna, "    FRIENDSHIP SURVIVES, CHANGED BUT UNBROKEN!");
    }

    // Shows game conclusion with player status
    private static void displayGameConclusion(MoonlightsGambit game, GameCharacter char1, GameCharacter char2, String victoryMessage) {
        GameUtils.clearScreen();
        System.out.println("=".repeat(50));
        System.out.println(GameUtils.GAME_CONCLUDED);
        System.out.println("=".repeat(50));
        
        for (GameCharacter p : game.getPlayers()) {
            if (p == null) continue;
            boolean victorious = (p == char1 || p == char2);
            String fate = GameUtils.getFate(victorious);
            System.out.printf("""
                              
                              %s as %s    %s%n""", p.getName(), p.getRoleName(), fate);
        }
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           " + victoryMessage);
        System.out.println("=".repeat(50));
    }
}

