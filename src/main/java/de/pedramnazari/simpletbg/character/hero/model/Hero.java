package de.pedramnazari.simpletbg.character.hero.model;

import de.pedramnazari.simpletbg.inventory.model.IItemCollectorElement;
import de.pedramnazari.simpletbg.inventory.model.Inventory;
import de.pedramnazari.simpletbg.model.Figure;

public class Hero extends Figure implements IItemCollectorElement {
    public static final int HERO_TYPE = 1000;

    private Inventory inventory;

    public Hero(int x, int y) {
        super(x, y);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
