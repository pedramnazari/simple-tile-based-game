package de.pedramnazari.simpletbg.character.enemy.model;

import de.pedramnazari.simpletbg.character.model.Character;

public class Enemy extends Character implements de.pedramnazari.simpletbg.tilemap.model.IEnemy {

    public Enemy(int type, int x, int y) {
        super(type, x, y);
    }
}
