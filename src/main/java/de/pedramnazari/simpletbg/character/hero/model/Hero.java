package de.pedramnazari.simpletbg.character.hero.model;

import de.pedramnazari.simpletbg.character.model.Character;
import de.pedramnazari.simpletbg.inventory.model.IItemCollector;
import de.pedramnazari.simpletbg.inventory.model.Inventory;
import de.pedramnazari.simpletbg.inventory.model.Weapon;

import java.util.Optional;

public class Hero extends Character implements IItemCollector {
    public static final int HERO_TYPE = 1000;

    private Inventory inventory;
    private Weapon weapon;

    public Hero(int x, int y) {
        super(HERO_TYPE, x, y);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setWeapon(Weapon weapon) {
        // TODO: handle case that hero already has a weapon (drop it or put it in inventory)
        this.weapon = weapon;
    }

    public Optional<Weapon> getWeapon() {
        return Optional.ofNullable(weapon);
    }
}
