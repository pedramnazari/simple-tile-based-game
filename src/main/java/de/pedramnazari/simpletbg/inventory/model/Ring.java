package de.pedramnazari.simpletbg.inventory.model;

import de.pedramnazari.simpletbg.tilemap.model.IMagicEffect;
import de.pedramnazari.simpletbg.tilemap.model.IRing;

import java.util.Optional;

public class Ring extends Item implements IRing {

    private IMagicEffect magicPower;

    private int attackingPower = 0;

    public Ring(int x, int y, String name, String description, int type) {
        super(x, y, name, description, type);
    }

    @Override
    public void setMagicPower(IMagicEffect magicPower) {
        if (this.magicPower != null) {
            this.magicPower.setAssociatedItem(null);
        }

        this.magicPower = magicPower;
        magicPower.setAssociatedItem(this);
    }

    @Override
    public Optional<IMagicEffect> getMagicPower() {
        return Optional.ofNullable(magicPower);
    }

    @Override
    public int getAttackingPower() {
        return attackingPower;
    }

    @Override
    public void setAttackingPower(int attackingPower) {
        this.attackingPower = attackingPower;
    }
}
