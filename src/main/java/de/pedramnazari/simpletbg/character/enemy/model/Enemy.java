package de.pedramnazari.simpletbg.character.enemy.model;

import de.pedramnazari.simpletbg.character.model.Character;

public class Enemy extends Character implements de.pedramnazari.simpletbg.tilemap.model.IEnemy {

    private int frozenTurns = 0;

    public Enemy(int type, int x, int y) {
        super(type, x, y);
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
