package de.pedramnazari.simpletbg.tilemap.model;

public interface IProjectile extends IMovableTileElement {
    int getRemainingRange();

    void setRemainingRange(int remainingRange);

    boolean isActive();

    void deactivate();

    IWeapon getWeapon();

    int getDamage();
}
