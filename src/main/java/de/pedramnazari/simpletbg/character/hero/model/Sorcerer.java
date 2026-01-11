package de.pedramnazari.simpletbg.character.hero.model;

public class Sorcerer extends Hero {

    public Sorcerer(int x, int y) {
        super(x, y);
        setMaxMana(150);
        increaseMana(150);
        setAttackingPower(15);
    }
}
