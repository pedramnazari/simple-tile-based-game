package de.pedramnazari.simpletbg.character.enemy.model;

public class RushCreature extends Enemy {

    public RushCreature(int type, int x, int y) {
        super(type, x, y);
        // Rush creatures are very fragile - low health
        decreaseHealth(90); // Reduce from 100 to 10
    }
}
