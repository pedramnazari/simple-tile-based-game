package de.pedramnazari.simpletbg.tilemap.service;

import de.pedramnazari.simpletbg.tilemap.model.IItem;

import java.util.Collection;
import java.util.Optional;

public interface IItemService {
    Collection<IItem> getItems();

    Optional<IItem> getItem(int x, int y);

    @Deprecated
    boolean removeItem(IItem item);

    void addItems(Collection<IItem> items);

    void addItem(IItem item);
}
