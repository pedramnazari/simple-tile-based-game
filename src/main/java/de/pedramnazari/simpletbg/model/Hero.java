package de.pedramnazari.simpletbg.model;

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
