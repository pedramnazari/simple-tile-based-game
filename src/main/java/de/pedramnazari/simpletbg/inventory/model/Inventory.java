package de.pedramnazari.simpletbg.inventory.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Inventory {

    private final Collection<Item> items;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public Collection<Item> getItems() {
        return List.copyOf(items);
    }

}
