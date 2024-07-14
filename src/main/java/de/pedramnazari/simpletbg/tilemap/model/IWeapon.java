package de.pedramnazari.simpletbg.tilemap.model;

public interface IWeapon extends IItem {
    int getAttackingDamage();

    void setAttackingDamage(int attackingDamage);

    int getRange();
}
