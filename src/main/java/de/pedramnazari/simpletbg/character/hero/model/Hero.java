package de.pedramnazari.simpletbg.character.hero.model;

import de.pedramnazari.simpletbg.character.model.Character;
import de.pedramnazari.simpletbg.inventory.model.IItemCollector;
import de.pedramnazari.simpletbg.inventory.model.Inventory;

public class Hero extends Character implements IItemCollector {
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
