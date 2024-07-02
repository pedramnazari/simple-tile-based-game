package de.pedramnazari.simpletbg.character.enemy.model;

import de.pedramnazari.simpletbg.character.model.Character;
import de.pedramnazari.simpletbg.inventory.model.IItemCollector;

public class Enemy extends Character implements IItemCollector {

    public Enemy(int type, int x, int y) {
        super(type, x, y);
    }
}
