package de.pedramnazari.simpletbg.character.hero.model.spell;

/**
 * Base class for sorcerer spells
 */
public abstract class Spell {
    private final String name;
    private final int manaCost;
    private final String description;

    protected Spell(String name, int manaCost, String description) {
        this.name = name;
        this.manaCost = manaCost;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getManaCost() {
        return manaCost;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Check if the spell can be cast with the available mana
     */
    public boolean canCast(int availableMana) {
        return availableMana >= manaCost;
    }
}
