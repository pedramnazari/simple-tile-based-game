package de.pedramnazari.simpletbg.character.hero.model.spell;

/**
 * Heal spell - restores health using mana
 */
public class HealSpell extends Spell {
    private static final String NAME = "Heal";
    private static final int MANA_COST = 40;
    private static final String DESCRIPTION = "Restores 50 health points";
    private static final int HEALING_AMOUNT = 50;

    public HealSpell() {
        super(NAME, MANA_COST, DESCRIPTION);
    }

    public int getHealingAmount() {
        return HEALING_AMOUNT;
    }
}
