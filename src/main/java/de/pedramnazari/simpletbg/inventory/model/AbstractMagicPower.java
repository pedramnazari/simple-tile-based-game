package de.pedramnazari.simpletbg.inventory.model;

import de.pedramnazari.simpletbg.tilemap.model.IItem;
import de.pedramnazari.simpletbg.tilemap.model.IMagicPower;

public abstract class AbstractMagicPower implements IMagicPower {

    private final String name;
    private IItem associatedItem;

    public AbstractMagicPower(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public IItem getAssociatedItem() {
        return associatedItem;
    }

    public void setAssociatedItem(IItem associatedItem) {
        this.associatedItem = associatedItem;
    }
}