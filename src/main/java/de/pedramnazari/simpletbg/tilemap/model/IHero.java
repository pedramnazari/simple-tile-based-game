package de.pedramnazari.simpletbg.tilemap.model;

import java.util.Optional;

public interface IHero extends ICharacter, IItemCollector {
    int HERO_TYPE = 1000;

    IInventory getInventory();

    void setInventory(IInventory inventory);

    void setWeapon(IWeapon weapon);

    Optional<IWeapon> getWeapon();
}
