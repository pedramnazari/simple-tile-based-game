package de.pedramnazari.simpletbg.character.enemy.model;

import de.pedramnazari.simpletbg.character.model.Character;

public class RushCreature extends Character implements de.pedramnazari.simpletbg.tilemap.model.IEnemy {

    private int frozenTurns = 0;

    public RushCreature(int type, int x, int y) {
        super(type, x, y);
        // Rush creatures are very fragile - low health
        setHealth(10);
    }

    private void setHealth(int health) {
        // Override health to be very low for fragile creatures
        while (getHealth() > health) {
            decreaseHealth(1);
        }
    }

    @Override
    public void setFrozenTurns(int turns) {
        this.frozenTurns = Math.max(0, turns);
    }

    @Override
    public int getFrozenTurns() {
        return frozenTurns;
    }

    @Override
    public int decrementFrozenTurns() {
        if (frozenTurns > 0) {
            frozenTurns--;
        }
        return frozenTurns;
    }
}
