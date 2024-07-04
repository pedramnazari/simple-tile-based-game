package de.pedramnazari.simpletbg.inventory.model;

public class Weapon extends Item {

    private int attackingDamage = 10;

    public Weapon(int x, int y, String name, String description, int type) {
        super(x, y, name, description, type);
    }

    public int getAttackingDamage() {
        return attackingDamage;
    }

    public void setAttackingDamage(int attackingDamage) {
        this.attackingDamage = attackingDamage;
    }
}
