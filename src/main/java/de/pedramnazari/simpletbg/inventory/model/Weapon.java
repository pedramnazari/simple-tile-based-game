package de.pedramnazari.simpletbg.inventory.model;

import de.pedramnazari.simpletbg.tilemap.model.IWeapon;

public class Weapon extends Item implements IWeapon {

    private int attackingDamage = 10;
    private int range = 1;

    public Weapon(int x, int y, String name, String description, int type) {
        super(x, y, name, description, type);
    }

    @Override
    public int getAttackingDamage() {
        return attackingDamage;
    }

    @Override
    public void setAttackingDamage(int attackingDamage) {
        this.attackingDamage = attackingDamage;
    }

    @Override
    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }
}
