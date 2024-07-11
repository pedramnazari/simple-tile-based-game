package de.pedramnazari.simpletbg.tilemap.model;

import java.util.Collection;

public interface IInventory {
    void addItem(IItem item);

    void removeItem(IItem item);

    Collection<IItem> getItems();
}
