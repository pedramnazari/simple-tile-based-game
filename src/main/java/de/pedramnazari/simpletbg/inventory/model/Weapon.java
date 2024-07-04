package de.pedramnazari.simpletbg.inventory.model;

public class Weapon extends Item {

    private int damage = 10;

    public Weapon(int x, int y, String name, String description, int type) {
        super(x, y, name, description, type);
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
