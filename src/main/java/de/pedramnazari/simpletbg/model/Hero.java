package de.pedramnazari.simpletbg.model;

public class Hero extends Figure {

    private final Inventory inventory;

    public Hero(Inventory inventory, int x, int y) {
        super(x, y);
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
