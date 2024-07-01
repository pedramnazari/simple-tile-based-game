package de.pedramnazari.simpletbg.character.enemy.model;

import de.pedramnazari.simpletbg.inventory.model.IItemCollectorElement;
import de.pedramnazari.simpletbg.model.Figure;

public class Enemy extends Figure implements IItemCollectorElement {

    public Enemy(int x, int y) {
        super(x, y);
    }
}
