package de.pedramnazari.simpletbg.inventory.model;

import de.pedramnazari.simpletbg.tilemap.model.IItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Inventory implements de.pedramnazari.simpletbg.tilemap.model.IInventory {

    private final Collection<IItem> items;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    @Override
    public void addItem(IItem item) {
        items.add(item);
    }

    @Override
    public void removeItem(IItem item) {
        items.remove(item);
    }

    @Override
    public Collection<IItem> getItems() {
        return List.copyOf(items);
    }

}
