package de.pedramnazari.simpletbg.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Inventory {

    private Collection<Item> items;

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
