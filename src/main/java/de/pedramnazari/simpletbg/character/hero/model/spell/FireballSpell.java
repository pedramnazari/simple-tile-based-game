package de.pedramnazari.simpletbg.character.hero.model.spell;

/**
 * Fireball spell - deals magic damage to enemies in a direction
 */
public class FireballSpell extends Spell {
    private static final String NAME = "Fireball";
    private static final int MANA_COST = 30;
    private static final String DESCRIPTION = "Launches a magical fireball that deals 25 damage";
    private static final int DAMAGE = 25;
    private static final int RANGE = 3;

    public FireballSpell() {
        super(NAME, MANA_COST, DESCRIPTION);
    }

    public int getDamage() {
        return DAMAGE;
    }

    public int getRange() {
        return RANGE;
    }
}
