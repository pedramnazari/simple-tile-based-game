package de.pedramnazari.simpletbg.character.hero.model.spell;

/**
 * Teleport spell - allows sorcerer to teleport short distances
 */
public class TeleportSpell extends Spell {
    private static final String NAME = "Teleport";
    private static final int MANA_COST = 50;
    private static final String DESCRIPTION = "Teleports the sorcerer up to 3 tiles in the facing direction";
    private static final int MAX_DISTANCE = 3;

    public TeleportSpell() {
        super(NAME, MANA_COST, DESCRIPTION);
    }

    public int getMaxDistance() {
        return MAX_DISTANCE;
    }
}
