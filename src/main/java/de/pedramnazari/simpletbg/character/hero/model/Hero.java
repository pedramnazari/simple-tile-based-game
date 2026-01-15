package de.pedramnazari.simpletbg.character.hero.model;

import de.pedramnazari.simpletbg.character.model.Character;
import de.pedramnazari.simpletbg.tilemap.model.IArmor;
import de.pedramnazari.simpletbg.tilemap.model.IHero;
import de.pedramnazari.simpletbg.tilemap.model.IInventory;
import de.pedramnazari.simpletbg.tilemap.model.IRing;
import de.pedramnazari.simpletbg.tilemap.model.IWeapon;

import java.util.Optional;

public class Hero extends Character implements IHero {

    private IInventory inventory;
    private IWeapon weapon;
    private IRing ring;
    private IArmor armor;

    public Hero(int x, int y) {
        super(HERO_TYPE, x, y);
    }

    @Override
    public IInventory getInventory() {
        return inventory;
    }

    @Override
    public void setInventory(IInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void setWeapon(IWeapon weapon) {
        this.weapon = weapon;
    }

    @Override
    public Optional<IWeapon> getWeapon() {
        return Optional.ofNullable(weapon);
    }

    @Override
    public void setRing(IRing ring) {
        this.ring = ring;
    }

    @Override
    public Optional<IRing> getRing() {
        return Optional.ofNullable(ring);
    }

    @Override
    public void setArmor(IArmor armor) {
        this.armor = armor;
    }

    @Override
    public Optional<IArmor> getArmor() {
        return Optional.ofNullable(armor);
    }
}
