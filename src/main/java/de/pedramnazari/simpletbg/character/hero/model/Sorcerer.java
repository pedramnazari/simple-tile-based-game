package de.pedramnazari.simpletbg.character.hero.model;

public class Sorcerer extends Hero {

    private static final int MAX_SORCERER_MANA = 150;
    private static final int SORCERER_ATTACKING_POWER = 15;

    public Sorcerer(int x, int y) {
        super(x, y);
        setMaxMana(MAX_SORCERER_MANA);
        increaseMana(MAX_SORCERER_MANA);
        setAttackingPower(SORCERER_ATTACKING_POWER);
    }
}
